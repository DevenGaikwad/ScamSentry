#!/bin/sh
# Keep collection within the emulator action's lifetime; preserve the test result.
set -u
cd "$(dirname "$0")/../android" || exit 1
status=0
./gradlew --no-daemon :app:connectedDebugAndroidTest --stacktrace || status=$?
mkdir -p ../release/device
timeout 20s adb pull /sdcard/Android/data/in.deven.scamsentry/files/screenshots ../release/device/ || true
timeout 20s adb logcat -d -t 1500 > ../release/device/logcat.txt || true
timeout 20s adb shell getprop > ../release/device/emulator_properties.txt || true
exit "$status"
