"""Package reviewed Git source and verified installable research APKs, without private files."""
import argparse
from pathlib import Path
import hashlib
import json
import subprocess
import zipfile
import tempfile
import sys
ROOT=Path(__file__).resolve().parents[1]
sys.path.insert(0,str(ROOT/'ml/src'))
from scamsentry_ml.release_validation import validate_release


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--output',type=Path,default=ROOT/'release')
    p.add_argument('--source-only',action='store_true')
    args=p.parse_args();out=args.output.resolve();out.mkdir(parents=True,exist_ok=True)
    dirty=subprocess.check_output(['git','status','--porcelain'],cwd=ROOT,text=True).strip()
    if dirty:raise ValueError('Review and commit source before packaging')
    commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT,text=True).strip()
    source=out/'ScamSentry_0.2.0-preview_Source.zip'
    subprocess.run(['git','archive','--format=zip','--prefix=ScamSentry/','-o',str(source),commit],cwd=ROOT,check=True)
    with tempfile.TemporaryDirectory(prefix='scamsentry-archive-check-') as temporary:
        folder=Path(temporary)
        with zipfile.ZipFile(source) as z:
            if z.testzip() is not None:raise ValueError('ZIP CRC failure')
            for info in z.infolist():
                if Path(info.filename).is_absolute() or '..' in Path(info.filename).parts:raise ValueError('Unsafe ZIP entry')
            z.extractall(folder)
            for info in z.infolist():
                if info.external_attr>>16 & 0o111:(folder/info.filename).chmod(0o755)
        validation=validate_release(folder/'ScamSentry','archive')
        if validation['status']!='PASS':raise ValueError(json.dumps(validation))
    (out/'archive_validation.json').write_text(json.dumps(validation,indent=2)+'\n')
    if args.source_only:
        print(f'Source archive verified: {source}');return
    signing=json.loads((out/'signing_report.json').read_text())
    artifacts=[]
    for artifact in signing['artifacts']:
        file=out/'install'/artifact['file']
        if not file.is_file() or hashlib.sha256(file.read_bytes()).hexdigest()!=artifact['sha256']:raise ValueError('Signed APK digest mismatch')
        artifacts.append(dict(artifact,source_commit=commit,min_sdk=26,version_code=2,version_name='0.2.0-preview',channel='research-preview'))
    manifest=json.loads((ROOT/'ml/artifacts/bundle/manifest.json').read_text())
    inventory=dict(schema_version=2,source_commit=commit,model=manifest,artifacts=artifacts,production_signing=False,byte_identical_builds_claimed=False,scope='Research preview; physical phone benchmarks and owner-managed production signing remain separate gates')
    (out/'ARTIFACT_INVENTORY.json').write_text(json.dumps(inventory,indent=2)+'\n')
    (out/'SHA256SUMS.txt').write_text(''.join(a['sha256']+'  install/'+a['file']+'\n' for a in artifacts))
    bundle=out/'ScamSentry_0.2.0_Research_Preview.zip'
    with zipfile.ZipFile(source) as z,zipfile.ZipFile(bundle,'w',zipfile.ZIP_DEFLATED,compresslevel=9) as final:
        for info in z.infolist():final.writestr(info,z.read(info))
        for artifact in artifacts:final.write(out/'install'/artifact['file'],'ScamSentry/install/'+artifact['file'])
        for name in ['ARTIFACT_INVENTORY.json','SHA256SUMS.txt','signing_report.json','archive_validation.json']:
            final.write(out/name,'ScamSentry/'+name)
    print(json.dumps(dict(archive=str(bundle),source_commit=commit,bytes=bundle.stat().st_size,sha256=hashlib.sha256(bundle.read_bytes()).hexdigest(),artifacts=artifacts),indent=2))

if __name__=='__main__':main()
