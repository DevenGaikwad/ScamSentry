# ScamSentry 0.2.0 — Research Preview

Native Android and local-browser analysis share versioned preprocessing, observations, URL parsing, quality flags and decision policy. This preview fixes global secret-request suppression and the archive validator's skipped-scan failure, adds meaningful cross-platform regressions, improves native retention/OCR/export handling, and includes the functional worker-based browser companion.

The approved estimator remains `ss-char-lr-0.1.0`; historical metrics are preserved. Eight v2 configurations were evaluated on allowed exploratory partitions. Word models improved clean development quality; noise-trained character models improved the synthetic substitution slice while losing clean quality. No replacement is promoted without the declared independent validation and resource evidence.

APK assets are **TEST-SIGNED with a disposable research identity**, versionCode 2 / `0.2.0-preview`, minimum Android 8 / API 26. Choose arm64 for compatible phones, x86_64 for emulators, or inspect the universal package's ABI inventory. These are installable research artifacts, separate from production signing. A prior build with another key may require uninstalling it first. Review `ARTIFACT_INVENTORY.json` and `SHA256SUMS.txt` for exact source/model/artifact identities.

Raw datasets, private checkpoints and signing secrets are excluded. Drafts stay local; history is manual, summary-only and capped at 200 entries. Reports are redacted and reviewed before sharing. Browser input stays in memory and the worker verifies public model assets before inference.

Research limits remain explicit: historical English messages, no verified sender identity, no live reputation lookup, no validated Marathi/Hindi/Hinglish coverage, and no guaranteed detection. Browser OCR/PWA support is deferred. Physical-phone benchmarks, first-use offline OCR and 16 KB runtime behavior remain device gates even when host/CI checks pass. Consult `docs/GATE_REPORT.json`, the test matrix and model card for actual evidence.

Conditional Vercel deployment requires verified free scope and access. No paid plan, trial, domain, inference service or database is required or activated.
