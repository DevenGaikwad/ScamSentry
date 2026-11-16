# Model card: ss-char-lr-0.1.0

Research preview for English short-message resemblance, not authentication or guaranteed scam detection. Owner/integration: Deven Sachin Gaikwad. Version 0.1.0. Prepared 16 November 2026.

- Task/class order: ordinary, promotional-or-spam, smishing-like. Source annotations do not establish an individual sender's intent.
- Source: Mishra and Soni, Mendeley Data V1, DOI 10.17632/f45bkkt8pr.1, CC BY 4.0. Original corpus 5,971 records; 5,740 retained after conflict quarantine and duplicate reduction. No raw messages distributed.
- Feature contract: typed token minimization, NFKC, ASCII lowercasing, fixed whitespace, overlapping Unicode codepoint n-grams 3–5, sublinear TF, train-only smoothed IDF, L2 norm. Vocabulary is bounded at 20,000 entries.
- Model: class-balanced regularized multinomial logistic regression C2, scalar temperature 0.55833103, ordinal policy confidence .62 and margin .18. No combined safety percentage.
- Validation: historical grouped test 861 records/813 groups; macro-F1 0.904, smishing precision 94.20%, recall 65/75. See EVALUATION_RESULTS.md for confidence intervals, calibration, class errors and robustness failure.
- Bundle SHA-256: `14927b442dc9f33a35ad7086bd522b87e1110a73a6d1b2826b73ac1bf1a8a8fa`. JSON size 2,369,544 bytes. The Android asset and reference bundle are checked byte-for-byte.
- Runtime: direct Kotlin sparse accumulation; no executable pickle, remote service or bundled neural runtime. Host JVM and device parity have separate recorded statuses.
- Limitations: historical/partly overlapping sources, unknown record dates/campaigns, script check is not robust language identification, Romanized/mixed language unsupported, no current Indian scam coverage claim, poor severe character-obfuscation robustness, no live URL reputation. Calibration may shift outside the benchmark.
- Product mitigation: reviewable input, editable OCR, explicit observations/uncertainty, no automatic URL opening, summary-only optional history and redacted export. Model corruption must never become a lower-risk result.
- Data privacy: numeric/email/URL strings minimized before vocabulary learning. Names/fragments may remain; this is not differential privacy. Raw/processed records and trusted developer joblib checkpoints are excluded from release.
- Licence and intended use: original code MIT; source-data-derived assets retain accompanying CC BY attribution and applicable third-party terms. Academic/research preview; no unsupported certification or medical/financial authenticity claim.

See the gate report for what was actually verified. This card must not be used to infer that physical-device or public-release gates passed.

## Application v0.2.0 integration

The estimator and its historical reports remain unchanged. Application orchestration is now schema 2 / `ordinal_v2`, with local context v2 and lexical URL v2. The model's embedded ordinal-v1 thresholds remain .62/.18; the application explicitly adds quality, standalone-URL and unavailable-model precedence. These are separate versioned contracts.

New exploratory word/character/hybrid comparisons are documented in [MODEL_COMPARISON.md](MODEL_COMPARISON.md). Their development improvements and synthetic-noise gains are not attached to this baseline's historical test score. The approved browser asset is byte-identical to the same baseline. Actual complete-domain fixtures compare Python, Kotlin and TypeScript; physical-phone and contemporary external evidence remain separate.
