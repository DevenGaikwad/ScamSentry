# Hyperparameter search and execution record

Protocol `ss-exploratory-v2`; plan and configuration digests are frozen in the run preregistration. Eight predeclared configurations, 3 train-only StratifiedGroupKFold folds, seed 42. Every fold refits vocabulary, IDF and LR on training parents. Variants are generated after splitting, preserving parent ownership. No fold overlap is recorded. No adaptive grid expansion occurred.

Word tokens: typed minimization → NFKC/ASCII case/fixed whitespace → `[a-z]+(?:'[a-z]+)?`, tokens of at least 2 ASCII characters; unigram and adjacent bigram features joined by one space. Smoothed train-only IDF, sublinear TF, L2 block norm. Character features preserve the archived code-point 3–5 contract. Hybrid blocks are normalized separately and multiplied by explicit weights; **no final L2 normalization** is added. Research schema 2 records vocabulary order, each IDF and weight, coefficient offsets, calibration and contract versions.

| Candidate | C | Class weight | min_df | Block vocabulary budget | Augmentation severity / block weights | Train-CV macro-F1 | Constraint eligible |
|---|---:|---|---:|---:|---|---:|---|
| word_c1 | 1 | balanced | 2 | 15000 | none | 0.8762 | True |
| word_c4 | 4 | balanced | 2 | 20000 | none | 0.8839 | True |
| word_unweighted | 4 | None | 1 | 20000 | none | 0.8625 | False |
| char_robust_c2 | 2 | balanced | 2 | 20000 | 0.15 | 0.8739 | False |
| char_robust_c4 | 4 | balanced | 2 | 20000 | 0.3 | 0.8857 | True |
| hybrid_c1 | 1 | balanced | 2 | 20000 | [0.65, 0.35] | 0.8474 | False |
| hybrid_c4 | 4 | balanced | 2 | 20000 | [0.5, 0.5] | 0.8772 | True |
| hybrid_word_heavy | 4 | None | 2 | 20000 | [0.35, 0.65] | 0.8595 | False |

Run 02 wall time: 142.39 seconds, peak process RSS 230,188 KiB. These are host-process measurements. Environment: Python 3.12.14, scikit-learn 1.8.0, NumPy 2.3.5, 2 CPU threads. Calibration fits only the 574-record/541-group calibration partition; temperature search is bounded exp(-2)…exp(2). Public prediction files contain source IDs, group IDs, labels and estimates, with no original text. Model byte sizes come from actual JSON exports. Phone loading, inference and memory remain pending.

The executed script digest is `701467a67ed56d6acb3f25a34a49e50f6381866b3e3f467cae9b9ed9a14340ff`. Source base commit `499fa7879d499a715306424d0e95da2b2c554158` was accompanied by the explicitly recorded dirty implementation; the public commit contains the script and frozen reports. No claim of a clean pre-existing commit was made.
