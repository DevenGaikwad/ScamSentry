# ScamSentry v4 model comparison results

[Preregistered protocol](ML_V4_EXPERIMENT.md)

## Observed result

All 12 planned fold fits completed. None met the 82.00% training-fold smishing recall floor. The held-out development table therefore contains only the shipping baseline, as preregistered. The old test was not scored. The criteria and hashes are in the [experiment record](../ml/artifacts/v4/robust_hybrid_20261118_01/preregistration.json).

| Candidate | Clean CV macro F1 | Mildly perturbed CV macro F1 | Clean CV smishing recall | Ordinary to smishing |
| --- | ---: | ---: | ---: | ---: |
| Character, mild noise | 88.05% | 88.32% | 80.29% | 0.14% |
| Word, mild noise | 87.95% | 85.90% | 80.61% | 0.21% |
| Equal character/word weights | 88.37% | 87.61% | 81.96% | 0.21% |
| Heavier character weight | 88.01% | 87.83% | 80.62% | 0.17% |

The best recall value is 0.819562 before rounding. No candidate was refitted, calibrated, exported or shipped. The [aggregate baseline](../ml/artifacts/v4/robust_hybrid_20261118_01/baseline.json) includes the full confusion matrix, class metrics, warning rates, calibration and robustness slices. [Fold-level results](../ml/artifacts/v4/robust_hybrid_20261118_01/cv.json) and the [decision](../ml/artifacts/v4/robust_hybrid_20261118_01/summary.json) retain the measured supports and scope. No recipient was contacted and no further source was ingested.
