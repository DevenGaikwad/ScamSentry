"""Predeclared grouped exploratory search. Does not score the historical test."""
import argparse
import csv
import json
import platform
import resource
import subprocess
import time
import warnings
from pathlib import Path
import numpy as np
import sklearn
from scipy.optimize import minimize_scalar
from scipy.special import softmax
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.pipeline import FeatureUnion
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import StratifiedGroupKFold
from sklearn.metrics import f1_score,log_loss
from threadpoolctl import threadpool_limits
from .common import ROOT,CLASSES,read_json,write_json,digest
from .contracts import PortableModel,privacy_transform
from .blocks_v2 import char_features,word_features,PortableBlocks,export_blocks
from .analysis_v2 import analyze
from .metrics_v2 import classification,pipeline
from .splits import verify


def features(cfg):
    blocks=[];weights={}
    for kind in (['char','word'] if cfg['family']=='hybrid' else [cfg['family']]):
        blocks.append((kind,TfidfVectorizer(analyzer=char_features if kind=='char' else word_features,lowercase=False,min_df=cfg['min_df'],max_features=cfg['budget'],sublinear_tf=True,dtype=np.float64)))
        weights[kind]=cfg.get('weights',[1,1])[0 if kind=='char' else 1] if cfg['family']=='hybrid' else 1
    return FeatureUnion(blocks,transformer_weights=weights)

def augment(texts,y,severity):
    if not severity:return list(texts),np.asarray(y)
    rng=np.random.default_rng(42);extra=[]
    for text in texts:
        chars=[]
        for c in text:
            if c.lower() in ('o','l') and rng.random()<severity: c='0' if c.lower()=='o' else '1'
            if c==' ' and rng.random()<severity*.2: c='\u200b '
            chars.append(c)
        extra.append(''.join(chars))
    return list(texts)+extra,np.concatenate([y,y])

def fit(cfg,texts,y):
    xt,yt=augment(texts,y,cfg.get('noise',0));f=features(cfg);start=time.perf_counter()
    with warnings.catch_warnings(record=True) as caught:
        warnings.simplefilter('always');x=f.fit_transform(xt)
        lr=LogisticRegression(C=cfg['C'],class_weight=cfg['class_weight'],max_iter=500,random_state=42).fit(x,yt)
    return f,lr,time.perf_counter()-start,[str(w.message) for w in caught]

def paired_ci(y,base,new,groups,n):
    rng=np.random.default_rng(42);unique=np.unique(groups);indices={g:np.flatnonzero(groups==g) for g in unique};deltas=[]
    for _ in range(n):
        ix=np.concatenate([indices[g] for g in rng.choice(unique,len(unique),replace=True)])
        deltas.append(f1_score(y[ix],new[ix],labels=[0,1,2],average='macro',zero_division=0)-f1_score(y[ix],base[ix],labels=[0,1,2],average='macro',zero_division=0))
    return dict(replicates=n,unit='designated parent group',seed=42,macro_f1_difference_95ci=np.quantile(deltas,[.025,.975]).tolist())

def thresholds(y,p):
    rows=[];pred=p.argmax(1);ordered=np.sort(p,axis=1)
    for confidence in [.50,.62,.75,.85]:
        for margin in [.05,.18,.30]:
            accepted=(ordered[:,-1]>=confidence)&(ordered[:,-1]-ordered[:,-2]>=margin)
            high=accepted&(pred==2);miss=(y==2)&~high;nuisance=(y==0)&high;promo=(y==1)&high
            rows.append(dict(confidence=confidence,margin=margin,coverage=float(accepted.mean()),accepted_argmax_error=float((pred[accepted]!=y[accepted]).mean()) if accepted.any() else None,smishing_high_recall=float(high[y==2].mean()),ordinary_high_rate=float(high[y==0].mean()),cost_per_record=float((5*miss.sum()+nuisance.sum()+.5*promo.sum()+.25*(~accepted).sum())/len(y))))
    binary=[]
    for t in [.20,.35,.50,.65,.80]:
        high=p[:,2]>=t;tp=int((high&(y==2)).sum());fp=int((high&(y!=2)).sum());fn=int((~high&(y==2)).sum());tn=int((~high&(y!=2)).sum())
        binary.append(dict(threshold=t,TP=tp,FP=fp,FN=fn,TN=tn,precision=tp/(tp+fp) if tp+fp else None,recall=tp/(tp+fn),FPR=fp/(fp+tn),FNR=fn/(fn+tp)))
    return dict(scope='Model-led gates only; deterministic observations are evaluated separately in full_pipeline',confidence_margin=rows,smishing_score=binary)

def run(config_path):
    config=read_json(config_path);out=ROOT/'artifacts/v2'/config['run_id']
    if out.exists():raise ValueError('Run exists; never overwrite experiment results')
    plan=ROOT.parent/'docs/ML_OPTIMIZATION_PLAN.md';out.mkdir(parents=True)
    write_json(out/'preregistration.json',dict(config=config,plan_sha256=digest(plan),config_sha256=digest(config_path),status='frozen before fits',created_at='2026-11-16'))
    membership=verify(ROOT/'artifacts/splits.json');part_by_id={r['record_id']:r['partition'] for r in membership['membership']}
    if digest(ROOT/'data/processed/messages.json')!=membership['processed_digest']:raise ValueError('Data digest mismatch')
    eligible=[r for r in read_json(ROOT/'data/processed/messages.json') if part_by_id[r['record_id']] in ['train','development','calibration']]
    parts={p:[r for r in eligible if part_by_id[r['record_id']]==p] for p in ['train','development','calibration']}
    train,dev,cal=[parts[p] for p in ['train','development','calibration']]
    needed={r['row'] for r in dev};original={}
    with (ROOT/'data/raw/Dataset_5971.csv').open(encoding='utf-8-sig',newline='') as stream:
        for i,row in enumerate(csv.DictReader(stream),2):
            if i in needed:original[i]=row['TEXT']
    texts=[r['text'] for r in train];y=np.array([r['label'] for r in train]);groups=np.array([r['group_id'] for r in train])
    td=[r['text'] for r in dev];yd=np.array([r['label'] for r in dev]);gd=np.array([r['group_id'] for r in dev]);tc=[r['text'] for r in cal];yc=np.array([r['label'] for r in cal])
    start=time.perf_counter();folds=list(StratifiedGroupKFold(n_splits=3,shuffle=True,random_state=42).split(texts,y,groups))
    write_json(out/'fold_membership.json',[dict(fold=i,training_groups=sorted(set(groups[a])),validation_groups=sorted(set(groups[b])),overlap=len(set(groups[a])&set(groups[b]))) for i,(a,b) in enumerate(folds)])
    cv=[];reports=[];baseline=PortableModel(read_json(ROOT/'artifacts/bundle/model.json'))
    base_probs=np.array([baseline.predict(t)['probabilities'] for t in td]);base_pred=base_probs.argmax(1)
    def report(name,model,probs,extra):
        pred=probs.argmax(1);scores=classification(yd,pred,probs)
        risks=[analyze(original[r['row']],model)['risk'] for r in dev]
        robust={}
        for family,transform in {'case':str.upper,'spacing':lambda t:t.replace(' ','  \t'),'zero_width':lambda t:t.replace(' ','\u200b '),'severe_ocr_substitution':lambda t:t.replace('o','0').replace('l','1')}.items():
            rp=np.array([model.predict(transform(t))['probabilities'] for t in td]);robust[family]=classification(yd,rp.argmax(1),rp)
        item=dict(name=name,model_id=model.bundle['model_id'],partition='development',independent_groups=len(set(gd)),model_only=scores,full_pipeline=pipeline(yd,risks,pred),robustness=robust,thresholds=thresholds(yd,probs),paired_vs_baseline=paired_ci(yd,base_pred,pred,gd,config['bootstrap']),**extra)
        write_json(out/f'{name}.json',item)
        write_json(out/f'{name}_predictions.json',[dict(record_id=r['record_id'],group_id=r['group_id'],label=int(yd[i]),probabilities=probs[i].tolist(),category=risks[i]) for i,r in enumerate(dev)])
        reports.append(item);print(f"{name}: dev macro-F1={scores['macro_f1']:.5f}, smishing recall={scores['one_vs_rest']['smishing-like']['recall']:.5f}",flush=True)
    report('archived_baseline',baseline,base_probs,dict(fit_seconds=None,serialized_model_bytes=(ROOT/'artifacts/bundle/model.json').stat().st_size,promotion_status='retained production baseline'))
    with threadpool_limits(limits=2):
        for cfg in config['candidates']:
            record=dict(config=cfg,folds=[],status='RUNNING')
            try:
                for fold,(a,b) in enumerate(folds):
                    assert not set(groups[a])&set(groups[b])
                    f,lr,secs,caught=fit(cfg,[texts[i] for i in a],y[a]);p=lr.predict(f.transform([texts[i] for i in b]));m=classification(y[b],p)
                    record['folds'].append(dict(fold=fold,macro_f1=m['macro_f1'],smishing_recall=m['one_vs_rest']['smishing-like']['recall'],ordinary_to_smishing=m['ordinary_to_smishing'],fit_seconds=secs,warnings=caught,features=sum(len(v.vocabulary_) for _,v in f.transformer_list)))
                record['mean_macro_f1']=float(np.mean([x['macro_f1'] for x in record['folds']]));record['eligible']=bool(np.mean([x['smishing_recall'] for x in record['folds']])>=.8 and np.mean([x['ordinary_to_smishing'] for x in record['folds']])<=.03)
                f,lr,secs,caught=fit(cfg,texts,y)
                logits=lr.decision_function(f.transform(tc));opt=minimize_scalar(lambda logt:log_loss(yc,softmax(logits/np.exp(logt),axis=1),labels=[0,1,2]),bounds=(-2,2),method='bounded');temperature=float(np.exp(opt.x))
                bundle=export_blocks(f,lr,temperature,'ss-research-'+cfg['name']+'-v2')
                private=ROOT/'artifacts/private/v2'/config['run_id']/cfg['name'];write_json(private/'model.json',bundle)
                portable=PortableBlocks(bundle);expected=lr.decision_function(f.transform(td));got=np.array([portable.predict(t)['logits'] for t in td]);delta=float(np.max(np.abs(expected-got)))
                if delta>1e-9:raise ValueError(f'Export math mismatch: {delta}')
                probs=softmax(expected/temperature,axis=1)
                report(cfg['name'],portable,probs,dict(config=cfg,fit_seconds=secs,warnings=caught,temperature=temperature,export_max_logit_difference=delta,serialized_model_bytes=(private/'model.json').stat().st_size,model_sha256=digest(private/'model.json'),phone_latency=None,browser_latency=None,promotion_status='research_only'))
                record['status']='PASS'
            except Exception as e:
                record.update(status='FAIL',error=repr(e));print(cfg['name'],repr(e),flush=True)
            cv.append(record);write_json(out/'search.json',cv)
    eligible_cv=[x for x in cv if x['status']=='PASS' and x.get('eligible')]
    best=max(eligible_cv,key=lambda x:x['mean_macro_f1'])['config']['name'] if eligible_cv else None
    write_json(out/'summary.json',dict(protocol_id=config['protocol_id'],run_id=config['run_id'],plan_sha256=digest(plan),config_sha256=digest(config_path),dataset_sha256=digest(ROOT/'data/processed/messages.json'),split_sha256=digest(ROOT/'artifacts/splits.json'),source_commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT,text=True).strip(),code_dirty=True,code_sha256=digest(__file__),environment=dict(python=platform.python_version(),sklearn=sklearn.__version__,numpy=np.__version__,platform=platform.platform(),threads=2),partitions={p:dict(records=len(rows),groups=len({r['group_id'] for r in rows})) for p,rows in parts.items()},historical_test_scored=False,blind_evaluation=False,cv_selected=best,wall_seconds=time.perf_counter()-start,peak_process_rss_kib=resource.getrusage(resource.RUSAGE_SELF).ru_maxrss,promotion='RETAIN_BASELINE: no prospective holdout or physical-phone measurements',results=[dict(name=r['name'],macro_f1=r['model_only']['macro_f1'],accuracy=r['model_only']['accuracy'],smishing=r['model_only']['one_vs_rest']['smishing-like'],coverage=r['full_pipeline']['coverage'],ordinary_high_rate=r['full_pipeline']['ordinary_high_rate'],noise_macro_f1=r['robustness']['severe_ocr_substitution']['macro_f1'],model_bytes=r['serialized_model_bytes'],paired_vs_baseline=r['paired_vs_baseline']) for r in reports]))
    print(f'Reports frozen in {out}',flush=True)

def main():
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--config',required=True,type=Path);a=p.parse_args();run(a.config)
if __name__=='__main__':main()
