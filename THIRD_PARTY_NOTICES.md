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

## Browser companion added in 0.2.0

React and React DOM 19.3.1 are MIT licensed; the React notice is included in `assets/licenses/React-MIT.txt`. TypeScript 6.0.3, Vite 8.3.8 and their pinned build/test dependencies retain their upstream licences in the npm packages and lockfile. The original 0.2.0 browser used Inter and Space Grotesk under SIL OFL; these remain bundled in Android and historical assets. The 0.3.0 browser uses Archivo and Geist Mono. No copied bank interface or unlicensed stock illustration is used. The historical geometric shield is original project SVG/CSS. It is replaced in the current browser by the original Signal Aperture SS monogram.

The public browser model is byte-identical to the attributed baseline model-derived asset. Mendeley source attribution and CC BY 4.0 conditions remain applicable independently of the project's MIT code licence. No raw training messages, private checkpoints, user messages or screenshots are hosted. Research-only toy fixtures are explicitly fictional fit/export tests and are never production fallback assets.

Browser OCR assets are not distributed in this release. Tesseract worker/WASM/language assets were not silently downloaded as part of a user analysis. Android ML Kit terms and bundled-model notices continue to apply to the native screenshot workflow.

## Editorial browser 0.3.0

Archivo Project Authors / Omnibus-Type: SIL OFL 1.1, upstream `google/fonts` commit `6c70c829f09ea345d3590406693220ea35c6553f`. Geist Project Authors: SIL OFL 1.1, upstream `vercel/geist-font` commit `ef2597cfc42f4797676df105517ccc0a1cfa6569`. The exact upstream licences accompany the browser at `web/public/fonts/archivo-OFL.txt` and `geistmono-OFL.txt`. [File hashes, actual variable axes, versions and subsetting record](assets/evidence/v3/FONT_LEDGER.json). The supplied variable fonts are subsetted to the interface character range with original axis ranges retained. Archivo includes weight 100–900 and width 62–125; Geist Mono includes weight 100–900. No third-party font CDN is contacted at runtime.

The original [Signal Aperture vectors and geometry notes](assets/brand/README.md), social card, diagrams and motion are project contributions. No reference-site assets, portraits, awards, company logos or metrics are copied. Archived Android fonts, icons and already-published APKs are unchanged. This v3 design brief is not an Android or product 3.0 release.
