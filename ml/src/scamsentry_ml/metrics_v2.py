"""Fixed-label metrics with explicit zero denominators and operational deferral."""
import math
import numpy as np
from sklearn.metrics import confusion_matrix, matthews_corrcoef, log_loss
from .common import CLASSES

def ratio(n,d): return float(n/d) if d else None

def classification(y,pred,probabilities=None):
    y=np.asarray(y,dtype=int); pred=np.asarray(pred,dtype=int)
    if len(y)==0 or y.shape!=pred.shape or not np.isin(y,[0,1,2]).all() or not np.isin(pred,[0,1,2]).all(): raise ValueError('Invalid nonempty labels')
    cm=confusion_matrix(y,pred,labels=[0,1,2]); n=len(y)
    classes={}
    for k,name in enumerate(CLASSES):
        tp=int(cm[k,k]);fn=int(cm[k,:].sum()-tp);fp=int(cm[:,k].sum()-tp);tn=n-tp-fn-fp
        precision=ratio(tp,tp+fp);recall=ratio(tp,tp+fn)
        classes[name]=dict(TP=tp,FP=fp,TN=tn,FN=fn,support=tp+fn,precision=precision,recall=recall,specificity=ratio(tn,tn+fp),FPR=ratio(fp,fp+tn),FNR=ratio(fn,fn+tp),NPV=ratio(tn,tn+fn),f1=ratio(2*tp,2*tp+fp+fn),f2=ratio(5*tp,5*tp+4*fn+fp))
    f1=[classes[k]['f1'] or 0 for k in CLASSES]
    result=dict(records=n,confusion_matrix=cm.tolist(),confusion_rows='actual',class_order=CLASSES,one_vs_rest=classes,accuracy=float(np.trace(cm)/n),micro_f1=float(np.trace(cm)/n),macro_f1=float(np.mean(f1)),weighted_f1=float(sum(f1[k]*cm[k,:].sum() for k in range(3))/n),balanced_accuracy_fixed_labels=float(np.mean([classes[k]['recall'] or 0 for k in CLASSES])),MCC=float(matthews_corrcoef(y,pred)),ordinary_to_smishing=ratio(int(((y==0)&(pred==2)).sum()),int((y==0).sum())),ordinary_to_any_nonordinary=ratio(int(((y==0)&(pred!=0)).sum()),int((y==0).sum())),undefined='null; fixed-label averages substitute 0 for undefined class terms')
    if probabilities is not None:
        p=np.asarray(probabilities,dtype=float)
        if p.shape!=(n,3) or not np.isfinite(p).all() or (p<0).any() or not np.allclose(p.sum(1),1): raise ValueError('Invalid probabilities')
        bins=[];confidence=p.max(1);ece=0
        for i in range(10):
            mask=(confidence>=i/10)&(confidence<((i+1)/10) if i<9 else confidence<=1)
            if mask.any():
                acc=float((pred[mask]==y[mask]).mean());conf=float(confidence[mask].mean());count=int(mask.sum())
                bins.append(dict(lower=i/10,upper=(i+1)/10,count=count,accuracy=acc,confidence=conf));ece+=count/n*abs(acc-conf)
        result.update(log_loss=float(log_loss(y,p,labels=[0,1,2])),brier_multiclass_sum=float(np.mean(((p-np.eye(3)[y])**2).sum(1))),ece_10_equal_width=float(ece),reliability=bins)
    return result

def pipeline(y,risks,pred):
    y=np.asarray(y);risks=np.asarray(risks);pred=np.asarray(pred)
    accepted=risks!='unable';high=risks=='higher';positive=y==2;ordinary=y==0
    return dict(records=len(y),accepted=int(accepted.sum()),abstained=int((~accepted).sum()),coverage=float(accepted.mean()),accepted_model_argmax_error=ratio(int(((pred!=y)&accepted).sum()),int(accepted.sum())),abstained_model_argmax_error=ratio(int(((pred!=y)&~accepted).sum()),int((~accepted).sum())),smishing_support=int(positive.sum()),smishing_high=int((positive&high).sum()),smishing_deferred=int((positive&~accepted).sum()),smishing_not_high=int((positive&~high).sum()),smishing_automatic_high_recall=ratio(int((positive&high).sum()),int(positive.sum())),ordinary_support=int(ordinary.sum()),ordinary_high=int((ordinary&high).sum()),ordinary_high_rate=ratio(int((ordinary&high).sum()),int(ordinary.sum())),deferred_by_class={CLASSES[k]:int(((y==k)&~accepted).sum()) for k in range(3)},category_counts={k:int((risks==k).sum()) for k in ['higher','checking','lower','unable']})
