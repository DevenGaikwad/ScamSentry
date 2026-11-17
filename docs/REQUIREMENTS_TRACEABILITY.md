# Master prompt execution audit

This matrix maps the **51 sections of UPGRADE_SPEC_V2_1.md**. The original 60-section v1 mapping is preserved under `docs/history/0.1.0/REQUIREMENTS_TRACEABILITY.md`; its old publication/browser statuses must not be read as current.

COMPLETE describes the stated implementation/research outcome, not universal safety. PARTIAL/PREPARED/PENDING_CI identify a precise limit. Hardware measurements and prospective data cannot be inferred from CI. Final executed results are in GATE_REPORT.json and the release's RELEASE_VERIFICATION.json.

| § | Requirement | Status | Evidence | Scope / limit |
|---:|---|---|---|---|
| 1 | Execution objective and authorized GitHub outcome | COMPLETE | README.md; docs/PUBLICATION_PROVENANCE.json | Exact public repository created and verified; release checks remain explicit. |
| 2 | Immutable starting evidence | COMPLETE | docs/history/0.1.0; docs/PUBLICATION_PROVENANCE.json | Original archive/model digests and historical reports preserved. |
| 3 | What the current project already implements | COMPLETE | android; docs/ARCHITECTURE.md | Native application retained and incrementally improved. |
| 4 | Independent checks performed during this audit | COMPLETE | assets/evidence/v2; docs/PROJECT_STATE.md | Historical audit, fresh host builds and actual CI outcomes remain distinguishable. |
| 5 | Priority finding: global suppression of secret-request warnings | COMPLETE | ml/tests/test_v2_context.py; NativeContractTest.kt | Local request context replaces global suppression; full native parity executed. |
| 6 | Priority finding: validation can report success after skipping a scan | COMPLETE | ml/tests/test_v2_release_validation.py; scripts/validate_project.py | Real non-Git scan, cache rejection and read-only validator entry point verified. |
| 7 | Important model-selection finding | COMPLETE | docs/MODEL_COMPARISON.md | Word baseline investigated; no unexplained character-only preference. |
| 8 | Important robustness finding and required research response | PARTIAL | ml/artifacts/v2; docs/MODEL_COMPARISON.md | Robustness research completed; preserved baseline still has a severe substitution weakness. |
| 9 | Important parity and policy-orchestration finding | COMPLETE | NativeContractTest.kt; web/tests/parity.test.ts | 59 complete domain/minimization cases, plus separate archived numeric and synthetic block checks. |
| 10 | URL inspection improvements | COMPLETE | UrlLexicalV2.kt; ml/src/scamsentry_ml/url_v2.py | Conservative lexical parsing, IDNA/numeric/IPv6 guards, exact 40-link boundary; no network resolution. |
| 11 | Privacy, history, and export refinements | PENDING_CI | PrivacyRaceTest.kt; OrientationExportTest.kt; HistoryMigrationTest.kt | Retention/migrations/exports executed; new queued-save privacy race awaits its run. |
| 12 | OCR correctness, memory ownership, and image handling | PENDING_CI | OcrReader.kt; PrivacyRaceTest.kt; OcrQualityTest.kt | Original OCR/EXIF executed; controlled cancellation and six rendering slices now prepared. Physical heap review remains separate. |
| 13 | Updated stack: retain the native foundation | COMPLETE | android/gradle/libs.versions.toml; docs/DEPENDENCIES.md | Kotlin/Compose native foundation and reviewed stable compatibility group retained. |
| 14 | Dependency upgrade acceptance and build reproducibility | PENDING_CI | .github/workflows/validate.yml | Linux builds executed; latest annotation/R8 test changes and Windows batch execution require new results. |
| 15 | Research protocol for v2 models | COMPLETE | docs/ML_OPTIMIZATION_PLAN.md | Protocol registered before bounded fitting; known historical test excluded from new scoring. |
| 16 | Data expansion and provenance safeguards | PARTIAL | ml/manifests/sources.json; docs/DATA_SOURCES.md | Eligible primary source retained; conditional/permission-dependent additions excluded. Fresh campaign acquisition remains open. |
| 17 | Candidate model implementation details | COMPLETE | ml/src/scamsentry_ml/blocks_v2.py; docs/HYPERPARAMETER_SEARCH.md | Word, character, augmentation and hybrid candidates evaluated; portable synthetic block exports verified. |
| 18 | Calibration, operating policy, and limited-support behavior | COMPLETE | ml/src/scamsentry_ml/metrics_v2.py; docs/METRIC_DEFINITIONS.md | Separate calibration, threshold/coverage tables and explicit limited-support outcomes. |
| 19 | Efficiency improvements in the model runtime | PARTIAL | android/core-analysis; docs/PERFORMANCE.md | Runtime allocations reduced with parity; no physical-phone speedup claimed. |
| 20 | APK size and ABI-specific distribution | COMPLETE | scripts/sign_research_apks.py; scripts/inspect_v2_apks.py | Three actual ABI artifacts, signatures, model digests, package and alignment inspection; final release inventory identifies exact bytes. |
| 21 | Baseline Profiles, Macrobenchmark, and performance evidence | PREPARED | android/benchmark; docs/PERFORMANCE.md | Release-like journeys compile; physical measurements and generated Baseline Profile remain unavailable. |
| 22 | Modern architecture without unnecessary fragmentation | COMPLETE | docs/ARCHITECTURE.md | Domain/platform boundaries and focused screen files; simple ownership seams. |
| 23 | Navigation and adaptive layout upgrade | PARTIAL | android/app/src/main/java/in/deven/scamsentry/ui | Window-size navigation implemented; physical large-window/assistive review remains pending. |
| 24 | Visual refinement and interaction quality | PARTIAL | README.md; docs/DESIGN_SYSTEM.md | Actual native/browser captures and contrast checks; manual accessibility remains pending. |
| 25 | Testing that works from the distributed source | COMPLETE | ml/tests; .github/workflows/validate.yml | Public checkout and actual non-Git extraction run without private checkpoints. |
| 26 | Device verification and realistic environment handling | PENDING_CI | docs/TEST_MATRIX.md; docs/DEVICE_CHECKLIST.md | API 35 KVM executes native suites; latest corrected/minified/offline cases pending. No physical-phone claim. |
| 27 | CI that proves the public checkout works | PENDING_CI | .github/workflows/validate.yml; scripts/create_release_evidence.py | Independent Python, browser, Android, Windows and KVM jobs; publication requires executed reports. |
| 28 | Security and data-rights release review | COMPLETE | docs/THREAT_MODEL.md; THIRD_PARTY_NOTICES.md | Least privilege, source rights, secret/raw-data exclusions and advisory/package review; not a universal vulnerability warranty. |
| 29 | Create and initialize the exact GitHub repository | COMPLETE | docs/PUBLICATION_PROVENANCE.json | Public DevenGaikwad/ScamSentry verified; no other repository modified. |
| 30 | Repository content, release assets, and professional presentation | PENDING_CI | README.md; release; scripts/package_release.py | Reviewed source and real screenshots public; installable prerelease awaits gates. |
| 31 | Signing, versioning, and release channels | PENDING_CI | RELEASE_NOTES.md; scripts/package_release.py | App 0.2.0-preview/code 2, disposable TEST signing, exact source/model/checksum inventory. Production identity remains owner-managed. |
| 32 | Academic and competition-level evidence | PARTIAL | docs/ACADEMIC_REPORT_OUTLINES.md; docs/FACULTY_DEMO.md | Two course outlines, viva/demo plan, architecture and measured assets; no invented rehearsal, participants or faculty approval. |
| 33 | Scope priorities and useful optional extensions | COMPLETE | docs/ROADMAP.md | Priority fixes completed; QR, learned URL classifier and multilingual expansion remain optional future work. |
| 34 | Milestones and concrete deliverables | PARTIAL | docs/PROJECT_STATE.md | Preservation, fixes, publication and bounded research completed; final runtime/package verification in progress. |
| 35 | Resume protocol and evidence management | COMPLETE | docs/PROJECT_STATE.md; assets/evidence/v2 | Failed runs retained; resume from public checkpoints without repeating completed training. |
| 36 | Definition of completion for this follow-up | PARTIAL | docs/GATE_REPORT.json | Research-preview completion is distinct from remaining physical-device and independent-data gates. |
| 37 | Explicit optimization objective | COMPLETE | docs/ML_OPTIMIZATION_PLAN.md; docs/MODEL_COMPARISON.md | Optimization pursued jointly across errors, quality, calibration, coverage and resources. |
| 38 | Confusion counts and classification metrics | COMPLETE | docs/METRIC_DEFINITIONS.md; ml/artifacts/v2 | TP/FP/TN/FN, fixed labels, precision/recall/FPR/FNR, F1/F2, MCC, calibration and uncertainty recorded. |
| 39 | Whole-system metrics and abstention | COMPLETE | ml/src/scamsentry_ml/metrics_v2.py | Full-pipeline deferral, coverage and unresolved-positive denominators separate from classifier argmax. |
| 40 | Hyperparameter and feature optimization | COMPLETE | docs/HYPERPARAMETER_SEARCH.md | Eight bounded configurations, three grouped training folds, train-only augmentation, seeds/time/memory recorded. |
| 41 | Thresholds, calibration, and error reduction | COMPLETE | ml/artifacts/v2; docs/MODEL_COMPARISON.md | Calibration/threshold trade-offs and class errors examined together; no final-test tuning. |
| 42 | Model promotion and statistical evidence | COMPLETE | docs/MODEL_COMPARISON.md | Baseline retained after measured clean/noise trade-offs and missing prospective/device evidence. |
| 43 | Metric artifacts and reproducible reporting | COMPLETE | docs/METRIC_DEFINITIONS.md; assets/evidence/v2/model_tradeoffs.png | Machine-readable candidate metrics, comparison tables and plotted actual results. |
| 44 | Vercel feasibility and zero-cost conditions | DEFERRED_BY_OWNER | docs/ZERO_COST_DEPLOYMENT_CHECKLIST.md | 17 November instruction explicitly postpones Vercel activation/deployment; no new link or action. |
| 45 | Browser application architecture | COMPLETE | web; docs/WEB_ARCHITECTURE.md | Functional static React/Vite client, worker inference, digest checks, redacted downloads and production browser evidence. |
| 46 | Python, Kotlin, and browser parity | COMPLETE | ml/artifacts/parity; NativeContractTest.kt; web/tests/parity.test.ts | Python/Kotlin/browser contract, Unicode/privacy/URL edge cases and separate numeric/export checks. |
| 47 | Browser screenshot support | DEFERRED | docs/WEB_ARCHITECTURE.md; web/src/App.tsx | Browser OCR/PWA not included; UI explicitly directs screenshot users to native Android. |
| 48 | Copyright, permissions, and privacy | COMPLETE | THIRD_PARTY_NOTICES.md; ml/manifests/sources.json | Eligible public assets and attribution; no permission emails, inbox collection or paid inference. |
| 49 | GitHub and conditional web deployment | PARTIAL | README.md; docs/WEB_DEPLOYMENT.md | Correct GitHub source public; conditional Vercel hosting deferred by owner. |
| 50 | Added release gates | PENDING_CI | docs/GATE_REPORT.json; scripts/create_release_evidence.py | Separate required gates and exact-commit release verification; failed/blocked outcomes never become all-passed. |
| 51 | Final execution instruction | PARTIAL | docs/PROJECT_STATE.md | Independent authorized work continued; final handoff awaits new executable gates, with Vercel explicitly deferred. |

## Appendix A: all 30 required properties

| Regression | Status before final run | Evidence / boundary |
|---|---|---|
| REG-01 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-02 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-03 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-04 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-05 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-06 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-07 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-08 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-09 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-10 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-11 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-12 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-13 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-14 | EXECUTED | 59-case contract and native/reference/browser suites; local-context and conservative parser cases |
| REG-15 | EXECUTED | Bundle/worker corruption and unavailable-model paths; no reassuring fallback |
| REG-16 | EXECUTED | test_v2_export.py and actual tiny fit/export; derived run metadata and production-path protection |
| REG-17 | EXECUTED | Public archive/privacy/fit-export tests; no private checkpoint needed |
| REG-18 | EXECUTED | Public archive/privacy/fit-export tests; no private checkpoint needed |
| REG-19 | EXECUTED | Public archive/privacy/fit-export tests; no private checkpoint needed |
| REG-20 | PENDING_CI | PrivacyRaceTest: rapid replacement, completion cancellation and queued-save race |
| REG-21 | PENDING_CI | PrivacyRaceTest: rapid replacement, completion cancellation and queued-save race |
| REG-22 | EXECUTED | All eight EXIF pixel layouts; original actual bundled OCR is a separate check |
| REG-23 | PENDING_CI | PrivacyRaceTest: rapid replacement, completion cancellation and queued-save race |
| REG-24 | EXECUTED | RetentionTest and schema-3 migration prune actual storage to 200 |
| REG-25 | EXECUTED | OrientationExportTest: independent files and expired-file pruning; external recipient behavior still requires a real receiving app |
| REG-26 | EXECUTED | Enabled/off history setting and manual save/delete controls; physical accessibility remains separate |
| REG-27 | PENDING_CI / PHYSICAL OPEN | Minified OCR + text gates, recorded emulator radio state; fresh physical-phone installation still needed |
| REG-28 | PHYSICAL OPEN | Contrast/semantics implemented; TalkBack, maximum font and correction workflow need manual runtime review |
| REG-29 | EXECUTED / FINAL INVENTORY PENDING | Actual arm64 APK/signature/model/ABI/alignment checks; final download checksums generated by release job |
| REG-30 | PUBLIC SOURCE VERIFIED / FINAL CI PENDING | Owner/name/public source and commit verified; final release workflow outcomes remain explicit |

The six rendering slices report character/word extraction error separately from observed risk. They do not establish general OCR accuracy or recalibrate the classifier. Windows batch/core checks are separate from a complete Windows Android SDK installation. Browser timing remains Linux/Chromium evidence, not phone latency.
