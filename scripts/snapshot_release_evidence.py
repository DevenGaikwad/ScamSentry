"""Preserve observed public preview reports/captures after successful publication."""
from pathlib import Path
import hashlib
import json
import os
import shutil
import subprocess

ROOT=Path(__file__).resolve().parents[1]
RELEASE=ROOT/'release'
TARGET=ROOT/'assets/evidence/v2/final'

def main():
    commit=os.environ['GITHUB_SHA']
    repository=os.environ['GITHUB_REPOSITORY']
    if repository!='DevenGaikwad/ScamSentry' or os.environ['GITHUB_REF']!='refs/heads/main':
        raise ValueError('Evidence publication is restricted to the authorized repository/main')
    remote=subprocess.check_output(['git','ls-remote','origin','refs/heads/main'],cwd=ROOT,text=True).split()[0]
    if remote!=commit:
        raise ValueError('Main advanced; preserve release assets without overwriting newer source')
    TARGET.mkdir(parents=True,exist_ok=True)
    for name in ['RELEASE_VERIFICATION.json','ARTIFACT_INVENTORY.json','apk_inspection.json','archive_validation.json','full_package_validation.json','DOWNLOAD_SHA256SUMS.txt','model_manifest.json']:
        shutil.copy2(RELEASE/name,TARGET/name)
    for image in RELEASE.glob('native_*.png'):
        shutil.copy2(image,TARGET/image.name)
    browser=RELEASE/'browser-artifact/assets/evidence/v2'
    for name in ['web_desktop-chromium_home.png','web_desktop-chromium_result.png','web_mobile-chromium_result.png','web_desktop-chromium_timings.json']:
        shutil.copy2(browser/name,TARGET/name)
    files=[dict(file=p.name,bytes=p.stat().st_size,sha256=hashlib.sha256(p.read_bytes()).hexdigest()) for p in sorted(TARGET.iterdir()) if p.is_file()]
    (TARGET/'SNAPSHOT_PROVENANCE.json').write_text(json.dumps(dict(source_commit=commit,workflow_run=os.environ['GITHUB_RUN_ID'],scope='Observed CI reports and original fictional runtime captures; no APKs, raw datasets or private keys in Git',files=files),indent=2)+'\n')
    subprocess.run(['git','add','--','assets/evidence/v2/final'],cwd=ROOT,check=True)
    subprocess.run(['git','-c','user.name=github-actions[bot]','-c','user.email=41898282+github-actions[bot]@users.noreply.github.com','commit','-m','Preserve executed preview evidence [skip ci]'],cwd=ROOT,check=True)
    subprocess.run(['git','push','origin','HEAD:refs/heads/main'],cwd=ROOT,check=True)
    print('Preserved the exact release reports and captures in assets/evidence/v2/final')

if __name__=='__main__':main()
