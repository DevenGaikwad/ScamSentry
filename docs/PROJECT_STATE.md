# ScamSentry v2 continuation

16 November 2026. Original checkout preserved at local tag `baseline-0.1.0`, commit `499fa7879d499a715306424d0e95da2b2c554158`. Original archive SHA-256 `fbfc705ce7959c5ae48c96942475c45f3d39416189150e2123888f52f6766dca` verified; 154 safe entries, 114,887,257 uncompressed bytes. Historical reports remain in `docs/history/0.1.0`. Do not overwrite the frozen model or historical test reports.

## Published source and evidence

Public repository: https://github.com/DevenGaikwad/ScamSentry. Initial full upgrade publication: remote commit `d984f9b0ddda9607c27b7baccbbe28c710ee1493`, tree `6f200cd8aca99332caba1d851883f3fbec4f44cd`, exactly matching reviewed local commit `6d66e02d9bf67aa222f728c26ac91677d062f257`. The remote baseline import is `111450b974c8a0ffd153af5e908a89c29f60769c`, tree `f6b1f1a7ff8b70f2bb93299143d78789f6a93e0c`; remote branch `baseline-0.1.0` points there. Commit identities differ because the connector imports Git trees; provenance records this explicitly.

CI run https://github.com/DevenGaikwad/ScamSentry/actions/runs/43864597675 passed Python/archive, Android build/host checks and browser workflows. Browser: 57 unit/parity checks and eight real production Playwright workflows passed. Real browser screenshots/network assertions are under `assets/evidence/v2`; screenshots are fictional fixtures, not native-phone evidence. Android: nine core JUnit methods, app unit tests, lint (0 errors, 9 warnings), debug/test APKs, minified release and release-like benchmark assembly passed. Actual package reports, test counts and source IDs are in `ci_run1_build.json`.

The API 35 KVM instrumentation task failed. Collection after emulator shutdown then blocked on adb; cancellation requested, report pending. Repair moves bounded collection inside the emulator lifetime and preserves the test exit code. Do not describe device workflows as passed until actual reports establish that. Local machine has no `/dev/kvm` or phone; do not repeat known software-only emulator failures.

Current local Python checks: 74 pass. New archive validator rejects caches and unexpected payload paths, checks APK inventory paths and tampering. Full release packaging now re-extracts and validates the complete APK-bearing ZIP, and rejects signing reports from another source commit. Browser refinements retain an idle worker, cancel active edits and scroll mobile results into view; their real workflow/timing rerun is pending.

## Model and release scope

App `0.2.0-preview` / versionCode 2, analysis schema 2, policy `ordinal_v2`, context `local_context_v2`, URL `lexical_v2`; shipped model remains `ss-char-lr-0.1.0`, SHA-256 `14927b442dc9f33a35ad7086bd522b87e1110a73a6d1b2826b73ac1bf1a8a8fa`.

Eight configurations completed exploratory run `exploratory_20261116_02` (142.39 s). Word C1 development macro-F1 .9243 versus baseline .9000; robust character C4 clean .8839 and severe-noise .8962. No replacement satisfies all promotion evidence; retain the baseline. Historical final-test .903759 is separate from new development comparisons. Do not rescore or relabel the known historical test as blind.

Three ABI APKs have been built with disposable TEST identities and verified signatures/ZIP alignment. Local arm64 download is 15,759,844 bytes. Packaged model digests and all 64-bit ELF load alignments pass. Source/release inventories refer to exact builds; do not reuse old signed artifacts with new commit metadata. GitHub prerelease publication is pending final verification. Production signing, physical-phone timings, first-use offline behavior, accessibility and actual 16 KB runtime remain separate gates.

## Hosting and next actions

Authorization covers public ScamSentry source and conditional zero-cost Vercel hosting. Vercel user billing reports active Hobby with no trial, but target team `team_SWRAhDlKOj1YObsysmS7m60F` returns 403 and its entitlement remains unverified. Earlier authorization link expired without observed approval. No paid plan, purchase, credits, permission email or private message was used. Continue independent GitHub work; do not bypass the connector scope restriction.

Next: retrieve failed instrumentation reports; repair any runtime defect; publish reviewed source refinements; run CI with production browser workflows and bounded KVM tests; create the accurately labelled research prerelease and verify assets. Update this state and `GATE_REPORT.json` with observed results. Never turn pending gates into PASS to obtain a complete badge.
