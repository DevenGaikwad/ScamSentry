"""Assemble observed CI results; fail on skipped, missing or failed release checks."""
from pathlib import Path
import hashlib
import json
import os
import re
import shutil
import subprocess
import xml.etree.ElementTree as ET

ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'release'

# Test parameter IDs may contain synthetic credential fixtures; never export them.
LABEL_CREDENTIAL_MARKER=re.compile(r'gh[pousr]_[A-Za-z0-9]{30,}|github_pat_[A-Za-z0-9_]{30,}|-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----')

def safe_label(value):
    return LABEL_CREDENTIAL_MARKER.sub('[redacted-fixture-marker]',value or '')

def junit(paths,minimum):
    paths=list(paths)
    cases=[case for path in paths for case in ET.parse(path).getroot().iter('testcase')]
    if len(cases)<minimum:raise ValueError(f'Missing executed JUnit cases: {len(cases)} < {minimum}')
    if any(c.find('failure') is not None or c.find('error') is not None or c.find('skipped') is not None for c in cases):raise ValueError('Failed or skipped JUnit case in release evidence')
    redacted=sum(bool(LABEL_CREDENTIAL_MARKER.search((c.get('classname') or '')+' '+(c.get('name') or ''))) for c in cases)
    return dict(tests=len(cases),failed=0,skipped=0,labels_redacted=redacted,cases=[dict(class_name=safe_label(c.get('classname')),name=safe_label(c.get('name'))) for c in cases])

def main():
    commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT,text=True).strip()
    needs=json.loads(os.environ['NEEDS_JSON'])
    if set(needs)!={'python','android','browser','windows','emulator'} or any(v['result']!='success' for v in needs.values()):raise ValueError('A required workflow job did not pass')
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
    native=junit((emulator/'android/app/build/outputs/androidTest-results/connected/debug').rglob('TEST-*.xml'),16)
    minified=junit((emulator/'android/benchmark/build/outputs/androidTest-results').rglob('TEST-*.xml'),2)
    required={'minifiedTextFlowShowsCredentialObservation','minifiedScreenshotReviewRunsBundledOcrAndUsesEditedText'}
    if not required.issubset({c['name'] for c in minified['cases']}):raise ValueError('A named external minified journey was not executed')
    minified_ocr=dict(tests=1,failed=0,skipped=0,cases=[c for c in minified['cases'] if c['name']=='minifiedScreenshotReviewRunsBundledOcrAndUsesEditedText'],scope='External driver selects the owned screenshot through the system picker, observes actual OCR, edits review text and verifies analysis')
    python=junit([OUT/'python-artifact/python-junit.xml'],77)
    extracted=junit([OUT/'python-artifact/archive-python-junit.xml'],77)
    windows=junit((OUT/'windows-artifact').rglob('TEST-*.xml'),9)
    quality_paths=list(emulator.rglob('ocr_quality.json'))
    if len(quality_paths)!=1:raise ValueError('Missing unambiguous OCR rendering report')
    quality=json.loads(quality_paths[0].read_text())
    if len(quality['cases'])!=6:raise ValueError('Incomplete OCR rendering slices')
    device=emulator/'release/device'
    radios={name:(device/file).read_text().strip() for name,file in [('wifi','offline_wifi.txt'),('airplane_mode','offline_airplane.txt'),('wifi_after','offline_wifi_after.txt'),('airplane_mode_after','offline_airplane_after.txt'),('legacy_mobile_data_setting','offline_mobile_data.txt')]}
    if any(radios[k]!='1' for k in ['airplane_mode','airplane_mode_after']):raise ValueError('Airplane mode was not verified')
    for key,filename in [('wifi_service','offline_wifi_status.txt'),('wifi_service_after','offline_wifi_status_after.txt')]:
        radios[key]=(device/filename).read_text().strip()
        if not any(line.strip().lower() in ['wifi is disabled','wi-fi is disabled'] for line in radios[key].splitlines()):raise ValueError('Effective Wi-Fi service state was not disabled')
    for filename in ['offline_connectivity.txt','offline_connectivity_after.txt']:
        if 'Active default network: none' not in (device/filename).read_text():raise ValueError('An active default network exists during the offline checks')
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
        if file.name not in ['01_analyze_home.png','02_credential_result.png','minified_credential_result.png','minified_ocr_review.png','minified_ocr_result.png']:continue
        target=OUT/('native_'+file.name)
        shutil.copy2(file,target)
        screenshots.append(dict(file=target.name,bytes=target.stat().st_size,sha256=hashlib.sha256(target.read_bytes()).hexdigest()))
    report=dict(schema_version=2,source_commit=commit,source_tree=subprocess.check_output(['git','rev-parse','HEAD^{tree}'],cwd=ROOT,text=True).strip(),workflow_url=f"https://github.com/{os.environ['GITHUB_REPOSITORY']}/actions/runs/{os.environ['GITHUB_RUN_ID']}",required_jobs=needs,python=python,extracted_source_python=extracted,browser_units=dict(tests=units['numTotalTests'],failed=0,skipped=0),browser_workflows=stats,native_debug=native,minified_functional=minified,distribution_checks=checks,packaged_model_sha256=expected,native_screenshots=screenshots,full_project_all_gates_passed=False,scope='GitHub-hosted Linux, desktop/mobile-view Chromium and API 35 x86_64 KVM functional checks. Minified benchmark variant uses debug signing and a profileable manifest; an external UI driver runs text and system-picker/OCR/review journeys without test-only application keep rules; no phone latency claim.',remaining=['Physical-phone performance and accessibility','Actual 16 KB runtime','First-use offline operation on a physical phone','Windows batch execution','Prospective independent model validation and known robustness trade-offs','Conditional free Vercel team access','Owner-managed production signing'])
    report.update(android_core_host=host_core,android_app_host=host_app,browser_worker_timings=timing,minified_ocr=minified_ocr)
    report.update(windows_wrapper_and_core=windows,emulator_radio_state=radios,ocr_rendering_quality=quality)
    report['remaining'].remove('Windows batch execution')
    (OUT/'RELEASE_VERIFICATION.json').write_text(json.dumps(report,indent=2)+'\n')
    print('Observed release evidence verified; native screenshots:',len(screenshots))
    print('RELEASE_VERIFICATION_JSON='+json.dumps(report,separators=(',',':')))

if __name__=='__main__':main()
