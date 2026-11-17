# Windows 11 setup

Open `android` directly in Android Studio. Existing Python/Node/MySQL/Vivado installations used for other projects are not Android prerequisites. Node, MySQL and a server are not needed.

| Tool | Required configuration | Official reference |
|---|---|---|
| Android Studio | Stable installation with SDK Manager | https://developer.android.com/studio |
| JDK | JDK 21 for the pinned Gradle 9.6.0 build | https://docs.gradle.org/9.6.0/userguide/compatibility.html |
| Android SDK | Platform 37.0, build-tools 36.0.0, platform-tools | https://developer.android.com/tools/sdkmanager |
| Gradle | Checked-in wrapper 9.6.0; no global install needed | https://docs.gradle.org/9.6.0/userguide/gradle_wrapper.html |
| Python | 3.12.x, project-local virtual environment | https://www.python.org/downloads/windows/ |
| Git | Optional for source history and future publication | https://git-scm.com/downloads/win |

The exact Android dependencies are in `android/gradle/libs.versions.toml`: AGP 9.5.1, Kotlin/Compose compiler 2.4.21 and the pinned Compose BOM. Version-catalog changes require build and parity checks. The original 0.1.0 dependency group remains in the preserved baseline branch. No JDK, SDK or dependency cache is bundled.

PowerShell from the extracted project root:

```powershell
java -version
python --version
Set-Location .\android
.\gradlew.bat :core-analysis:test :app:testDebugUnitTest :app:lintDebug :app:assembleDebug
```

Android Studio can create your local SDK path configuration. If the command line cannot find the SDK, set a project-local `local.properties` with your actual path using forward slashes, e.g. `sdk.dir=C:/Users/YOUR_USER/AppData/Local/Android/Sdk`. This file is deliberately not distributed. Resolve the actual path rather than copying the example unchanged.

The bundled model allows building/running the app without downloading the training dataset. To reproduce ML, from the project root:

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install -r .\ml\requirements-lock.txt
python -m pip install --no-deps -e .\ml
Set-Location .\ml
python -m scamsentry_ml.sources validate
python -m scamsentry_ml.bundle validate --bundle artifacts/bundle
python -m pytest
```

For full data reproduction, download the registered primary archive using `python scripts/fetch_source.py` from project root. The command verifies archive and CSV digests and never sends correspondence. Then use the explicit commands in `ml/README.md`. To retrain, make a new run config and ID; the distributed run is frozen and intentionally cannot be overwritten. Final-test access is guarded. A fresh training experiment is not a new independent holdout merely because it uses a different seed.

Connect your own Android phone using a known USB cable, enable Developer options/USB debugging, verify the attached computer before accepting its debugging authorization, then run `adb devices`. Select the phone in Android Studio and run the app, or use `adb install -r path-to-the-debug-apk`. Windows drivers may be needed for the phone manufacturer. An emulator is useful for layout checks; physical hardware is needed for runtime evidence. Freshly install, disable connectivity and follow the device checklist for text and bundled OCR.

The app can run offline after installation; development setup normally downloads Gradle, SDK and Python dependencies. Prepare lawful caches before an offline classroom build. No offline-from-a-fresh-machine build claim is made.

Common symptoms: missing SDK → set the local SDK path; incompatible Java → select the documented JDK; share target missing → inspect the installed app and text/plain MIME; OCR empty → choose a readable Latin-script screenshot and correct text; model unavailable → verify APK checksum/reinstall, never trust a fallback green state. Do not expose personal screenshots or signing secrets in bug reports.

## V0.2.0 upgrade notes

Clone `https://github.com/DevenGaikwad/ScamSentry` into a short ASCII path such as `C:\Projects\ScamSentry`. Keep JDK 21, SDK platform `android-37.0`, Build Tools 36.0.0, compile/target/min 37/36/26. The project now pins Gradle 9.6.0 / AGP 9.5.1; use the regenerated `android\gradlew.bat` rather than a globally installed older Gradle. Review `DEPENDENCIES.md` for the coherent stable group. Existing E: tools or unrelated project installations do not need moving.

From PowerShell at the repository root:

```powershell
python -m pip install -r ml/requirements-lock.txt
python -m pip install --no-deps -e ml
python -m pytest ml/tests -q
python scripts/validate_project.py --mode repository
Set-Location android
.\gradlew.bat :core-analysis:test :app:testDebugUnitTest :app:lintDebug
.\gradlew.bat :app:assembleDebug :app:assembleDebugAndroidTest
.\gradlew.bat :app:assembleRelease
```

Separate release assembly bounds compiler/lint/R8 memory residency. For a source ZIP, run the validator with `--mode archive` before building. No Git initialization or private joblib file is needed to validate source and synthetic export arithmetic.

For the browser, use Node 24, enter `web`, then run `npm ci`, `npm run build`, `npm run preview` and open the displayed localhost URL. Text/URL analysis is functional after public model download. Browser OCR/PWA are not included. Installable release APKs use explicit disposable TEST signatures and versionCode 2.

The actual Windows-hosted job in [run 43998404388](https://github.com/DevenGaikwad/ScamSentry/actions/runs/43998404388) passed `gradlew.bat --no-daemon :core-analysis:test` on source `8c12eab7d2f77a1ef2946b79febab22f721f0888`. This verifies the batch launcher and core suite on Windows; full Windows APK assembly and an owner's local SDK setup are separate. JSON model assets use LF on both platforms to preserve their frozen digest.
