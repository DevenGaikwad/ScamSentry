"""Validate a repository or extracted archive without silently skipped stages."""
import argparse
import json
from pathlib import Path
import sys
# Inspection must not manufacture a cache inside an extracted distribution.
sys.dont_write_bytecode=True
ROOT=Path(__file__).resolve().parents[1]
sys.path.insert(0,str(ROOT/'ml/src'))
from scamsentry_ml.release_validation import validate_release

def main():
    p=argparse.ArgumentParser()
    p.add_argument('--root',type=Path,default=ROOT)
    p.add_argument('--mode',choices=['repository','archive'],default='repository')
    p.add_argument('--format',choices=['text','json'],default='text')
    p.add_argument('--report',type=Path)
    a=p.parse_args(); result=validate_release(a.root,a.mode)
    if a.report: a.report.write_text(json.dumps(result,indent=2)+'\n')
    if a.format=='json': print(json.dumps(result,indent=2))
    else:
        for s in result['stages']:
            print(f"{s['status']}: {s['stage']} ({s['inspected']} inspected) {s['details']}")
            for e in s['errors']: print(f'  {e}')
    raise SystemExit(0 if result['status']=='PASS' else 1)
if __name__=='__main__': main()
