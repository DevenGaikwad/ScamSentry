import json
from pathlib import Path
import copy
import pytest
from scamsentry_ml.blocks_v2 import PortableBlocks,validate_blocks
P=Path(__file__).parents[1]/'artifacts/parity/research_blocks_synthetic_v2.json'

def test_portable_research_reference():
    for item in json.loads(P.read_text())['models']:
        model=PortableBlocks(item['bundle'])
        for f in item['fixtures']:
            actual=model.predict(f['text']);assert actual['normalized']==f['expected']['normalized']
            assert actual['logits']==pytest.approx(f['expected']['logits'],abs=1e-10)
        assert item['max_sklearn_logit_difference']<1e-10

def test_block_contract_rejects_implicit_normalization():
    b=json.loads(P.read_text())['models'][0]['bundle'];b['final_l2']=True
    with pytest.raises(ValueError):validate_blocks(b)
