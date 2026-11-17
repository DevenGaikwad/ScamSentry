#!/bin/sh
# Keep collection within the emulator action's lifetime; preserve the test result.
set -u
cd "$(dirname "$0")/../android" || exit 1
status=0
mkdir -p ../release/device
timeout 20s adb shell cmd connectivity airplane-mode enable || status=$?
timeout 20s adb shell svc wifi disable
timeout 20s adb shell svc data disable
timeout 20s adb shell settings get global airplane_mode_on > ../release/device/offline_airplane.txt
timeout 20s adb shell settings get global wifi_on > ../release/device/offline_wifi.txt
# The legacy global setting can differ from subscription-specific effective state.
timeout 20s adb shell settings get global mobile_data > ../release/device/offline_mobile_data.txt
timeout 20s adb shell dumpsys connectivity > ../release/device/offline_connectivity.txt
./gradlew --no-daemon :app:connectedDebugAndroidTest --stacktrace || status=$?
timeout 20s adb pull /sdcard/Android/data/in.deven.scamsentry/files/screenshots ../release/device/ || true
timeout 20s adb logcat -d -t 1500 > ../release/device/debug_logcat.txt || true
./gradlew --no-daemon -Pscamsentry.instrumentedBuild=benchmark :app:connectedBenchmarkAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=in.deven.scamsentry.OcrIntegrationTest --stacktrace || status=$?
timeout 20s adb logcat -b crash -d > ../release/device/minified_ocr_crash.txt || true
timeout 20s adb logcat -d -t 8000 > ../release/device/minified_ocr_logcat.txt || true
./gradlew --no-daemon :benchmark:connectedBenchmarkAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=in.deven.scamsentry.benchmark.ReleaseLikeSmokeTest --stacktrace || status=$?
timeout 20s adb pull /sdcard/Android/data/in.deven.scamsentry.benchmark/files/screenshots ../release/device/minified-screenshots || true
timeout 20s adb logcat -d -t 1500 > ../release/device/logcat.txt || true
timeout 20s adb shell getprop > ../release/device/emulator_properties.txt || true
timeout 20s adb shell settings get global airplane_mode_on > ../release/device/offline_airplane_after.txt
timeout 20s adb shell settings get global wifi_on > ../release/device/offline_wifi_after.txt
timeout 20s adb shell dumpsys connectivity > ../release/device/offline_connectivity_after.txt
# AGP can return zero even when the runner crashes before discovering a case.
# Read the executed XML instead of trusting only the Gradle exit code.
python3 - <<'PY' || status=$?
import sys
from pathlib import Path
sys.path.insert(0,'../scripts')
from create_release_evidence import junit
root=Path('.')
for name,path,minimum in [('debug','app/build/outputs/androidTest-results/connected/debug',16),('minified OCR','app/build/outputs/androidTest-results/connected/benchmark',1),('minified UI','benchmark/build/outputs/androidTest-results',1)]:
    report=junit((root/path).rglob('TEST-*.xml'),minimum)
    print(name,report['tests'],'executed; zero failures/skips')
PY
exit "$status"
