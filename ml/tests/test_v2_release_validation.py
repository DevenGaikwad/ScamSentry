import subprocess
import pytest
from scamsentry_ml.release_validation import scan_release_tree

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
    ('notes.md','-----BEGIN '+'PRIVATE KEY-----'),('notes.py','ghp_'+'a'*36),
])
def test_reject_distribution_secrets(tmp_path,name,body):
    p=tmp_path/name; p.parent.mkdir(parents=True,exist_ok=True); p.write_text(body)
    _,stages=scan_release_tree(tmp_path,'archive')
    assert stages[1]['status']=='FAIL'
    assert not any(body in e for e in stages[1]['errors'])

def test_archive_symlink_rejected(tmp_path):
    (tmp_path/'escape').symlink_to('/etc/passwd')
    assert scan_release_tree(tmp_path,'archive')[1][1]['status']=='FAIL'

def test_untracked_candidates_are_scanned(tmp_path):
    subprocess.run(['git','init','-q'],cwd=tmp_path,check=True)
    (tmp_path/'README.md').write_text('review me')
    names,stages=scan_release_tree(tmp_path,'repository')
    assert names==['README.md'] and stages[1]['inspected']==1
