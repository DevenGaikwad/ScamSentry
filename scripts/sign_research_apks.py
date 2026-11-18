"""Create installable, explicitly TEST-SIGNED previews; never uses an owner's release key."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
from release_identity import release_identity

ROOT=Path(__file__).resolve().parents[1]

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--sdk',type=Path,default=Path(os.environ.get('ANDROID_HOME','/missing-sdk')))
    p.add_argument('--output',type=Path,default=ROOT/'release/install')
    a=p.parse_args();a.output.mkdir(parents=True,exist_ok=True)
    tools=a.sdk/'build-tools/36.0.0';signer=tools/('apksigner.bat' if os.name=='nt' else 'apksigner')
    version, version_code = release_identity()
    inventory=[]
    with tempfile.TemporaryDirectory(prefix='scamsentry-test-signing-') as temporary:
        key=Path(temporary)/'research-test.jks'
        subprocess.run(['keytool','-genkeypair','-keystore',str(key),'-storepass','android','-keypass','android','-alias','research','-keyalg','RSA','-keysize','2048','-validity','3650','-dname','CN=ScamSentry Disposable Research Test, OU=Academic Preview, O=ScamSentry, C=IN'],check=True,capture_output=True)
        for abi in ['arm64-v8a','x86_64','universal']:
            source=ROOT/f'android/app/build/outputs/apk/release/app-{abi}-release-unsigned.apk'
            if not source.is_file():raise FileNotFoundError(source)
            target=a.output/f'ScamSentry_{version}_{abi}_TEST-SIGNED.apk'
            if target.exists():raise FileExistsError(target)
            subprocess.run([str(signer),'sign','--ks',str(key),'--ks-key-alias','research','--ks-pass','pass:android','--key-pass','pass:android','--out',str(target),str(source)],check=True,capture_output=True)
            verify=subprocess.run([str(signer),'verify','--verbose','--print-certs',str(target)],check=True,capture_output=True,text=True)
            aligned=subprocess.run([str(tools/'zipalign'),'-c','-P','16','-v','4',str(target)],capture_output=True,text=True)
            if aligned.returncode:raise ValueError('APK ZIP alignment check failed')
            inventory.append(dict(file=target.name,abi=abi,bytes=target.stat().st_size,sha256=hashlib.sha256(target.read_bytes()).hexdigest(),signing='Disposable TEST identity; not production',signature_verification=verify.stdout.strip().splitlines(),zip_alignment_16k='PASS'))
    commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT,text=True).strip()
    tree=subprocess.check_output(['git','rev-parse','HEAD^{tree}'],cwd=ROOT,text=True).strip()
    (a.output.parent/'signing_report.json').write_text(json.dumps(dict(source_commit=commit,source_tree=tree,version_name=version,version_code=version_code,artifacts=inventory,production_signing=False),indent=2)+'\n')
    print(f'Created and verified {len(inventory)} installable TEST-SIGNED APKs')
if __name__=='__main__':main()
