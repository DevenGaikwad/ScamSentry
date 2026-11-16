"""Tiny fully fictional models test real fit/export consistency from a public checkout."""
import json
import sys
from pathlib import Path
import numpy as np
from threadpoolctl import threadpool_limits
ROOT=Path(__file__).resolve().parents[1];sys.path.insert(0,str(ROOT/'ml/src'))
from scamsentry_ml.optimize_v2 import fit
from scamsentry_ml.blocks_v2 import export_blocks,PortableBlocks
from scamsentry_ml.common import write_json
texts=['Bring your laboratory notes after class.','Meet us in the project library tomorrow.','Your lab appointment is confirmed.','Fictional shop discount this weekend.','Fictional sale on project notebooks.','Fictional promotion buy two notebooks.','Send your OTP to confirm access.','Give your password to the operator.','Reply with your PIN immediately.']
y=np.array([0,0,0,1,1,1,2,2,2]);test=['Bring the project notes.','Send your password immediately.','Ｆｉｃｔｉｏｎａｌ sale 😀','Email bücher@example.org after class.','Code １２３４ remains private.','',"We'll meet tomorrow."]
models=[]
with threadpool_limits(limits=2):
    for family in ['word','hybrid']:
        cfg=dict(family=family,C=2,class_weight='balanced',min_df=1,budget=20000,weights=[.6,.4])
        f,lr,_,_=fit(cfg,texts,y);b=export_blocks(f,lr,1.2,'synthetic-'+family+'-parity-only');p=PortableBlocks(b)
        expected=lr.decision_function(f.transform(test));actual=np.array([p.predict(t)['logits'] for t in test]);assert np.allclose(expected,actual,atol=1e-10)
        models.append(dict(bundle=b,fixtures=[dict(text=t,expected=p.predict(t)) for t in test],max_sklearn_logit_difference=float(np.max(np.abs(expected-actual)))))
write_json(ROOT/'ml/artifacts/parity/research_blocks_synthetic_v2.json',dict(description='Fictional fit/export tests only. Never a production fallback or performance claim.',tolerance=1e-6,models=models))
print('Word and hybrid synthetic reference exports verified')
