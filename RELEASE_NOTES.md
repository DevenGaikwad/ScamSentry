# ScamSentry 0.1.0 — research preview

Implemented native message entry/review, local JSON classifier, rule explanations, lexical URL inspection, bundled Latin OCR integration, Android text sharing, optional summary history, educational guidance, settings/model information and redacted exports. Screens use the project's dark semantic theme and bundled fonts.

The primary corpus was verified and reduced before a frozen designated-group split. Baselines and a compact neural alternative were measured. The deployed character LR achieved historical held-out macro-F1 .904 on 861 records; smishing recall is 65/75. Cross-language model fixtures match. The robustness report deliberately exposes a substantial character-substitution failure.

This package is a reviewable preview. Read `docs/GATE_REPORT.json` for final observed build, lint, device and packaging statuses. A compiled APK, available OCR asset or built instrumentation APK does not imply its workflow passed on a physical phone. The install folder identifies signing status; no permanent owner key or public deployment is created.

Known limits: English research scope, historical source, imperfect campaign grouping, no current reputation lookup or learned URL classifier, poor severe obfuscation robustness, script heuristic rather than robust language identification, unresolved physical-device/accessibility/backup/performance acceptance, and no universal SDK-communication guarantee. Lower observed risk never means authenticated or guaranteed safe.
