# V2 verification matrix

Status is evidence-specific. Host checks do not imply device checks. Historical v1 evidence is preserved under `docs/history/0.1.0` and existing baseline evidence files; new results use `assets/evidence/v2`.

| Area | Executed / prepared checks | Evidence or status |
|---|---|---|
| Context rules | Unrelated example labels, local negation, attached quotes, warning then new request, exception clauses | Python regressions and Kotlin ContextRulesV2Test pass |
| Numeric baseline | 24 archived normalized/features/norm/logits/calibrated estimates and v1 categories | Host ParityTest pass; original bundle unchanged |
| Complete domain contract | 53 fictional cases, including URL-only, unsupported text, absent model, short OCR, weak/repetitive input, exactly 40 and >40 links, malformed numeric/IDN/IPv6 hosts | Python-generated pack; Kotlin FullContractTest pass; browser parity pass |
| Word/hybrid export | Actual tiny fictional scikit-learn fit and export; both blocks, norms, logits, estimates | Python, Kotlin and TypeScript synthetic-reference tests pass |
| Public fit/export | Tiny real LR fit; staged metadata; malformed class/shape/threshold/numeric contracts | Public Python tests; no private joblib skip |
| Metrics | Known matrix, absent classes, no predicted positives, imbalance, deferral denominators | metrics_v2 tests pass |
| Distribution validation | Non-Git archive walk, repository Git failure, raw data, synthetic token/private-key markers, keystore-like path, symlink escape | Explicit stage output and isolated Python regressions |
| Android build | Core + app unit tests, lint, debug/test/release APKs and benchmark assembly | Fresh public CI PASS; lint 0 errors / 9 warnings; signed APK/package evidence in ci_run1_build.json |
| History | Transactional insert/prune; schema 2→3 prunes legacy rows; v1 summaries preserved | KVM run 1 failed; actual reports and repaired rerun pending |
| OCR | Original fictional image, malformed bytes, all 8 EXIF pixel layouts, reviewed text | KVM run 1 failed; no physical-device claim |
| Lifecycle / concurrency | Rotation keeps draft, new share replaces pending input, cancellation/version guards | Browser stale-result workflow PASS; native runtime diagnosis pending |
| Export | Redaction, current version metadata, independent files and expired-file pruning | Host app unit tests PASS; native tests prepared; sharing errors visible |
| Browser workflow | Real worker, desktop/mobile layout, corrupt model, local-input request checks, redacted report, navigation | Eight real production Playwright desktop/mobile workflows PASS on d984f9b; new worker/scroll refinements need rerun |
| Browser OCR | No compatible fully reviewed/validated browser asset chain established | Deferred; explicit UI limitation |
| Physical performance | Cold/warm startup, inference, OCR, scrolling, heap, 16 KB runtime, fresh offline operation | Blocked: no physical phone / local KVM |
| Windows invocation | Regenerated official wrapper; SDK/Windows instructions retained | Batch execution not run on this Linux host |
| Remote CI / release | Independent Python/archive, Android, browser, KVM emulator and explicit preview publication jobs | Public source verified; Python/Android/browser jobs PASS, instrumentation failed; prerelease pending |

Browser test canaries and screenshots are fictional. No private input is included in log artifacts. A CI emulator is functional evidence, not a phone performance benchmark. APK signature/ZIP/ELF checks and runtime page-size behavior have independent outcomes.
