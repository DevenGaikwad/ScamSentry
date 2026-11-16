"""Stage a frozen, non-executable export. Production promotion is a separate action."""
import argparse
from pathlib import Path
import shutil
from .common import ROOT,digest,read_json,write_json
from .legacy_v1 import PortableModel,decide,observations,urls
from .bundle import validate

FIXTURES = ["Let's meet in the library after class.", "Please bring your lab notebook before 9 tomorrow.", "Send your OTP immediately to keep the account active.", "Fictional shop: 20 percent discount this weekend. Reply STOP to unsubscribe.", 'Educational example: never share your OTP. A scam may say "send your password".', "https://example.org/notes", "https://bank.example@review.test/login", "https://xn--bcher-kva.example", "Meet\t me\n at 5", "ＡＢＣ full width café e\u0301 😀", "पैसे पाठवा", "", "abc\u200bdefghijk", "aaaa" * 1000, "  ", "Visit https://example.org/a, and https://review.test/b.", "https://example.org\u00a0after the link", "A code １２３４ and an emoji 😀 in this message.", "Email bücher@example.org after class.", "Quoted example: Send your password immediately.", "https://[2001:db8::1]/notes", "https://example.org:99999", "intent://review.test/path", "zzzqqqwwwyyy" ]

def export(path,output=None):
    path=Path(path);path=ROOT/path if not path.is_absolute() else path
    frozen=read_json(path/'frozen.json');raw=read_json(path/'model.json')
    if digest(path/'model.json')!=frozen['model_sha256']: raise ValueError('Model changed since freeze')
    model_id=raw['model_id']
    out=Path(output) if output else ROOT/'artifacts/exports'/model_id
    out=out.resolve()
    protected=[ROOT/'artifacts/bundle',ROOT.parent/'android/app/src/main/assets',ROOT.parent/'web/public']
    if any(out==p.resolve() or out.is_relative_to(p.resolve()) for p in protected): raise ValueError('Export must stage outside production asset paths')
    if out.exists() and any(out.iterdir()): raise ValueError('Export destination is not empty; choose a new staging directory')
    out.mkdir(parents=True,exist_ok=True);shutil.copy2(path/'model.json',out/'model.json')
    evaluation=frozen.get('evaluation_report')
    write_json(out/'manifest.json',dict(schema_version=1,model_sha256=frozen['model_sha256'],model_id=model_id,protocol_id=frozen['protocol_id'],evaluation_report=evaluation,format='utf8-json-float64',byte_size=(out/'model.json').stat().st_size,promotion_status='staged_only'))
    model=PortableModel(validate(out));fixtures=[]
    for i,text in enumerate(FIXTURES):
        pred=model.predict(text)
        fixtures.append(dict(id=f'golden_{i:02d}',text=text,**pred,category=decide(text,pred['probabilities'],observations(text),urls(text),model.bundle['policy']),urls=urls(text)))
    write_json(out/'golden.json',dict(tolerance=1e-6,contract='archived_numeric_and_policy_v1',model_sha256=frozen['model_sha256'],fixtures=fixtures))
    return out

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--run',required=True,type=Path);p.add_argument('--output',type=Path)
    a=p.parse_args();print(f'Staged export: {export(a.run,a.output)}; no app assets were changed')
if __name__=='__main__': main()
