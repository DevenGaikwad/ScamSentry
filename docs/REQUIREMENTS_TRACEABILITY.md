# Master prompt execution audit

This matrix maps the **51 sections of UPGRADE_SPEC_V2_1.md**. The original 60-section v1 mapping is preserved under `docs/history/0.1.0/REQUIREMENTS_TRACEABILITY.md`; its old publication/browser statuses must not be read as current.

COMPLETE describes the stated implementation/research outcome, not universal safety. PARTIAL/PREPARED identify a precise limit. Hardware measurements and prospective data cannot be inferred from CI. Final executed results are in GATE_REPORT.json and the release's RELEASE_VERIFICATION.json.

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
| 11 | Privacy, history, and export refinements | COMPLETE | HistoryMigrationTest.kt; PrivacyRaceTest.kt; OrientationExportTest.kt | API 35 executes schema migration, 200-summary retention, queued-save cancellation, independent redacted exports and workflow controls. |
| 12 | OCR correctness, memory ownership, and image handling | PARTIAL | OcrReader.kt; PrivacyRaceTest.kt; OcrQualityTest.kt; docs/OCR_QUALITY.md | Bundled OCR, all eight EXIF layouts, replacement/cancellation ownership and six measured rendering slices execute. Physical heap and varied-message OCR evaluation remain open. |
| 13 | Updated stack: retain the native foundation | COMPLETE | android/gradle/libs.versions.toml; docs/DEPENDENCIES.md | Kotlin/Compose native foundation and reviewed stable compatibility group retained. |
| 14 | Dependency upgrade acceptance and build reproducibility | COMPLETE | assets/evidence/v2/final/RELEASE_VERIFICATION.json; docs/DEPENDENCIES.md | Exact-source Linux builds, Windows batch/core, R8/resource-shrunk runtime journeys, lint and three signed APK inspections passed. |
| 15 | Research protocol for v2 models | COMPLETE | docs/ML_OPTIMIZATION_PLAN.md | Protocol registered before bounded fitting; known historical test excluded from new scoring. |
| 16 | Data expansion and provenance safeguards | PARTIAL | ml/manifests/sources.json; docs/DATA_SOURCES.md | Eligible primary source retained; conditional/permission-dependent additions excluded. Fresh campaign acquisition remains open. |
| 17 | Candidate model implementation details | COMPLETE | ml/src/scamsentry_ml/blocks_v2.py; docs/HYPERPARAMETER_SEARCH.md | Word, character, augmentation and hybrid candidates evaluated; portable synthetic block exports verified. |
| 18 | Calibration, operating policy, and limited-support behavior | COMPLETE | ml/src/scamsentry_ml/metrics_v2.py; docs/METRIC_DEFINITIONS.md | Separate calibration, threshold/coverage tables and explicit limited-support outcomes. |
| 19 | Efficiency improvements in the model runtime | PARTIAL | android/core-analysis; docs/PERFORMANCE.md | Runtime allocations reduced with parity; no physical-phone speedup claimed. |
| 20 | APK size and ABI-specific distribution | COMPLETE | scripts/sign_research_apks.py; scripts/inspect_v2_apks.py | Three actual ABI artifacts, signatures, model digests, package and alignment inspection; final release inventory identifies exact bytes. |
| 21 | Baseline Profiles, Macrobenchmark, and performance evidence | PREPARED / FUNCTIONAL EXECUTED | android/benchmark; docs/PERFORMANCE.md | External release-like text and system-picker/OCR journeys execute. Physical Macrobenchmark timings and generated Baseline Profile remain unavailable. |
| 22 | Modern architecture without unnecessary fragmentation | COMPLETE | docs/ARCHITECTURE.md | Domain/platform boundaries and focused screen files; simple ownership seams. |
| 23 | Navigation and adaptive layout upgrade | PARTIAL | android/app/src/main/java/in/deven/scamsentry/ui | Window-size navigation implemented; physical large-window/assistive review remains pending. |
| 24 | Visual refinement and interaction quality | PARTIAL | README.md; docs/DESIGN_SYSTEM.md | Actual native/browser captures and contrast checks; manual accessibility remains pending. |
| 25 | Testing that works from the distributed source | COMPLETE | ml/tests; .github/workflows/validate.yml | Public checkout and actual non-Git extraction run without private checkpoints. |
| 26 | Device verification and realistic environment handling | PARTIAL | assets/evidence/v2/final/RELEASE_VERIFICATION.json; docs/DEVICE_CHECKLIST.md | All 16 debug and both external minified API 35 KVM cases pass with verified offline radio/network state. Physical-phone gates remain open. |
| 27 | CI that proves the public checkout works | COMPLETE | assets/evidence/v2/final/RELEASE_VERIFICATION.json; .github/workflows/validate.yml | Five independent required jobs and gated preview publication passed for the immutable release source; no skipped cases. |
| 28 | Security and data-rights release review | COMPLETE | docs/THREAT_MODEL.md; THIRD_PARTY_NOTICES.md | Least privilege, source rights, secret/raw-data exclusions and advisory/package review; not a universal vulnerability warranty. |
| 29 | Create and initialize the exact GitHub repository | COMPLETE | docs/PUBLICATION_PROVENANCE.json | Public DevenGaikwad/ScamSentry verified; no other repository modified. |
| 30 | Repository content, release assets, and professional presentation | COMPLETE | README.md; docs/RELEASE_HANDOFF.md; assets/evidence/v2/final | Public reviewed source, exact-release native/browser captures, installable prerelease, source/full ZIPs, recording, notices and checksums. |
| 31 | Signing, versioning, and release channels | COMPLETE / PRODUCTION KEY OPEN | RELEASE_NOTES.md; assets/evidence/v2/final/ARTIFACT_INVENTORY.json | App 0.2.0-preview/code 2, three disposable TEST-signed APKs and exact source/model/checksum inventory published. Production identity remains owner-managed. |
| 32 | Academic and competition-level evidence | PARTIAL | docs/ACADEMIC_REPORT_OUTLINES.md; docs/FACULTY_DEMO.md | Two course outlines, viva/demo plan, architecture and measured assets; no invented rehearsal, participants or faculty approval. |
| 33 | Scope priorities and useful optional extensions | COMPLETE | docs/ROADMAP.md | Priority fixes completed; QR, learned URL classifier and multilingual expansion remain optional future work. |
| 34 | Milestones and concrete deliverables | COMPLETE WITH RECORDED LIMITS | docs/PROJECT_STATE.md; docs/RELEASE_HANDOFF.md | Preservation, priority fixes, public source, bounded model research, executed runtime verification and installable preview delivered. |
| 35 | Resume protocol and evidence management | COMPLETE | docs/PROJECT_STATE.md; assets/evidence/v2 | Failed runs retained; resume from public checkpoints without repeating completed training. |
| 36 | Definition of completion for this follow-up | COMPLETE WITH RECORDED LIMITS | docs/GATE_REPORT.json; docs/RELEASE_HANDOFF.md | Concrete research-preview handoff delivered; physical-device, prospective-data and known robustness limits remain explicit. |
| 37 | Explicit optimization objective | COMPLETE | docs/ML_OPTIMIZATION_PLAN.md; docs/MODEL_COMPARISON.md | Optimization pursued jointly across errors, quality, calibration, coverage and resources. |
| 38 | Confusion counts and classification metrics | COMPLETE | docs/METRIC_DEFINITIONS.md; ml/artifacts/v2 | TP/FP/TN/FN, fixed labels, precision/recall/FPR/FNR, F1/F2, MCC, calibration and uncertainty recorded. |
| 39 | Whole-system metrics and abstention | COMPLETE | ml/src/scamsentry_ml/metrics_v2.py | Full-pipeline deferral, coverage and unresolved-positive denominators separate from classifier argmax. |
| 40 | Hyperparameter and feature optimization | COMPLETE | docs/HYPERPARAMETER_SEARCH.md | Eight bounded configurations, three grouped training folds, train-only augmentation, seeds/time/memory recorded. |
| 41 | Thresholds, calibration, and error reduction | COMPLETE | ml/artifacts/v2; docs/MODEL_COMPARISON.md | Calibration/threshold trade-offs and class errors examined together; no final-test tuning. |
| 42 | Model promotion and statistical evidence | COMPLETE | docs/MODEL_COMPARISON.md | Baseline retained after measured clean/noise trade-offs and missing prospective/device evidence. |
| 43 | Metric artifacts and reproducible reporting | COMPLETE | docs/METRIC_DEFINITIONS.md; assets/evidence/v2/model_tradeoffs.png | Machine-readable candidate metrics, comparison tables and plotted actual results. |
| 44 | Vercel feasibility and zero-cost conditions | COMPLETE | assets/evidence/v2/hosting/VERCEL_RELEASE_VERIFICATION.json | Owner resumed hosting; authenticated target team verified Hobby, current terms/limits checked, no paid plan/trial/domain/database/add-on activated. |
| 45 | Browser application architecture | COMPLETE | web; docs/WEB_ARCHITECTURE.md; assets/evidence/v2/hosting/VERCEL_RELEASE_VERIFICATION.json | Functional worker-based React/Vite source plus live static deployment; public bytes/security headers and actual live workflows verified. |
| 46 | Python, Kotlin, and browser parity | COMPLETE | ml/artifacts/parity; NativeContractTest.kt; web/tests/parity.test.ts | Python/Kotlin/browser contract, Unicode/privacy/URL edge cases and separate numeric/export checks. |
| 47 | Browser screenshot support | DEFERRED | docs/WEB_ARCHITECTURE.md; web/src/App.tsx | Browser OCR/PWA not included; UI explicitly directs screenshot users to native Android. |
| 48 | Copyright, permissions, and privacy | COMPLETE | THIRD_PARTY_NOTICES.md; ml/manifests/sources.json | Eligible public assets and attribution; no permission emails, inbox collection or paid inference. |
| 49 | GitHub and conditional web deployment | COMPLETE | assets/evidence/v2/hosting/VERCEL_RELEASE_VERIFICATION.json; docs/WEB_DEPLOYMENT.md | Exact GitHub repository retained; live public Hobby URL verified. GitHub integration was absent, so reviewed passing CI static output was uploaded; future Git pushes are not automatic deployments. |
| 50 | Added release gates | COMPLETE | docs/GATE_REPORT.json; assets/evidence/v2/final/RELEASE_VERIFICATION.json | Required original/additive gates have actual evidence; remaining failed/blocked/not-applicable outcomes stay visible and all_gates_passed remains false. |
| 51 | Final execution instruction | COMPLETE WITH RECORDED LIMITS | docs/PROJECT_STATE.md; docs/RELEASE_HANDOFF.md | Independent implementation/evaluation/source/release work and the resumed free Vercel browser publication completed. Physical-device and prospective-data limits remain recorded. |

## Appendix A: all 30 required properties

Exact executed counts and environment are in [RELEASE_VERIFICATION.json](../assets/evidence/v2/final/RELEASE_VERIFICATION.json). These properties deliberately retain partial/manual limits.

| Regression | Scenario | Status | Evidence / boundary |
|---|---|---|---|
| REG-01 | Direct affirmative OTP request | PASS | ContextRulesV2Test, test_v2_context.py and complete native/reference/browser contract: concrete observation stays visible. |
| REG-02 | Unrelated “for example” phrase | PASS | Local-context regressions and full contract prevent unrelated educational labels suppressing a request. |
| REG-03 | Warning followed by password request | PASS | Independent local contexts preserve the later affirmative request. |
| REG-04 | “Do not share this notice” plus OTP request | PASS | Negation referent regressions preserve the separate secret request. |
| REG-05 | Educational quotation | PASS | Attached-quote/local-negation cases avoid asserting an active request from the quotation alone. |
| REG-06 | Legitimate urgency | PASS | ParityTest.policyDoesNotTreatUrgencyAsProof and complete contract. |
| REG-07 | Zero-feature gibberish | PASS | Complete contract emits limited support, consistently in all three implementations. |
| REG-08 | Very few active features | PASS | Frozen policy quality boundaries and weak/repetitive-input fixtures; no new population validation implied. |
| REG-09 | Bare standalone URL | PASS | 59-case full contract: lexical observations without invented message-risk percentage. |
| REG-10 | Uppercase HTTP scheme | PASS | Cross-platform URL fixtures match scheme handling. |
| REG-11 | User-info deception | PASS | Actual host exposed conservatively without opening the link. |
| REG-12 | Unicode/punycode host | PASS | IDNA and Unicode fixtures match; no automatic fraud attribution. |
| REG-13 | IPv6, ports, malformed authority | PASS | Bounded conservative parser fixtures on Python, Kotlin and TypeScript. |
| REG-14 | Exactly forty / more than forty links | PASS | 40 remains complete; 41st sets explicit truncation/limitation. |
| REG-15 | Corrupt model / unknown schema | PASS | Bundle integrity/unit and real browser corrupt-model workflow; no reassuring fallback. |
| REG-16 | New run export metadata | PASS | Public tiny scikit-learn fit/export and word/hybrid block parity derive their own run identifiers. |
| REG-17 | Non-Git source extraction | PASS | Actual source and full package extraction use real filesystem scans; source extraction also runs all 77 Python tests. |
| REG-18 | Forbidden data / synthetic secret | PASS | Rejecting archive/repository regressions plus strict final full-ZIP privacy scan; label minimization cannot hide failed/skipped tests. |
| REG-19 | Missing private reference checkpoint | PASS | All public fit/export consistency checks use an actual tiny fictional fit. No private joblib dependency or skipped reference test. |
| REG-20 | Rapid screenshot replacement | PASS | PrivacyRaceTest.rapidImageReplacementKeepsLatestOutcomeAndClearOwnsPreview. |
| REG-21 | Cancellation around OCR completion | PASS | PrivacyRaceTest.cancellationAfterNativeCompletionRecyclesUndeliveredDraft. |
| REG-22 | All eight EXIF orientations | PASS | OrientationExportTest verifies actual pixel layout for all eight transformations; OCR has separate integration cases. |
| REG-23 | History off / queued save | PASS | PrivacyRaceTest.queuedSaveCannotSurviveHistoryBeingDisabled. |
| REG-24 | More than 200 summaries | PASS | RetentionTest transactional prune and HistoryMigrationTest schema-3 legacy prune. |
| REG-25 | Concurrent exports / delayed receiver read | PARTIAL | Independent files survive later export and expired-file pruning in host/native tests. Actual third-party receiver timing remains a physical/manual check. |
| REG-26 | History enabled with editor visible | PASS | WorkflowTest.manualSummarySaveDeleteAndDisableControls; metadata-only setting reflected in UI. |
| REG-27 | Release build / fresh offline install | CI PASS / PHYSICAL OPEN | External minified text plus real system-picker/OCR/review/edit/analysis pass on fresh API 35 emulator with radios disabled and no default network. Distributed test-signed release APK still needs physical-phone first-use acceptance. |
| REG-28 | Large fonts / assistive reading | PHYSICAL OPEN | Contrast checks and accessibility semantics implemented; TalkBack, maximum-font layout and manual correction review remain pending. |
| REG-29 | Arm64 APK identity | PASS | Final APK inspection and inventory verify ABI, package, signature, version, frozen model digest, bytes and SHA-256. |
| REG-30 | Public repository / CI | PASS | Exact owner/name, public visibility, release source commit, five required jobs and successful gated publication verified. |

The six OCR rendering slices describe one fictional English message under correlated image variants; extraction error is separate from observed category. They do not establish general OCR/classification accuracy. Windows batch/core execution is separate from a complete Windows Android SDK installation. Browser worker timing remains Linux/Chromium evidence, excluding network and UI, with no phone-latency claim.
