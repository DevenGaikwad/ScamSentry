import subprocess
import pytest
from scamsentry_ml.release_validation import scan_release_tree
from scamsentry_ml.release_validation import validate_release
import hashlib
import json
from pathlib import Path
import shutil
import sys

def test_archive_scans_without_git(tmp_path):
    (tmp_path/'README.md').write_text('Clean archive')
    names,stages=scan_release_tree(tmp_path,'archive')
    assert names==['README.md'] and stages[0]['status']=='NOT_APPLICABLE'
    assert stages[1]['status']=='PASS' and stages[1]['inspected']==1

def test_repository_without_git_fails(tmp_path):
    _,stages=scan_release_tree(tmp_path,'repository')
    assert stages[0]['status']=='FAIL' and 'archive' in stages[0]['details']

@pytest.mark.parametrize('name,body',[
    ('ml/data/raw/messages.csv','text,label'),('signing.jks','opaque'),
    ('docs/notes.md','-----BEGIN '+'PRIVATE KEY-----'),('ml/src/notes.py','ghp_'+'a'*36),
])
def test_reject_distribution_secrets(tmp_path,name,body):
    p=tmp_path/name; p.parent.mkdir(parents=True,exist_ok=True); p.write_text(body)
    _,stages=scan_release_tree(tmp_path,'archive')
    assert stages[1]['status']=='FAIL'
    assert not any(body in e for e in stages[1]['errors'])
    marker='Credential marker' if name.startswith(('docs/','ml/src/')) else 'Forbidden distribution candidate'
    assert any(marker in e for e in stages[1]['errors'])

def test_archive_symlink_rejected(tmp_path):
    (tmp_path/'escape').symlink_to('/etc/passwd')
    assert scan_release_tree(tmp_path,'archive')[1][1]['status']=='FAIL'

def test_untracked_candidates_are_scanned(tmp_path):
    subprocess.run(['git','init','-q'],cwd=tmp_path,check=True)
    (tmp_path/'README.md').write_text('review me')
    names,stages=scan_release_tree(tmp_path,'repository')
    assert names==['README.md'] and stages[1]['inspected']==1


def test_unexpected_archive_path_rejected(tmp_path):
    (tmp_path/'unexpected_payload.txt').write_text('harmless unexpected fixture')
    _,stages=scan_release_tree(tmp_path,'archive')
    assert stages[1]['status']=='FAIL' and any('Unexpected distribution path' in e for e in stages[1]['errors'])


def test_archive_rejects_cache_directory_without_unbounded_descent(tmp_path):
    (tmp_path/'README.md').write_text('clean source')
    (tmp_path/'node_modules').mkdir()
    (tmp_path/'node_modules/fixture.js').write_text('harmless build fixture')
    _,stages=scan_release_tree(tmp_path,'archive')
    assert stages[1]['status']=='FAIL' and any('Build cache in archive' in e for e in stages[1]['errors'])

def test_packaged_inventory_checks_actual_bytes(tmp_path):
    install=tmp_path/'install';install.mkdir()
    apk=install/'fictional.apk';apk.write_bytes(b'fictional package checksum fixture')
    item=dict(file=apk.name,bytes=apk.stat().st_size,sha256=hashlib.sha256(apk.read_bytes()).hexdigest())
    (tmp_path/'ARTIFACT_INVENTORY.json').write_text(json.dumps(dict(artifacts=[item])))
    def packaging():return next(s for s in validate_release(tmp_path,'archive')['stages'] if s['stage']=='release_packaging')
    assert packaging()['status']=='PASS'
    apk.write_bytes(b'changed package')
    assert packaging()['status']=='FAIL'

@pytest.mark.parametrize('name',['../outside.apk',r'..\outside.apk','/tmp/outside.apk'])
def test_inventory_rejects_path_escape_before_reading(tmp_path,name):
    (tmp_path/'ARTIFACT_INVENTORY.json').write_text(json.dumps(dict(artifacts=[dict(file=name)])))
    result=next(s for s in validate_release(tmp_path,'archive')['stages'] if s['stage']=='release_packaging')
    assert result['status']=='FAIL' and 'Unsafe APK inventory path' in result['errors'][0]

def test_extracted_cli_does_not_create_rejected_bytecode_cache(tmp_path):
    root=Path(__file__).resolve().parents[2]
    (tmp_path/'scripts').mkdir()
    shutil.copy2(root/'scripts/validate_project.py',tmp_path/'scripts/validate_project.py')
    shutil.copytree(root/'ml/src/scamsentry_ml',tmp_path/'ml/src/scamsentry_ml',ignore=shutil.ignore_patterns('__pycache__'))
    run=subprocess.run([sys.executable,str(tmp_path/'scripts/validate_project.py'),'--mode','archive','--format','json'],capture_output=True,text=True)
    result=json.loads(run.stdout)
    privacy=next(s for s in result['stages'] if s['stage']=='filesystem_privacy')
    assert privacy['status']=='PASS'
    assert not list(tmp_path.rglob('__pycache__'))
