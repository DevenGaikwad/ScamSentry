# Historical 0.2.0 research-preview handoff

[Live browser app](https://scamsentry-bay.vercel.app/) · [Public repository](https://github.com/DevenGaikwad/ScamSentry) · [v0.2.0-preview downloads](https://github.com/DevenGaikwad/ScamSentry/releases/tag/v0.2.0-preview) · [Executed workflow](https://github.com/DevenGaikwad/ScamSentry/actions/runs/44012744040)

Release source: `bc227ba189796c1167c3cd21c3a7ec973156ccfd`. The prerelease tag, source ZIP, APK inventory and executed verification report refer to this immutable commit. A later bot commit preserves reports/screenshots; later documentation edits do not claim to have produced the immutable APKs. [Source-to-release provenance](../assets/evidence/v2/final/SNAPSHOT_PROVENANCE.json).

## Installable artifacts

| Download | ABI(s) | Exact bytes | SHA-256 |
|---|---|---:|---|
| [ScamSentry_0.2.0-preview_arm64-v8a_TEST-SIGNED.apk](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.2.0-preview/ScamSentry_0.2.0-preview_arm64-v8a_TEST-SIGNED.apk) | arm64-v8a | 15,747,556 | `e9d49467d868f93fe10e52e2b3fa55d7b45476655a3cc8e5df40cd86ebc8dc6e` |
| [ScamSentry_0.2.0-preview_x86_64_TEST-SIGNED.apk](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.2.0-preview/ScamSentry_0.2.0-preview_x86_64_TEST-SIGNED.apk) | x86_64 | 16,308,699 | `e82d0ea9b3fac83359b3bea1101c3b6737a17f92b883d105d8a7bc5a5bba4926` |
| [ScamSentry_0.2.0-preview_universal_TEST-SIGNED.apk](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.2.0-preview/ScamSentry_0.2.0-preview_universal_TEST-SIGNED.apk) | arm64-v8a, armeabi-v7a, x86, x86_64 | 45,833,462 | `7531923ec1fa01ac9840d79871573424454e9501b0220506cb7b0bf07f06729c` |

All packages: `in.deven.scamsentry`, versionName `0.2.0-preview`, versionCode `2`, minSdk `26` (Android 8), targetSdk `36`. Choose only an ABI your device supports; arm64 is the normal compatible-phone download. Universal is a larger fallback containing its inspected ABI set. No full-device compatibility claim is made from a build alone.

The APKs are installable **TEST-SIGNED research previews** using a disposable certificate. They are not an owner-controlled production-signing identity. A previous preview with another certificate may require uninstalling before installation; preserve any wanted local summary history first. APK digest checks establish byte equality to the published inventory; use the trusted repository/release source for the manifest.

Use [DOWNLOAD_SHA256SUMS.txt](../assets/evidence/v2/final/DOWNLOAD_SHA256SUMS.txt) for APKs, source/full ZIPs, screenshots, the browser recording and machine-readable reports. [ARTIFACT_INVENTORY.json](../assets/evidence/v2/final/ARTIFACT_INVENTORY.json) binds the three APK digests to source/model. [APK inspection](../assets/evidence/v2/final/apk_inspection.json) records actual package, signatures and alignment. Checksums do not make separate preview builds byte-identical.

The exact disposable certificate SHA-256 is `7b009915cc741cd50b697e8ef655df010b82794c9f1f1f30821ec7b2e49e0a19`; v2 and v3 APK signature verification passed for all three files.

On Windows, compute `Get-FileHash .\\<downloaded-file.apk> -Algorithm SHA256` and compare the entire result to the matching filename in the checksum inventory. After verifying a compatible APK, install through your normal owner-controlled Android file transfer/install flow or `adb install -r <downloaded-file.apk>`. The research preview requires no account, server key or inbox permission.

## Exact-source verification

| Executed gate | Result |
|---|---|
| Python checkout / source extraction | 77 / 77 passed, zero skipped |
| Browser units / production workflows | 63 / 8 passed, zero skips/flakes |
| Linux Kotlin core / app unit tests | 9 / 2 passed |
| Windows batch/core | 9 passed |
| API 35 debug / external minified journeys | 16 / 2 passed, zero failures/skips |
| Build, signature, packaging | Required jobs and publication passed; source/full ZIP filesystem scan PASS |


The original [RELEASE_VERIFICATION.json](../assets/evidence/v2/final/RELEASE_VERIFICATION.json) records case names, source/tree/model identities, radio observations, browser timing samples and six OCR rendering results. [Full package validation](../assets/evidence/v2/final/full_package_validation.json) retains the strict real-filesystem privacy and inventory checks. No failed or skipped required runtime case was accepted to publish.

API 35 KVM evidence covers fresh minified text analysis and the actual Android photo picker → bundled Latin OCR → editable review → explicit correction → result. Airplane mode was enabled, the actual Wi-Fi service was disabled and there was no active default network before/after. The external UI target inherits release R8/resource shrinking but uses debug signing and a profileable manifest. It is separate from physical-phone acceptance of the distributed APK.

## Model and product contract

App `0.2.0-preview` / code `2`; analysis schema `2`; policy `ordinal_v2`; rules `local_context_v2`; URL contract `lexical_v2`. Shipping estimator: `ss-char-lr-0.1.0`, schema `1`, protocol `ss-group-v1`, SHA-256 `14927b442dc9f33a35ad7086bd522b87e1110a73a6d1b2826b73ac1bf1a8a8fa`.

The eight-configuration experiment `exploratory_20261116_02` was already completed and was not retrained on resume. Its clean-quality/noise trade-offs did not earn a replacement promotion. Historical 861-record grouped test evidence remains preserved and was not rescored or advertised as a new blind test. [Model comparison](MODEL_COMPARISON.md) · [Research protocol](ML_OPTIMIZATION_PLAN.md) · [Historical evaluation](EVALUATION_RESULTS.md).

The complete application fixes global context suppression, conservative URL handling and fail-open archive validation; preserves three-platform parity; provides actual native screenshot review and safe lifecycle ownership; bounds optional summary history and independent redacted exports; and supplies the functional local-worker browser tool. Source rights, raw-data exclusions and notices are preserved. No dataset permission emails were sent.

## Review and demonstration

Original native and browser screenshots are retained under `assets/evidence/v2/final`. The prerelease includes the actual production [browser workflow recording](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.2.0-preview/ScamSentry_Browser_Workflow.webm). This recording is separate from a narrated faculty rehearsal and from phone performance evidence.

For a 6–10 minute review use [FACULTY_DEMO.md](FACULTY_DEMO.md); course outlines are in [ACADEMIC_REPORT_OUTLINES.md](ACADEMIC_REPORT_OUTLINES.md). Demonstrate an ordinary message, legitimate urgency, direct secret request, mixed warning/request, edited OCR, deceptive host, limited-support input and manual history controls. All examples are fictional.

## Recorded limits and next acceptance

- Physical-phone startup/text/OCR timings, heap/battery, generated Baseline Profile, TalkBack/large-font/adaptive keyboard review, first-use offline operation of the downloaded release and actual 16 KB page-size runtime remain open. Procedures and benchmark code are ready in [DEVICE_CHECKLIST.md](DEVICE_CHECKLIST.md) and [PERFORMANCE.md](PERFORMANCE.md).
- Severe obfuscation remains a known weakness of the retained model. The six OCR slices are correlated renderings of one fictional message, not a population OCR study. Marathi/Hindi/Hinglish, sender authenticity and live reputation are not validated.
- Prospective campaign-diverse model evaluation needs eligible independent data; owner-managed durable production signing is separate. These gaps remain visible in [GATE_REPORT.json](GATE_REPORT.json), where `all_gates_passed` stays false.
- Browser OCR/PWA are explicitly deferred under the specification's allowance. The browser text/URL tool is functional, has a verified production build and keeps message input local.
- **The browser app is now live on verified Hobby hosting:** https://scamsentry-bay.vercel.app/. Exact passing CI static output was deployed and all 12 public-file hashes, security headers and live workflows were verified. GitHub integration was not installed, so this standalone project does not auto-deploy Git pushes. See [WEB_DEPLOYMENT.md](WEB_DEPLOYMENT.md) and [hosting evidence](../assets/evidence/v2/hosting/VERCEL_RELEASE_VERIFICATION.json). No paid plan, trial, purchased domain, credit or database was activated.

For rollback, use the immutable `baseline-0.1.0` branch/import and frozen reports only after reviewing compatibility and the known corrected defects. Do not overwrite archived results or silently downgrade an installed database. A fresh research install of an older differently signed preview is a separate owner-controlled action.
