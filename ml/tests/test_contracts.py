import copy
import json
from pathlib import Path
import numpy as np
import pytest
from scamsentry_ml.contracts import *
from scamsentry_ml.common import CLASSES, write_json
from scamsentry_ml.bundle import validate
from scamsentry_ml.splits import verify


def test_normalization_codepoints():
    assert normalize(" Ａ\t B  😀 ")=="a b 😀"
    assert ngrams("😀ab")==["😀ab"]
    assert normalize("e\u0301")=="é"
    with pytest.raises(ValueError): normalize("\ud800")


@pytest.mark.parametrize("text,host,codes",[
    ("https://bank.example@review.test/login","review.test",["userinfo"]),
    ("https://bücher.example","xn--bcher-kva.example",["idn"]),
    ("http://example.org","example.org",["unencrypted_scheme"]),
    ("https://example.org:99999",None,["unsupported_or_malformed"]),
    ("intent://review.test/x",None,["unsupported_or_malformed"]),
    ("https://%65xample.org",None,["unsupported_or_malformed"]),
])
def test_url_contract(text,host,codes):
    u=urls(text)[0];assert u['host']==host;assert u['codes']==codes


def test_rules_preserve_context_and_urgency():
    assert any(o['code']=='secret_request' for o in observations('Send me your OTP now'))
    assert not any(o['code']=='secret_request' for o in observations('Never share your OTP.'))
    text='Urgent: bring the laboratory notebook before class.'
    assert decide(text,[.9,.05,.05],observations(text),[])=='lower'
    assert decide(text,None,observations(text),[])=='unable'
    assert decide('पैसे पाठवा',[.9,.05,.05],[],[])=='unable'
    assert decide('a'*4001,[.9,.05,.05],[],[])=='unable'


def test_privacy_minimization_idempotent():
    text='Send 123456 to person@example.org or visit https://example.org/?token=abc'
    value=privacy_transform(text)
    assert '123456' not in value and 'person@' not in value and 'token=abc' not in value
    assert privacy_transform(value)==value


def test_multiple_urls_and_no_email_false_match():
    assert len(urls('Visit https://example.org/a, then https://review.test/b.'))==2
    assert urls('me@example.org')==[]
    assert urls('bank.example.com.attacker.xyz')[0]['host']=='bank.example.com.attacker.xyz'
    assert urls('Review example.org.')[0]['host']=='example.org'


def test_partition_guard_detects_template_leak(tmp_path):
    membership=[dict(record_id=f'r{i}',normalized_digest=f'd{i}',group_id='same' if i<2 else f'g{i}',partition=p) for i,p in enumerate(['train','test','development','calibration'])]
    path=tmp_path/'splits.json';write_json(path,dict(membership=membership))
    with pytest.raises(ValueError,match='leakage'): verify(path)



def test_corrupt_bundle_rejected(tmp_path):
    root=Path(__file__).resolve().parents[1]/'artifacts/bundle'
    (tmp_path/'model.json').write_bytes((root/'model.json').read_bytes()+b' ')
    (tmp_path/'manifest.json').write_bytes((root/'manifest.json').read_bytes())
    with pytest.raises(ValueError,match='digest'): validate(tmp_path)
