# ScamSentry v4 release audit

Browser 0.4.0 and Android 0.3.0-preview were integrated in [PR #3](https://github.com/DevenGaikwad/ScamSentry/pull/3). The exact app source is [01765677ea3e](https://github.com/DevenGaikwad/ScamSentry/commit/01765677ea3e7a95b8983498168604cfec83090c). The browser verification and signed native release are traceable to their recorded workflow and artifacts.

| Requirement | Status | Evidence |
| --- | --- | --- |
| Keep both periods and separate the words | PASS | Desktop and narrow viewport checks, including unavailable fonts and enlarged text. [Light browser capture](../assets/evidence/v4/browser-home-light.png) |
| Simplify the pipeline | PASS | Seven desktop modules, three compact modules, finite bounded orthogonal segments, common animated and paused drawing. [Dark capture](../assets/evidence/v4/browser-home-dark.png) |
| Preserve web motion and navigation | PASS | Offscreen pause, drawing failure, pointer coexistence, public/private route boundaries and Back focus. [Motion tour](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.3.0-preview/ScamSentry_Editorial_Motion.webm) |
| Preserve browser privacy and inference | PASS | Local model/worker identity, in-memory input, canary/storage checks, real unavailable-model contract and report redaction. |
| Align the Android design | PASS | Native Compose palettes, the SS icon, static bundled fonts, time-based pipeline, persisted theme and dark system-bar screenshot check. [Native home](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.3.0-preview/native_minified_analyze_home.png) |
| Preserve native behavior | PASS | 17 debug instrumented and 2 minified emulator cases. Verified extractions, corrected OCR, shared text, migration, redacted export, and optional history. [Release checks](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.3.0-preview/RELEASE_VERIFICATION.json) |
| Publish verified preview APKs | PASS | All three public TEST-signed APK downloads match their SHA-256 sums. CI inspected package identity, manifest, ABI, bundled fonts and model, and static alignment. [SHA-256 inventory](https://github.com/DevenGaikwad/ScamSentry/releases/download/v0.3.0-preview/DOWNLOAD_SHA256SUMS.txt) |
| Evaluate ML candidates | PASS | 12 grouped training-fold fits. The highest clean cross-validation smishing recall was 81.956% against the 82% preregistered floor. The approved baseline remains in production. [ML results](ML_V4_RESULTS.md) |
| Extra prospective validation | BLOCKED | No eligible independent prospective corpus was available for this release. |
| Complete physical Android validation | BLOCKED | Physical-phone TalkBack, font/inset review, timing and energy, first-use offline operation and actual 16 KB runtime still need device evidence. [Device checklist](DEVICE_CHECKLIST.md) |
| Review current README, docs and distribution | PASS | Current media and links, source and archive test checks, strict artifact inventory, font notices and the full creator credit. Historical evidence is linked separately. |
| Deploy exact browser bytes | PASS | 27 exact public files, expected headers, real missing-route and missing-asset responses, and a query-preserving legacy redirect. [HTTP verification](../assets/evidence/v4/PUBLIC_HTTP_VERIFICATION.json) |
| Exercise public production UI | PASS | The live hero displayed the expected text and right-angle pipeline. Fictional input yielded local browser inference and a redacted preview. [Browser smoke record](../assets/evidence/v4/PRODUCTION_UI.json) |

Browser verification ran 72 unit and parity cases plus 66 runtime cases across desktop Chromium, mobile Chromium and Firefox, with zero skips, flakes or failures. The repository and extracted source ZIP each passed 77 Python tests. CI also exercised Windows and Linux core tests and signed APK packaging. The protected Vercel preview was checked by file hashes and the HTML response. Public production supplied the live inference check.

The shipping model is `ss-char-lr-0.1.0`, SHA-256 `14927b442dc9f33a35ad7086bd522b87e1110a73a6d1b2826b73ac1bf1a8a8fa`. Android package `in.deven.scamsentry` reports versionCode 3, minimum API 26 and target API 36. The disposable signer differs from the 0.2.0-preview signer, which prevents an in-place update. [Release record](../assets/evidence/v4/RELEASE_RECEIPT.json)
