# Current continuation checkpoint

17 November 2026. Independent Android/GitHub/browser-source work is delivered as a published research preview. **Vercel activation/deployment is explicitly deferred by the owner.** Do not generate another time-limited activation link in this phase.

## Durable release and source

- Public repository: https://github.com/DevenGaikwad/ScamSentry
- Research prerelease: https://github.com/DevenGaikwad/ScamSentry/releases/tag/v0.2.0-preview
- Immutable application/release source: `bc227ba189796c1167c3cd21c3a7ec973156ccfd`.
- Exact-source executed workflow: https://github.com/DevenGaikwad/ScamSentry/actions/runs/44012744040
- CI evidence commit: `0462ac28b074febce68eeb38087db039de834fb4`; original reports, screenshots and provenance are in `assets/evidence/v2/final`.
- Concrete handoff: [RELEASE_HANDOFF.md](RELEASE_HANDOFF.md). Three ABI-labelled installable TEST-signed APKs, source/full ZIPs, browser workflow recording, model manifest, inventories, runtime PNGs and SHA-256 sums are attached to the release.

Later bot evidence and documentation commits are distinct from the immutable APK-producing source. Re-read actual main/release before any future mutation; preserve other changes. Do not retag the preview to a documentation commit or imply that APKs were built from one.

## Preserved baseline and completed research

Original archive SHA-256: `fbfc705ce7959c5ae48c96942475c45f3d39416189150e2123888f52f6766dca`. Original local baseline commit `499fa7879d499a715306424d0e95da2b2c554158`; identical imported tree public as `111450b974c8a0ffd153af5e908a89c29f60769c` on `baseline-0.1.0`. These identities are deliberately distinguished in PUBLICATION_PROVENANCE.json. Earlier execution history is preserved in `docs/history/v2/EXECUTION_JOURNAL.md` and dated diagnosis files, including safely minimized run14 evidence.

Eight preregistered configurations already completed `exploratory_20261116_02`: three grouped training folds, separate calibration, development uncertainty, confusion counts, per-class metrics, threshold/coverage, robustness and resource records. No candidate earned promotion: word candidates improved clean development results while noise-trained characters traded clean performance for severe-slice robustness. **Do not retrain or rescore the known historical test on resume.**

Shipping model `ss-char-lr-0.1.0`, SHA-256 `14927b442dc9f33a35ad7086bd522b87e1110a73a6d1b2826b73ac1bf1a8a8fa` remains unchanged in all packages. App `0.2.0-preview` / code `2`; analysis schema `2`, policy `ordinal_v2`, rules `local_context_v2`, URL contract `lexical_v2`.

## Executed final verification

| Scope | Actual result |
|---|---|
| Python checkout / actual source extraction | 77 / 77 passed, zero skipped |
| Browser unit/parity / production desktop+mobile workflows | 63 / 8 passed, no workflow skips/flakes |
| Linux core/app / actual Windows batch+core | 9 + 2 / 9 passed |
| API 35 native debug / external minified journeys | 16 / 2 passed, zero failures/skips |
| Build and packaging | Debug/test/minified/benchmark assembly, lint 0 errors/9 warnings, three signatures/ABIs/model digests/alignment, source/full ZIP and strict privacy checks passed |

Priority context suppression and fail-open validation fixes, 59-case full analysis contract, separate 24 numeric fixtures, real synthetic word/hybrid exports, native Room retention/migration/privacy races, OCR ownership/all-eight EXIF and browser local-worker/error/cancellation workflows are implemented and executed.

Minified OCR uses the actual system photo picker, bundled Latin recognizer, observed editable review, explicit correction and final analysis. The target is R8/resource shrunk with debug signing/profileable manifest; an external self-instrumenting driver avoids test-only app keep rules. API35 x86_64 KVM airplane mode1, actual Wi-Fi disabled and no default network were verified before/after. This is functional emulator evidence, not physical-phone acceptance.

Run14 had already passed all five application jobs and all 16+2 native cases, but strict full-ZIP scanning correctly rejected a synthetic credential fixture copied into a JUnit parameter label. Run15 minimizes credential-shaped public labels, records redaction counts and adds two regressions without weakening scanner or failed/skipped rejection. All final distribution gates then passed. The failed run remains discoverable; no real leaked credential or secret rotation is claimed.

Six rendering slices measure actual bundled OCR errors separately from category. Serif/noise have nonzero errors; all variants are correlated renderings of one fictional English message. [OCR_QUALITY.md](OCR_QUALITY.md) states these limits. Original final captures and browser timing samples are preserved with source/digests. PNG bytes/digests and test capture steps were verified, but final pixels could not be independently viewed after the local workspace became unavailable; the connected reader supports UTF-8 only. Manual visual/accessibility review remains open.

## Remaining work and resume boundary

- Physical-phone timings, heap/battery/Baseline Profile generation, TalkBack/large-font/adaptive keyboard review, first-use offline operation of the distributed APK, actual 16 KB page-size runtime and third-party receiver timing remain open. Use DEVICE_CHECKLIST.md/PERFORMANCE.md rather than restarting host tests or model fitting.
- Retained-model severe-obfuscation weakness and absence of eligible prospective independent data remain explicit. No 100% claim, multilingual validation, sender authentication or live-reputation capability is made.
- Durable owner-managed production signing is separate from the published disposable TEST signature.
- Browser OCR/PWA are honestly deferred under the specification's allowance; native OCR works and browser text/URL analysis is functional.
- Vercel is deferred by owner. Only when that work is resumed, recheck free target account/team scope, current terms/usage and exact deployment settings. No paid plan, trial, purchase, domain, credits or database was activated.

The local execution workspace became unavailable during this continuation. Durable remote source and genuine GitHub-hosted compiler/tests/runtime were used to finish. No unavailable local/browser tool was claimed as execution. Documentation-only handoff commits use [skip ci] after exact-source release verification; they do not retrigger training or imply a new APK build. `GATE_REPORT.json` intentionally retains `all_gates_passed: false` for the known failed and blocked research/device gates.
