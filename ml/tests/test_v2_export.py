import copy
import json
from pathlib import Path
import numpy as np
import pytest
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from scamsentry_ml.common import CLASSES,digest,write_json
from scamsentry_ml.contracts import PortableModel,ngrams,privacy_transform
from scamsentry_ml.export import export
from scamsentry_ml.bundle import validate

@pytest.fixture
def fitted_run(tmp_path):
    texts=['Bring the laboratory notes after class.','Meet us near the project library.','Our group will meet after the lecture.',
      'Fictional shop discount this weekend.','Fictional sale on notebooks today.','Fictional promotion buy a notebook.',
      'Send your OTP to confirm access.','Give your password to the operator.','Reply with your PIN immediately.']
    xtexts=[privacy_transform(t) for t in texts]
    vec=TfidfVectorizer(analyzer=ngrams,lowercase=False,min_df=1,sublinear_tf=True)
    x=vec.fit_transform(xtexts);lr=LogisticRegression(C=2,random_state=42,max_iter=300).fit(x,[0,0,0,1,1,1,2,2,2])
    model=dict(schema_version=1,model_id='synthetic-export-test',classes=CLASSES,normalizer='nfkc_ascii_ws_v1',privacy_transform='typed_tokens_v1',ngram_range=[3,5],sublinear_tf=True,l2_normalize=True,vocabulary=vec.get_feature_names_out().tolist(),idf=vec.idf_.tolist(),coefficients=lr.coef_.tolist(),intercept=lr.intercept_.tolist(),temperature=1.3,policy=dict(version='ordinal_v1',confidence=.62,margin=.18))
    run=tmp_path/'run';write_json(run/'model.json',model);write_json(run/'frozen.json',dict(protocol_id='synthetic-only',model_sha256=digest(run/'model.json'),evaluation_report=None))
    return run,vec,lr

def test_public_checkout_fit_export_math(fitted_run,tmp_path):
    run,vec,lr=fitted_run
    out=export(run,tmp_path/'staging');bundle=validate(out);portable=PortableModel(bundle)
    texts=['Bring your laboratory notes.','Reply with the password now.','😀 ＡＢＣ 555 https://example.org']
    logits=lr.decision_function(vec.transform([privacy_transform(t) for t in texts]))
    assert np.allclose(logits,[portable.predict(t)['logits'] for t in texts],atol=1e-10)
    assert json.loads((out/'manifest.json').read_text())['model_id']=='synthetic-export-test'
    assert json.loads((out/'manifest.json').read_text())['evaluation_report'] is None

@pytest.mark.parametrize('mutate',[
 lambda m:m['policy'].update(confidence=-.1),lambda m:m['policy'].update(margin=float('nan')),
 lambda m:m['policy'].update(version='unknown'),lambda m:m['idf'].__setitem__(0,True),
 lambda m:m.update(classes=list(reversed(CLASSES))),lambda m:m.update(sublinear_tf='true'),
])
def test_contract_rejects_malformed(fitted_run,tmp_path,mutate):
    run,_,_=fitted_run;out=export(run,tmp_path/'staging');m=json.loads((out/'model.json').read_text());mutate(m)
    (out/'model.json').write_text(json.dumps(m));manifest=json.loads((out/'manifest.json').read_text());manifest.update(model_sha256=digest(out/'model.json'),byte_size=(out/'model.json').stat().st_size);write_json(out/'manifest.json',manifest)
    with pytest.raises((ValueError,TypeError)):validate(out)
