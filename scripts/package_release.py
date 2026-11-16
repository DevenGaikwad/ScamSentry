"""Package tracked/reviewed source and generated APKs, excluding private developer material."""
from pathlib import Path
import hashlib
import json
import subprocess
import zipfile
import shutil

root=Path(__file__).resolve().parents[1]
output=root.parent/'deliverables';output.mkdir(exist_ok=True)
files=subprocess.check_output(['git','ls-files'],cwd=root,text=True).splitlines()
if not files:raise SystemExit('Commit/review source before packaging')
commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=root,text=True).strip()
install=output/'install';install.mkdir(exist_ok=True)
artifacts=[]
for source,name in [(root/'android/app/build/outputs/apk/debug/app-debug.apk','ScamSentry_0.1.0_Debug.apk'),(root/'android/app/build/outputs/apk/release/app-release-unsigned.apk','ScamSentry_0.1.0_Release_Unsigned.apk')]:
    if source.exists():
        target=install/name;shutil.copy2(source,target)
        artifacts.append({'file':name,'sha256':hashlib.sha256(target.read_bytes()).hexdigest(),'bytes':target.stat().st_size,'source_commit':commit,'signing':'debug/test identity' if 'Debug' in name else 'unsigned; not directly installable'})
manifest=json.loads((root/'ml/artifacts/bundle/manifest.json').read_text())
inventory={'source_commit':commit,'model':manifest,'artifacts':artifacts,'status':'Research preview; read gate report for blocked device/signing/publication work','byte_identical_builds_claimed':False}
(output/'ARTIFACT_INVENTORY.json').write_text(json.dumps(inventory,indent=2)+'\n')
(output/'SHA256SUMS.txt').write_text(''.join(f"{a['sha256']}  install/{a['file']}\n" for a in artifacts))
archive=output/'ScamSentry_0.1.0_Research_Preview.zip'
with zipfile.ZipFile(archive,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=9) as z:
    for name in files:
        p=root/name
        if p.is_file():z.write(p,'ScamSentry/'+name)
    for a in artifacts:z.write(install/a['file'],'ScamSentry/install/'+a['file'])
    z.write(output/'ARTIFACT_INVENTORY.json','ScamSentry/ARTIFACT_INVENTORY.json')
    z.write(output/'SHA256SUMS.txt','ScamSentry/SHA256SUMS.txt')
with zipfile.ZipFile(archive) as z:
    assert z.testzip() is None
    for name in z.namelist():
        assert not any(part in name for part in ['/data/raw/','/data/processed/','/.git/','/.gradle/','.jks','.keystore','.joblib','local.properties'])
print(json.dumps({'archive':str(archive),'bytes':archive.stat().st_size,'sha256':hashlib.sha256(archive.read_bytes()).hexdigest(),'source_commit':commit,'APKs':artifacts},indent=2))
