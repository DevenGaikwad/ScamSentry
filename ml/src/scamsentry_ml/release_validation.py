"""Read-only release checks with explicit repository/archive scanning."""
from pathlib import Path
import hashlib
import json
import os
import re
import subprocess
from .bundle import validate
from .splits import verify

CACHE = {'.git','.gradle','.kotlin','build','node_modules','.venv','__pycache__','.pytest_cache','dist','.vite','.vercel','test-results','playwright-report'}
FORBIDDEN = re.compile(r'(?:^|/)(?:data/(?:raw|processed)|artifacts/private|\.env(?:\.[^/]*)?|local\.properties|signing\.properties|id_rsa|id_ed25519)(?:/|$)|\.(?:jks|keystore|joblib|pickle|pkl|p12|pfx|pem)$')
TOKEN = re.compile(r'gh[pousr]_[A-Za-z0-9]{30,}|github_pat_[A-Za-z0-9_]{30,}|-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----')
ALLOWED_DIRS={'android','ml','scripts','docs','assets','web','release','install','.github'}
ALLOWED_TOP={'README.md','LICENSE','THIRD_PARTY_NOTICES.md','RELEASE_NOTES.md','ARTIFACT_INVENTORY.json','SHA256SUMS.txt','.gitignore','.gitattributes','SECURITY.md','CONTRIBUTING.md','signing_report.json','archive_validation.json'}
TEXT = {'.py','.kt','.kts','.json','.md','.yml','.yaml','.toml','.txt','.xml','.csv','.ts','.tsx','.js','.mjs','.css','.html','.ps1','.bat','.properties','.svg','.sh','.lock'}

def stage(name,status,count=0,details='',errors=None):
    return dict(stage=name,status=status,inspected=count,details=details,errors=errors or [])

def candidate_paths(root,mode):
    root=Path(root).resolve()
    if mode=='repository':
        try:
            top=subprocess.check_output(['git','rev-parse','--show-toplevel'],cwd=root,stderr=subprocess.DEVNULL,text=True).strip()
            if Path(top).resolve()!=root: raise ValueError('Root is not the repository root')
            names=subprocess.check_output(['git','ls-files','--cached','--others','--exclude-standard','-z'],cwd=root).decode().split('\0')
            names=sorted(set(filter(None,names)))
            if not names: raise ValueError('Git returned no candidates')
            return names,stage('git_listing','PASS',len(names),'Tracked and unignored candidates')
        except (OSError,ValueError,subprocess.CalledProcessError) as e:
            return [],stage('git_listing','FAIL',details='Use --mode archive for an extracted source archive',errors=[str(e)])
    names,errors=[],[]
    for folder,dirs,files in os.walk(root,followlinks=False):
        for d in list(dirs):
            p=Path(folder)/d
            if p.is_symlink():
                errors.append(f'Symlink directory: {p.relative_to(root)}'); dirs.remove(d)
            elif d in CACHE: dirs.remove(d)
        names.extend((Path(folder)/f).relative_to(root).as_posix() for f in files)
    return sorted(names),stage('git_listing','NOT_APPLICABLE',len(names),'Archive filesystem walk',errors)

def scan_release_tree(root,mode):
    root=Path(root).resolve(); names,listing=candidate_paths(root,mode)
    errors=list(listing['errors']); count=0
    for name in names:
        p=root/name
        if p.is_symlink() or not p.resolve().is_relative_to(root) or not p.is_file():
            errors.append(f'Unsafe or missing candidate: {name}'); continue
        count+=1
        if any(part in CACHE for part in Path(name).parts): errors.append(f'Build cache in distribution: {name}')
        first=Path(name).parts[0]
        if first not in ALLOWED_DIRS and name not in ALLOWED_TOP: errors.append(f'Unexpected distribution path: {name}')
        if FORBIDDEN.search(name): errors.append(f'Forbidden distribution candidate: {name}')
        if p.suffix.lower()=='.csv' and not name.startswith(('assets/demo/','ml/tests/fixtures/')) and p.name!='faculty_aggregate_summary.csv':
            errors.append(f'Unreviewed CSV: {name}')
        if p.suffix.lower() in TEXT and p.stat().st_size<=16_000_000 and TOKEN.search(p.read_text(errors='replace')):
            errors.append(f'Credential marker in {name}')
    if not count: errors.append('No files were inspected')
    return names,[listing,stage('filesystem_privacy','FAIL' if errors else 'PASS',count,errors=errors)]

def validate_release(root,mode):
    root=Path(root).resolve(); names,stages=scan_release_tree(root,mode)
    checks={'source_manifest':lambda: source_contract(root),'model_integrity':lambda: model_contract(root),'partition_isolation':lambda: verify(root/'ml/artifacts/splits.json'),'required_files':lambda: required_contract(root)}
    for name,check in checks.items():
        try: check(); stages.append(stage(name,'PASS',1))
        except (OSError,ValueError,KeyError,IndexError,TypeError,AssertionError) as e:
            stages.append(stage(name,'FAIL',errors=[str(e)]))
    inventory=root/'ARTIFACT_INVENTORY.json'
    if inventory.exists():
        try:
            items=json.loads(inventory.read_text())['artifacts']
            if not items: raise ValueError('Artifact inventory is empty')
            for item in items:
                p=root/'install'/item['file']
                if not p.is_file() or p.stat().st_size!=item['bytes'] or hashlib.sha256(p.read_bytes()).hexdigest()!=item['sha256']: raise ValueError('Release artifact checksum mismatch')
            stages.append(stage('release_packaging','PASS',len(items)))
        except (OSError,ValueError,KeyError,TypeError) as e: stages.append(stage('release_packaging','FAIL',errors=[str(e)]))
    else: stages.append(stage('release_packaging','NOT_APPLICABLE',details='Source tree contains no packaged APK inventory'))
    return dict(schema_version=2,mode=mode,status='FAIL' if any(s['status']=='FAIL' for s in stages) else 'PASS',stages=stages,candidate_count=len(names))

def source_contract(root):
    entries=json.loads((root/'ml/manifests/sources.json').read_text())
    if not entries: raise ValueError('Source registry empty')
    for entry in entries:
        for field in ['source_id','landing_page','published_license','eligibility_status','attribution']:
            if not entry.get(field): raise ValueError(f'Missing source field: {field}')
        if entry['eligibility_status']=='eligible':
            for field in ['download_sha256','file_review','checked_at']:
                if not entry.get(field): raise ValueError(f'Eligible source missing {field}')

def model_contract(root):
    p=root/'ml/artifacts/bundle/model.json'; validate(p.parent)
    if p.read_bytes()!=(root/'android/app/src/main/assets/model/model.json').read_bytes(): raise ValueError('Android/Python model mismatch')
    report=json.loads((root/'ml/artifacts/runs/baseline_char_lr_001/final_test.json').read_text())
    if hashlib.sha256(p.read_bytes()).hexdigest()!=report['model_sha256']: raise ValueError('Metrics/model mismatch')

def required_contract(root):
    for name in ['README.md','LICENSE','THIRD_PARTY_NOTICES.md','docs/PROJECT_STATE.md','docs/GATE_REPORT.json','android/gradlew','android/gradlew.bat','android/gradle/wrapper/gradle-wrapper.jar']:
        if not (root/name).is_file(): raise ValueError(f'Missing required artifact: {name}')
    if not os.access(root/'android/gradlew',os.X_OK): raise ValueError('Gradle wrapper is not executable')
    if any('READ_SMS' in p.read_text() for p in (root/'android/app/src/main').rglob('AndroidManifest.xml')): raise ValueError('Inbox permission present')
