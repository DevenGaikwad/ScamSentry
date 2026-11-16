import pytest
from scamsentry_ml.metrics_v2 import classification,pipeline

def test_counts_and_denominators():
    # rows actual [[3,1,0],[0,2,1],[1,0,2]]
    y=[0]*4+[1]*3+[2]*3;p=[0,0,0,1,1,1,2,0,2,2]
    m=classification(y,p);s=m['one_vs_rest']['smishing-like']
    assert m['confusion_matrix']==[[3,1,0],[0,2,1],[1,0,2]]
    assert (s['TP'],s['FP'],s['TN'],s['FN'])==(2,1,6,1)
    assert s['recall']==pytest.approx(2/3) and s['specificity']==pytest.approx(6/7)
    assert s['FNR']+s['recall']==pytest.approx(1)
    assert s['FPR']+s['specificity']==pytest.approx(1)
    assert m['accuracy']==m['micro_f1']==.7

def test_absent_and_no_positive_are_not_perfect():
    m=classification([0,0,1],[0,0,0]);s=m['one_vs_rest']['smishing-like']
    assert s['precision'] is None and s['recall'] is None
    assert m['macro_f1']==pytest.approx(.8/3)
    m=classification([0]*90+[2]*10,[0]*100)
    assert m['accuracy']==.9 and m['one_vs_rest']['smishing-like']['recall']==0
    assert m['macro_f1']<.32

def test_deferral_does_not_hide_unresolved_positives():
    m=pipeline([0,2,2],['lower','higher','unable'],[0,2,0])
    assert m['coverage']==pytest.approx(2/3)
    assert m['smishing_deferred']==1 and m['smishing_automatic_high_recall']==.5
