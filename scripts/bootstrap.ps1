$ErrorActionPreference = 'Stop'
Write-Host 'Checking ScamSentry prerequisites...'
if (-not (Get-Command java -ErrorAction SilentlyContinue)) { throw 'Install a supported JDK 21 or configure Android Studio Gradle JDK.' }
if (-not (Get-Command python -ErrorAction SilentlyContinue)) { throw 'Install Python 3.12 to reproduce ML. The Android bundle is already included.' }
java -version
python --version
Write-Host 'Open android/ in Android Studio. Install SDK platform 37 and build-tools 36.0.0. See docs/SETUP_WINDOWS.md.'
Write-Host 'No system settings, cloud accounts or unrelated tools were changed.'
