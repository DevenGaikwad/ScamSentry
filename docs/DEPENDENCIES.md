# Dependency and build policy

Exact direct versions live in the Android catalog and `ml/requirements-lock.txt`. No dynamic selector is used. Gradle wrapper download is SHA-256 pinned. Official Google/Maven Central repositories resolve Android dependencies. The source includes an immutable checkout action reference and explicit release tags for other actions; CI has read-only default token permissions. No CI publication credential or untrusted-fork secret is used.

The selected Compose BOM 2026.09.00 resolves Compose 1.12.1, which requires compile SDK 37 and AGP ≥9.1. The build was aligned to AGP 9.1.1, Gradle 9.3.1, Kotlin/Compose compiler 2.4.10 and KSP 2.3.10 with built-in Android Kotlin. Target SDK remains 36 and minimum SDK 26. `platforms;android-37.0` and build-tools 36.0.0 are the corresponding SDK packages. Room 2.8.3, DataStore 1.1.7, lifecycle 2.9.4, Navigation 2.9.5 and bundled Latin OCR 16.0.1 are pinned.

Updates require official release-note/compatibility review, source and licence review, relevant tests, bundle parity, debug/minified builds and a fresh device check when runtime behavior changes. Do not upgrade model preprocessing silently: calibration, policy and golden evidence depend on the exact feature contract.

The Python audit initially identified a pytest local temporary-directory vulnerability in 8.4.2. The test-only dependency was updated to 9.0.3; the follow-up audit result is retained in `assets/evidence/python_dependency_audit.json`. This change does not alter fitted model parameters. Training-environment evidence preserves the original versions rather than rewriting history. A clean Python scan does not cover Android/JDK/toolchain vulnerabilities or prove absence of unknown issues.

Android dependency coordinates and final manifest inspection are recorded separately. Full Android vulnerability tooling and supply-chain certification are not claimed. ML Kit may include data-transport operational diagnostics; app Internet permission is removed, and SDK behavior remains subject to device review. No remote inference, font, model or reputation request is part of the application core.
