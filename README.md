# ScamSentry

An Android research assistant for taking a second look at suspicious messages and links. Type, paste, share text or choose a screenshot; review the content, then inspect locally generated observations, model interpretation and useful next steps.

**Research preview 0.1.0.** Native source and an installable debug APK are prepared. Physical-device acceptance, runtime screenshots and a deliberate public signing identity are separate release gates. See [the gate report](docs/GATE_REPORT.json) before treating any capability as device-verified.

- Kotlin, Jetpack Compose and Material 3; bundled Space Grotesk and Inter.
- On-device character TF-IDF/logistic-regression inference; transparent observations and explicit uncertainty.
- Bundled Latin OCR with editable review; defensive text sharing and bounded image import.
- Safe lexical URL inspection with actual-host display. Standalone URLs do not receive a misleading message-model probability.
- Optional, manual, metadata-only history through Room; DataStore settings and a real schema migration fixture.
- Redacted report preview and deliberate FileProvider sharing. No inbox access, account or inference server.

The merged release manifest has no Internet, SMS, contacts, camera or broad gallery permission. Bundled resources are inspected separately from fresh-install offline execution. ML Kit's operational diagnostics remain a device-review consideration.

## Measured model evidence

| Historical grouped test | Result |
|---|---:|
| Test records / designated groups | 861 / 813 |
| Macro-F1 | 0.904 |
| Group-bootstrap 95% interval | 0.861–0.945 |
| Smishing precision | 65/69 = 94.2% |
| Smishing recall | 65/75 = 86.7% |
| Ordinary → smishing model predictions | 1/724 |
| Python/Kotlin golden fixtures | 24, within 1e-6 probability tolerance |

These measurements describe a historical single-source benchmark. They do not establish current Indian campaign coverage, real-world fraud prevention or phone latency. Severe character substitutions reduce held-out macro-F1 to 0.397; see [the complete results](docs/EVALUATION_RESULTS.md). A fictional OTP request is warned about by an observed-secret rule even when the statistical model alone misses it. The interface preserves that distinction.

![Measured held-out confusion matrix](assets/evidence/confusion_matrix.png)

## Build and run

Open `android/` in Android Studio with supported JDK 21, SDK platform 37.0 and build-tools 36.0.0. The checked-in Gradle 9.3.1 wrapper uses AGP 9.1.1 and the exact catalog. Minimum Android is API 26; target is API 36. The trained JSON bundle is included, so running the app does not require training or a cloud account.

```powershell
Set-Location .\android
.\gradlew.bat :core-analysis:test :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug :app:assembleRelease
```

The delivered package's `install/` directory and `SHA256SUMS.txt` identify actual generated APKs. A debug APK is development-signed. An unsigned release needs owner-managed signing before installation/public distribution. No signing material is included. [Windows setup](docs/SETUP_WINDOWS.md) covers Python reproduction and physical-phone testing.

## Inspect and reproduce

| Area | Evidence |
|---|---|
| Architecture and model interface | [Architecture](docs/ARCHITECTURE.md) · [model card](docs/MODEL_CARD.md) |
| Data rights and source reduction | [Sources](docs/DATA_SOURCES.md) · [registry](ml/manifests/sources.json) |
| Frozen evaluation | [Protocol](docs/EVALUATION_PROTOCOL.md) · [results](docs/EVALUATION_RESULTS.md) |
| Privacy and security | [Actual privacy behavior](docs/PRIVACY.md) · [threat model](docs/THREAT_MODEL.md) |
| Verification | [Gate report](docs/GATE_REPORT.json) · [device procedure](docs/DEVICE_CHECKLIST.md) |
| Reproduction | [ML commands](ml/README.md) · [dependencies](docs/DEPENDENCIES.md) |
| Faculty handoff | [Demo script](docs/FACULTY_DEMO.md) · [two report outlines](docs/ACADEMIC_REPORT_OUTLINES.md) |
| Continuation | [Project state](docs/PROJECT_STATE.md) · [roadmap](docs/ROADMAP.md) |

No raw/processed messages, trusted developer pickle files, machine-specific SDK/signing settings, credentials or keystores are distributed. CI configuration is prepared; no remote passing badge is claimed before it has actually run. Runtime screenshots will be added only after observing the packaged app, using the original fictional demo assets.

Original code: MIT. Primary trained-data attribution: Sandhya Mishra and Devpriya Soni, Mendeley Data V1, DOI 10.17632/f45bkkt8pr.1, CC BY 4.0; changes documented in the source registry. Fonts and dependencies retain their own terms. See [notices](THIRD_PARTY_NOTICES.md).
