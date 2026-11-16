"""Read-only artifact, privacy and consistency validation; never rewrites evidence."""
from pathlib import Path
import hashlib
import json
import re
import subprocess
import sys

root=Path(__file__).resolve().parents[1]
sys.path.insert(0,str(root/'ml/src'))
from scamsentry_ml.bundle import validate
from scamsentry_ml.sources import validate_sources
from scamsentry_ml.splits import verify

errors=[]
def check(condition,message):
    if not condition: errors.append(message)

required=['README.md','LICENSE','THIRD_PARTY_NOTICES.md','docs/PROJECT_STATE.md','docs/SETUP_WINDOWS.md','docs/PRIVACY.md','docs/MODEL_CARD.md','docs/EVALUATION_RESULTS.md','docs/THREAT_MODEL.md','docs/GATE_REPORT.json','android/gradlew','android/gradlew.bat','android/gradle/wrapper/gradle-wrapper.jar']
for n in required: check((root/n).is_file(),f'Missing required artifact: {n}')
try:
    validate_sources();validate(root/'ml/artifacts/bundle');verify(root/'ml/artifacts/splits.json')
except (ValueError,KeyError,OSError) as e: errors.append(str(e))
bundled=root/'android/app/src/main/assets/model/model.json'
check(bundled.read_bytes()==(root/'ml/artifacts/bundle/model.json').read_bytes(),'Android/Python model mismatch')
report=json.loads((root/'ml/artifacts/runs/baseline_char_lr_001/final_test.json').read_text())
check(hashlib.sha256(bundled.read_bytes()).hexdigest()==report['model_sha256'],'Metrics do not match bundled model')
try:
    files=subprocess.check_output(['git','ls-files','--cached','--others','--exclude-standard'],cwd=root,text=True).splitlines()
except subprocess.CalledProcessError: files=[]
for n in files:
    if re.search(r'(?:^|/)(?:data/raw|data/processed|\.venv|local\.properties|signing\.properties)(?:/|$)|\.(?:jks|keystore|joblib)$',n): errors.append(f'Forbidden distribution candidate: {n}')
    p=root/n
    if p.is_file() and p.suffix in ['.py','.kt','.kts','.json','.md','.yaml','.yml']:
        text=p.read_text(errors='replace')
        check(not re.search(r'gh[pousr]_[A-Za-z0-9]{30,}|-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----',text),f'Possible secret in {n}')
check(not any('READ_SMS' in p.read_text() for p in (root/'android/app/src/main').rglob('AndroidManifest.xml')),'Inbox permission present')
if errors:
    print('\n'.join(errors));raise SystemExit(1)
print('Project contracts, bundle, metrics, partition isolation and distribution candidates validated')
