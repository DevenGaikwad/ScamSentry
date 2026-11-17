"""Assemble observed CI results; fail on skipped, missing or failed release checks."""
from pathlib import Path
import hashlib
import json
import os
import shutil
import subprocess
import xml.etree.ElementTree as ET

ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'release'

def junit(paths,minimum):
    paths=list(paths)
    cases=[case for path in paths for case in ET.parse(path).getroot().iter('testcase')]
    if len(cases)<minimum:raise ValueError(f'Missing executed JUnit cases: {len(cases)} < {minimum}')
    if any(c.find('failure') is not None or c.find('error') is not None or c.find('skipped') is not None for c in cases):raise ValueError('Failed or skipped JUnit case in release evidence')
    return dict(tests=len(cases),failed=0,skipped=0,cases=[dict(class_name=c.get('classname'),name=c.get('name')) for c in cases])

def main():
    commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT,text=True).strip()
    needs=json.loads(os.environ['NEEDS_JSON'])
    if set(needs)!={'python','android','browser','emulator'} or any(v['result']!='success' for v in needs.values()):raise ValueError('A required workflow job did not pass')
    browser=OUT/'browser-artifact'
    stats=json.loads((browser/'web/test-results/results.json').read_text())['stats']
    if stats['expected']!=8 or any(stats[k] for k in ['skipped','unexpected','flaky']):raise ValueError('Browser workflow evidence incomplete')
    units=json.loads((browser/'web/unit-results/unit.json').read_text())
    if units['numTotalTests']<63 or units['numFailedTests'] or units['numPendingTests']:raise ValueError('Browser unit evidence incomplete')
    timing=json.loads((browser/'assets/evidence/v2/web_desktop-chromium_timings.json').read_text())
    if timing['source_commit']!=commit:raise ValueError('Browser evidence belongs to another source commit')
    android=OUT/'android-artifact'
    host_core=junit((android/'android/core-analysis/build/test-results/test').rglob('TEST-*.xml'),9)
    host_app=junit((android/'android/app/build/test-results/testDebugUnitTest').rglob('TEST-*.xml'),2)
    emulator=OUT/'emulator-artifact'
    native=junit((emulator/'android/app/build/outputs/androidTest-results/connected/debug').rglob('TEST-*.xml'),12)
    minified_ocr=junit((emulator/'android/app/build/outputs/androidTest-results/connected/benchmark').rglob('TEST-*.xml'),1)
    minified=junit((emulator/'android/benchmark/build/outputs/androidTest-results').rglob('TEST-*.xml'),1)
    python=junit([OUT/'python-artifact/python-junit.xml'],75)
    extracted=junit([OUT/'python-artifact/archive-python-junit.xml'],75)
    checks={}
    for name in ['repository_validation.json','archive_validation.json']:
        report=json.loads((OUT/'python-artifact'/name).read_text())
        if report['status']!='PASS':raise ValueError('Distribution validation failed')
        checks[name]=report
    expected=json.loads((ROOT/'ml/artifacts/bundle/manifest.json').read_text())['model_sha256']
    packages=json.loads((OUT/'apk_inspection.json').read_text())
    if len(packages)!=3 or any(p['model_sha256']!=expected for p in packages):raise ValueError('APK model evidence mismatch')
    screenshots=[]
    for file in sorted(emulator.rglob('*.png')):
        if file.name not in ['01_analyze_home.png','02_credential_result.png','minified_credential_result.png']:continue
        target=OUT/('native_'+file.name)
        shutil.copy2(file,target)
        screenshots.append(dict(file=target.name,bytes=target.stat().st_size,sha256=hashlib.sha256(target.read_bytes()).hexdigest()))
    report=dict(schema_version=2,source_commit=commit,source_tree=subprocess.check_output(['git','rev-parse','HEAD^{tree}'],cwd=ROOT,text=True).strip(),workflow_url=f"https://github.com/{os.environ['GITHUB_REPOSITORY']}/actions/runs/{os.environ['GITHUB_RUN_ID']}",required_jobs=needs,python=python,extracted_source_python=extracted,browser_units=dict(tests=units['numTotalTests'],failed=0,skipped=0),browser_workflows=stats,native_debug=native,minified_functional=minified,distribution_checks=checks,packaged_model_sha256=expected,native_screenshots=screenshots,full_project_all_gates_passed=False,scope='GitHub-hosted Linux, desktop/mobile-view Chromium and API 35 x86_64 KVM functional checks. Minified benchmark variant uses debug signing and profileable manifest; no phone latency claim.',remaining=['Physical-phone performance and accessibility','Actual 16 KB runtime','First-use offline operation on a physical phone','Windows batch execution','Prospective independent model validation and known robustness trade-offs','Conditional free Vercel team access','Owner-managed production signing'])
    report.update(android_core_host=host_core,android_app_host=host_app,browser_worker_timings=timing,minified_ocr=minified_ocr)
    (OUT/'RELEASE_VERIFICATION.json').write_text(json.dumps(report,indent=2)+'\n')
    print('Observed release evidence verified; native screenshots:',len(screenshots))

if __name__=='__main__':main()
