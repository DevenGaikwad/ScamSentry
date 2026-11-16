# Frozen evaluation protocol: ss-group-v1

Established before final-test access on 16 November 2026. The machine-readable freeze is `ml/artifacts/runs/baseline_char_lr_001/frozen.json`. The access ledger records the exact artifact opened for final testing.

The primary task has three source-derived classes, ordered ordinary, promotional-or-spam, smishing-like. Mendeley ham, spam/Spam, and smishing/Smishing map respectively. UCI spam labels are not smishing annotations and are excluded from training.

Global exact and normalized duplicates are linked. Privacy-minimized templates remove variable identifiers; 4-character shingle cosine retrieval considers 12 nearest candidates and verifies Jaccard ≥ 0.80 before union. The threshold is a fixed engineering choice, not optimized on test labels. This heuristic can miss distant paraphrases and unknown campaigns. Conflicting-label connected components are quarantined; exact normalized duplicates are represented once.

Twenty stratified group folds are assigned 12/3/2/3 to train/development/calibration/test (approximately 60/15/10/15). Membership IDs are saved. All designated groups and normalized digests must remain isolated. Grouping uses corpus-wide unsupervised retrieval only; the classification vocabulary and IDF fit train exclusively. Auxiliary source overlap is an audit, not an external benchmark.

Candidate families: majority, transparent rules, word logistic regression, character Naive Bayes, character SVC, character LR at C=2/4, and a 128,131-parameter character-feature MLP trained from scratch. The direct deployment interface admits character LR. Development performance and implementation cost determine deployment; group CV within train and one-seed learning curves supplement that decision. No pretrained encoder or learned URL model is selected.

Scalar temperature minimizes multinomial log loss on calibration only, with log-temperature bounds −2..2. Policy confidence .62 and margin .18 are fixed conservative research defaults, not an optimized real-world risk score. They are frozen with the model. Ordinary/lower and smishing/higher are accepted states for selective reporting; checking and unable are abstained. This is a policy definition, not a proof of out-of-distribution detection. Rules inspect original text in the app; minimized benchmark text can lose URL and numeric signals, so benchmark policy results do not establish end-to-end app performance.

Final metrics include class precision/recall/F1/support, macro-F1, confusion matrix, accuracy, ordinary→smishing and ordinary→any-nonordinary rates, one-vs-rest smishing average precision and ROC-AUC, multiclass log loss, mean summed class Brier error, and 10 equal-width confidence-bin ECE. Macro-F1 intervals use 300 designated-group bootstrap resamples, seed 42, percentile 2.5/97.5. A raw-record binomial interval is supplementary and cannot remove residual grouping dependence.

Held-out perturbations are generated only after model freeze: case, expanded whitespace, zero-width insertions, and severe character substitutions. They remain synthetic robustness checks. They do not measure actual OCR character accuracy. No changes are tuned against these final-test failures in this release.

Development objectives: smishing precision ≥ .90, recall ≥ .80, ordinary→smishing rate ≤ .02, accepted coverage ≥ .70. Targets apply only to the available historical benchmark, with denominators and uncertainty. Phone latency budget <300 ms warm text and <5 s typical OCR is provisional. Physical-device measurements, fresh offline OCR, usability and complete end-to-end performance remain separate gates.

Training code must refuse to overwrite frozen runs. Final-test evaluation requires `--protocol ss-group-v1` and refuses a second access. The guard is an audit aid, not an access-control mechanism. Later substantive improvements need a new experiment identity and an independently defensible holdout.
