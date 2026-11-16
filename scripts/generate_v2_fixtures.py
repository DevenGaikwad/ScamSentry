"""Fictional regression fixtures; never reads historical or prospective evaluation data."""
import json
import sys
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
sys.path.insert(0,str(ROOT/'ml/src'))
from scamsentry_ml.analysis_v2 import analyze
from scamsentry_ml.contracts import PortableModel, privacy_transform
from scamsentry_ml.export import FIXTURES
model=PortableModel(json.loads((ROOT/'ml/artifacts/bundle/model.json').read_text()))
cases=[dict(id=f'legacy_input_{i:02d}',text=t,source='text',model_available=True) for i,t in enumerate(FIXTURES)]
extras=[
 ('unrelated_example','Example timetable. Send your OTP now.','text',True),
 ('warning_then_request','Never share your OTP. Now send your OTP to confirm.','text',True),
 ('educational_quote','Educational example: "Send your OTP immediately".','text',True),
 ('negated_request','Do not send your password to anyone.','text',True),
 ('quote_then_request','Educational example: "send your PIN". Now send your OTP.','text',True),
 ('missing_model','Please bring the laboratory notes after class.','text',False),
 ('missing_model_request','Send your password to confirm your identity.','text',False),
 ('short_ocr','Meet me tomorrow','ocr',True),
 ('reviewed_ocr','Please bring your project notebook after class.','ocr',True),
 ('weak_features','zqjzxqjz','text',True),
 ('repetitive','a'*100,'text',True),
 ('over_limit','a'*4001,'text',True),
 ('links_40','Read these notes after class. '+' '.join(f'https://u{i}.test' for i in range(40)),'text',True),
 ('links_41','Read these notes after class. '+' '.join(f'https://u{i}.test' for i in range(41)),'text',True),
 ('upper_http','HTTP://Example.ORG:80/x','text',True),
 ('ambiguous_authority','https://a@b@review.test','text',True),
 ('empty_port','https://example.org:','text',True),
 ('numeric_hex','http://0x7f000001','text',True),
 ('numeric_tld','https://example.123','text',True),
 ('idna_capital_sharp','https://faẞ.de','text',True),
 ('ipv6_trailing_colon','https://[1:2:3:4:5:6:7:8:]/x','text',True),
 ('max_port','https://example.org:65535','text',True),
 ('idna_deviation','https://faß.de','text',True),
 ('backslash','https://example.org\\evil','text',True),
 ('ipv6_standalone','https://[2001:db8::1]','text',True),
 ('ipv6_bad','https://[::::]/x','text',True),
 ('invalid_control','Some laboratory notes\x00after class.','text',True),
 ('unsupported_with_request','मराठी message send your OTP','text',False),
 ('threshold_neutral','Bring the project notes before the laboratory session.','text',True),
 ('unicode_contacts','Class notes: email élève@review.test or call १२३४५६७८९० tomorrow.','text',True),
 ('unicode_url_whitespace','Review https://example.org/a\u0085https://review.test/b after class.','text',True),
 ('unicode_host_boundary','कexample.org class notes after laboratory.','text',True),
 ('combining_email','a\u0301@review.test: bring class notes after the laboratory.','text',True),
 ('fullwidth_digits','Meet at room １２３ after class; bring project notes.','text',True),
 ('url_bom','Read https://example.org/a\ufeffsuffix after class.','text',True),
]
cases.extend(dict(id=i,text=t,source=s,model_available=a) for i,t,s,a in extras)
for c in cases:
    c['minimized']=privacy_transform(c['text'])
    c['expected']=analyze(c['text'],model if c['model_available'] else None,c['source'])
result=dict(schema_version=2,tolerance=1e-6,model_sha256=json.loads((ROOT/'ml/artifacts/bundle/manifest.json').read_text())['model_sha256'],description='Fictional end-to-end contracts, not accuracy evidence',fixtures=cases)
for rel in ['ml/artifacts/parity/full_golden_v2.json','android/app/src/main/assets/model/full_golden_v2.json']:
    p=ROOT/rel;p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps(result,ensure_ascii=False,indent=2)+'\n')
print(f'Generated {len(cases)} complete contract fixtures')
