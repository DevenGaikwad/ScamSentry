import json
from pathlib import Path
import pytest
from scamsentry_ml.analysis_v2 import analyze
from scamsentry_ml.contracts import PortableModel
from scamsentry_ml.url_v2 import inspect_one

@pytest.fixture(scope='module')
def model():
    return PortableModel(json.loads((Path(__file__).parents[1]/'artifacts/bundle/model.json').read_text()))

def test_40_is_not_truncation(model):
    text='Read these notes after class. '+' '.join(f'https://u{i}.test' for i in range(40))
    a=analyze(text,model); assert len(a['urls'])==40 and not a['links_truncated']
    b=analyze(text+' https://u40.test',model)
    assert len(b['urls'])==40 and b['links_truncated'] and b['risk']=='unable'

def test_concrete_request_survives_missing_model_and_scope():
    a=analyze('मराठी message send your OTP',None)
    assert a['risk']=='higher' and 'model_unavailable' in a['quality_flags']

def test_limited_states(model):
    assert analyze('https://example.org',model)['estimate'] is None
    assert analyze('https://example.org',model)['risk']=='checking'
    assert analyze('Meet me',model,'ocr')['risk']=='unable'
    assert analyze('zqjzxqjz',model)['risk']=='unable'
    assert analyze('a'*4001,model)['risk']=='unable'

@pytest.mark.parametrize('text,valid,host',[
 ('HTTP://Example.ORG:80/x',True,'example.org'),
 ('https://a@b@review.test',False,None),('https://example.org:',False,None),
 ('https://example.org:0',False,None),('https://example.org:65535',True,'example.org'),
 ('https://[2001:db8::1]/x',True,'2001:db8::1'),('https://[::::]/',False,None),
 ('https://bücher.example',True,'xn--bcher-kva.example'),('https://faß.de',False,None),
 ('https://example.org\\evil',False,None),('https://%65xample.org',False,None),
])
def test_conservative_urls(text,valid,host):
    value=inspect_one(text); assert value['valid']==valid and value['host']==host

@pytest.mark.parametrize('text',['http://0x7f000001','https://example.123','https://faẞ.de','https://[1:2:3:4:5:6:7:8:]/x'])
def test_ambiguous_browser_host_forms_rejected(text):
    assert not inspect_one(text)['valid']
