# Third-party notices

This file does not replace applicable upstream licences. No endorsement is implied. Raw third-party datasets and development toolchain binaries are not distributed in the source archive.

| Component | Attribution / terms | Distribution |
|---|---|---|
| Primary training corpus | Sandhya Mishra; Devpriya Soni (2022), Mendeley Data V1, DOI 10.17632/f45bkkt8pr.1. [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/) | Raw corpus excluded. Bundled trained model derives from minimized, normalized, deduplicated and grouped records; this attribution accompanies that artifact. |
| Space Grotesk | Florian Karsten and contributors, SIL OFL 1.1; see assets/fonts/space-OFL.txt | Bundled heading font |
| Inter | Rasmus Andersson and contributors, SIL OFL 1.1; see assets/fonts/inter-OFL.txt | Bundled body font |
| AndroidX / Compose / Room / DataStore | Android Open Source Project, Apache 2.0 | Gradle-resolved app dependencies; preserve dependency notices |
| Kotlin / Coroutines | JetBrains and contributors, Apache 2.0 | App/runtime dependencies |
| ML Kit Latin text recognition | Google; [ML Kit terms](https://developers.google.com/ml-kit/terms) and applicable Google APIs/SDK terms | Bundled OCR library/model; not relicensed as MIT |
| org.json | JSON-java contributors, public-domain distribution identified in pinned artifact | Host core compilation/tests; Android uses platform JSON |
| Gradle wrapper | Gradle, Inc. and contributors, Apache 2.0; https://github.com/gradle/gradle | Wrapper JAR and small launch scripts only; distribution digest pinned |
| scikit-learn | BSD-3-Clause | Development dependency, not phone runtime |
| NumPy / SciPy / pandas / joblib | Respective projects, BSD-style licences | Development dependencies |
| PyYAML | Kirill Simonov and contributors, MIT | Development configuration parsing |
| pytest | pytest contributors, MIT | Development tests |

The original icon, drawing-code symbols, interface, fictional demo scenarios, pipeline and integration code are project contributions. No outside security-company mark, university seal, partner logo, testimonial or app-store rating is used. The complete primary-source licence evidence is recorded in `ml/manifests/publisher_license_evidence.json`.

Before any public release, inspect final merged dependencies and their notices, perform the recorded rights/privacy review, and manage a deliberate signing identity. This preview is not a claim of a completed legal audit of every underlying public message.

The full Apache 2.0 licence is provided in `assets/licenses/Apache-2.0.txt` and bundled in the APK under `assets/licenses/`. Font licence texts are also bundled.
