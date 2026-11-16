"""Generate readable reports from frozen aggregate measurements, without reopening test data."""
from pathlib import Path
import hashlib
import json
import math

root=Path(__file__).resolve().parents[1]
run=root/'ml/artifacts/runs/baseline_char_lr_001'
def read(p): return json.loads(p.read_text())
r=read(run/'final_test.json'); d=read(run/'development.json'); manifest=read(root/'ml/artifacts/bundle/manifest.json');research=read(root/'ml/artifacts/research_comparison.json');comparison=read(run/'comparison.json');rob=read(root/'ml/artifacts/robustness.json')
def pct(n):return f'{100*n:.2f}%'
def wilson(k,n):
    z=1.959963984540054;p=k/n;den=1+z*z/n;mid=(p+z*z/(2*n))/den;half=z*math.sqrt(p*(1-p)/n+z*z/(4*n*n))/den
    return [max(0,mid-half),min(1,mid+half)]
ci=wilson(1,724)
classrows='\n'.join(f"| {label} | {int(r['class_report'][str(i)]['support'])} | {pct(r['class_report'][str(i)]['precision'])} | {pct(r['class_report'][str(i)]['recall'])} | {r['class_report'][str(i)]['f1-score']:.3f} |" for i,label in enumerate(['Ordinary','Promotion / spam','Smishing-like']))
comprows='\n'.join(f"| {c['candidate']} | {c['development_macro_f1']:.3f} |" for c in comparison)
robrows='\n'.join(f"| {name} | {value['macro_f1']:.3f} |" for name,value in rob['families'].items())
text=f'''# Actual evaluation results — research preview

Frozen model `{manifest['model_id']}`. Protocol `ss-group-v1`. Final-test artifact SHA-256 `{r['model_sha256']}`. All figures below are from saved aggregate files; this report does not rerun or retune the test.

The held-out set contains 861 records in 813 designated groups. Macro-F1 is **{r['macro_f1']:.3f}**, with 95% group-bootstrap interval **{r['group_bootstrap_macro_f1_95ci'][0]:.3f}–{r['group_bootstrap_macro_f1_95ci'][1]:.3f}** (300 percentile resamples, group unit, seed 42). Accuracy is {pct(r['accuracy'])}. This is historical single-source performance, not current scam-prevention effectiveness.

| Source class | Support | Precision | Recall | F1 |
|---|---:|---:|---:|---:|
{classrows}

Smishing-like detection finds 65/75 source-labelled examples; 65/69 smishing predictions are correct. Ordinary→smishing is 1/724 ({pct(1/724)}), with supplementary record-binomial Wilson interval {pct(ci[0])}–{pct(ci[1])}. Ordinary→any-nonordinary is 8/724. Group dependence and source shift limit interpretation of these intervals. Complete policy higher-risk warnings are 0/724 ordinary benchmark records; this differs from model argmax, uses minimized corpus text and must not become a claim of zero app false alarms.

Multiclass log loss: {r['log_loss']:.5f}. Brier: {r['brier_multiclass_sum']:.5f}, defined as mean summed squared error across three classes. ECE: {r['ece_10_equal_width']:.5f}, 10 equal-width confidence bins. Smishing average precision: {r['smishing_average_precision']:.5f}; ROC-AUC: {r['smishing_roc_auc']:.5f}. Average precision is not trapezoidal PR-AUC.

Selective coverage (higher/lower accepted, checking/unable abstained): {pct(r['selective_coverage'])}. Accepted argmax error: {pct(r['accepted_error'])}; abstained error: {pct(r['abstained_error'])}. Abstention is not a complete unfamiliar-input detector. Calibration temperature is fitted on 574 separate calibration records, not final test.

## Development comparisons

| Candidate | Development macro-F1 |
|---|---:|
{comprows}
| Character-feature MLP, best early-stopped epoch | {research['neural']['best_development_macro_f1']:.3f} |

The deployed character LR C2 is the strongest deployable character LR in the bounded C search. Alternative feature families remain separate research comparisons, not hidden losers. The direct Unicode character runtime has parity/integrity and maintenance advantages. Three group-aware folds within train average {sum(x['macro_f1'] for x in research['group_cv'])/3:.3f}. The neural alternative has {research['neural']['parameters']:,} parameters and no mobile export claim. The learning curve uses one seed and cannot quantify full variability.

## Robustness and error interpretation

| Frozen held-out perturbation | Macro-F1 |
|---|---:|
{robrows}

Case and configured whitespace normalization preserve performance. Zero-width insertion reduces it; severe o→0/l→1 substitutions cause a large failure. These are controlled synthetic text variants, not measured OCR accuracy. No parameter or threshold was retuned against these final-test failures. The editable OCR step and visible uncertainty are necessary; they do not eliminate confident transcription mistakes. A new independent protocol is required before claiming an improved model.

Promotion/smishing confusion is a significant remaining error source: 9/75 smishing examples predict promotion, while 3/62 promotional examples predict smishing. One smishing record predicts ordinary. There is no independent contemporary external holdout or temporal generalization test. UCI overlaps the primary corpus substantially and is not an external validation source.

Runtime in this report is the Linux Python CPU reference only: warm p50 {r['runtime']['warm_p50_ms']:.2f} ms, p95 {r['runtime']['warm_p95_ms']:.2f} ms. It cannot be reported as phone latency. Actual Android execution, cold loading, OCR, memory, physical-device performance, fresh offline behavior and usability have independent gates.

![Frozen confusion matrix](../assets/evidence/confusion_matrix.png)

![Controlled robustness](../assets/evidence/robustness.png)

![Calibration reliability](../assets/evidence/calibration.png)
'''
(root/'docs/EVALUATION_RESULTS.md').write_text(text)
card=f'''# Model card: {manifest['model_id']}

Research preview for English short-message resemblance, not authentication or guaranteed scam detection. Owner/integration: Deven Sachin Gaikwad. Version 0.1.0. Prepared 16 November 2026.

- Task/class order: ordinary, promotional-or-spam, smishing-like. Source annotations do not establish an individual sender's intent.
- Source: Mishra and Soni, Mendeley Data V1, DOI 10.17632/f45bkkt8pr.1, CC BY 4.0. Original corpus 5,971 records; 5,740 retained after conflict quarantine and duplicate reduction. No raw messages distributed.
- Feature contract: typed token minimization, NFKC, ASCII lowercasing, fixed whitespace, overlapping Unicode codepoint n-grams 3–5, sublinear TF, train-only smoothed IDF, L2 norm. Vocabulary is bounded at 20,000 entries.
- Model: class-balanced regularized multinomial logistic regression C2, scalar temperature {read(run/'run.json')['temperature']:.8f}, ordinal policy confidence .62 and margin .18. No combined safety percentage.
- Validation: historical grouped test 861 records/813 groups; macro-F1 {r['macro_f1']:.3f}, smishing precision {pct(r['class_report']['2']['precision'])}, recall 65/75. See EVALUATION_RESULTS.md for confidence intervals, calibration, class errors and robustness failure.
- Bundle SHA-256: `{manifest['model_sha256']}`. JSON size {manifest['byte_size']:,} bytes. The Android asset and reference bundle are checked byte-for-byte.
- Runtime: direct Kotlin sparse accumulation; no executable pickle, remote service or bundled neural runtime. Host JVM and device parity have separate recorded statuses.
- Limitations: historical/partly overlapping sources, unknown record dates/campaigns, script check is not robust language identification, Romanized/mixed language unsupported, no current Indian scam coverage claim, poor severe character-obfuscation robustness, no live URL reputation. Calibration may shift outside the benchmark.
- Product mitigation: reviewable input, editable OCR, explicit observations/uncertainty, no automatic URL opening, summary-only optional history and redacted export. Model corruption must never become a lower-risk result.
- Data privacy: numeric/email/URL strings minimized before vocabulary learning. Names/fragments may remain; this is not differential privacy. Raw/processed records and trusted developer joblib checkpoints are excluded from release.
- Licence and intended use: original code MIT; source-data-derived assets retain accompanying CC BY attribution and applicable third-party terms. Academic/research preview; no unsupported certification or medical/financial authenticity claim.

See the gate report for what was actually verified. This card must not be used to infer that physical-device or public-release gates passed.
'''
(root/'docs/MODEL_CARD.md').write_text(card)
(root/'ml/artifacts/uncertainty_summary.json').write_text(json.dumps({'ordinary_model_smishing_fpr':{'count':1,'denominator':724,'wilson_95':ci},'smishing_recall':{'count':65,'denominator':75,'wilson_95':wilson(65,75)},'smishing_precision':{'count':65,'denominator':69,'wilson_95':wilson(65,69)},'warning':'Record-binomial supplements; designated group-bootstrap macro-F1 remains the grouped summary.'},indent=2)+'\n')
print('Generated model card, evaluation report and supplementary uncertainty from saved aggregates')
