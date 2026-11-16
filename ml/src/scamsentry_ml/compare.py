"""Supplementary train/development experiments, never opens final test."""
import argparse
import time
import warnings
import joblib
import numpy as np
from scipy.special import softmax
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import f1_score, classification_report
from sklearn.model_selection import StratifiedGroupKFold, GroupShuffleSplit
from sklearn.neural_network import MLPClassifier
from threadpoolctl import threadpool_limits
from .common import ROOT, write_json
from .contracts import ngrams
from .train import load_partitions


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--epochs",type=int,default=18)
    a=p.parse_args()
    if not 1<=a.epochs<=30: p.error("Epoch budget must be 1..30")
    parts=load_partitions()
    train,dev=parts["train"],parts["development"]
    texts=[r["text"] for r in train]; y=np.array([r["label"] for r in train]); groups=[r["group_id"] for r in train]
    folds=StratifiedGroupKFold(n_splits=3,shuffle=True,random_state=43)
    cv=[]
    start=time.perf_counter()
    with threadpool_limits(limits=2):
        for fit,val in folds.split(texts,y,groups):
            v=TfidfVectorizer(analyzer=ngrams,lowercase=False,min_df=2,max_features=20000,sublinear_tf=True)
            x=v.fit_transform([texts[i] for i in fit])
            estimator=LogisticRegression(C=2,max_iter=350,class_weight="balanced",random_state=42).fit(x,y[fit])
            pred=estimator.predict(v.transform([texts[i] for i in val]))
            cv.append(dict(macro_f1=f1_score(y[val],pred,average="macro"),fit_records=len(fit),validation_records=len(val),groups_isolated=True))
        curves=[]
        for fraction in [.3,.6,1.]:
            if fraction==1: ix=np.arange(len(train))
            else: ix,_=next(GroupShuffleSplit(n_splits=1,train_size=fraction,random_state=44).split(texts,y,groups))
            v=TfidfVectorizer(analyzer=ngrams,lowercase=False,min_df=2,max_features=20000,sublinear_tf=True)
            x=v.fit_transform([texts[i] for i in ix]); e=LogisticRegression(C=2,max_iter=350,class_weight="balanced",random_state=42).fit(x,y[ix])
            curves.append(dict(training_fraction=fraction,training_groups=len(set(groups[i] for i in ix)),development_macro_f1=f1_score([r["label"] for r in dev],e.predict(v.transform([r["text"] for r in dev])),average="macro")))
        # Small neural alternative on character TF-IDF, trained from scratch on train.
        v=TfidfVectorizer(analyzer=ngrams,lowercase=False,min_df=2,max_features=4000,sublinear_tf=True,dtype=np.float32)
        x=v.fit_transform(texts); xd=v.transform([r["text"] for r in dev]); yd=np.array([r["label"] for r in dev])
        mlp=MLPClassifier(hidden_layer_sizes=(32,),activation="relu",solver="adam",batch_size=128,learning_rate_init=.003,alpha=.001,random_state=42,max_iter=1,early_stopping=False)
        epochs=[]; best=-1.;patience=0
        weights=np.array([len(y)/(3*np.sum(y==label)) for label in y])
        for epoch in range(a.epochs):
            mlp.partial_fit(x,y,classes=[0,1,2],sample_weight=weights)
            score=f1_score(yd,mlp.predict(xd),average="macro")
            epochs.append(dict(epoch=epoch+1,development_macro_f1=score))
            if score>best+.001:
                best=score;patience=0;joblib.dump(dict(vectorizer=v,estimator=mlp),ROOT/'artifacts/private/neural_best.joblib')
            else: patience+=1
            if patience>=4: break
        parameters=sum(w.size for w in mlp.coefs_)+sum(b.size for b in mlp.intercepts_)
    write_json(ROOT/"artifacts/research_comparison.json",dict(final_test_accessed=False,group_cv=cv,learning_curve=curves,neural=dict(family="character TF-IDF MLP",architecture="4000 sparse character features → 32 ReLU → 3 softmax",parameters=parameters,epochs=epochs,best_development_macro_f1=best,export_status="Research only; no mobile runtime or parity. Linear bundle retained for measured development quality and simpler audited deployment."),seconds=time.perf_counter()-start,limitations=["One seed for learning curve; not a variability estimate", "No pretrained encoder downloaded", "Neural development stopping is disclosed and uses no test labels"]))
    print(dict(group_cv_mean=float(np.mean([r['macro_f1'] for r in cv])),neural_best=best,parameters=parameters,seconds=time.perf_counter()-start))


if __name__=="__main__": main()
