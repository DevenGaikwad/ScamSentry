"""Generate v2 tables and charts from frozen reports. Does not reopen data."""
from pathlib import Path
import json
ROOT=Path(__file__).resolve().parents[1];RUN=ROOT/'ml/artifacts/v2/exploratory_20261116_02'
s=json.loads((RUN/'summary.json').read_text());search=json.loads((RUN/'search.json').read_text());results=s['results']
def p(x):return 'undefined' if x is None else f'{100*x:.2f}%'
def f(x):return 'undefined' if x is None else f'{x:.4f}'
rows=[]
for r in results:
    c=r['smishing'];ci=r['paired_vs_baseline']['macro_f1_difference_95ci']
    rows.append(f"| {r['name']} | {p(r['macro_f1'])} | {p(r['accuracy'])} | {c['TP']} | {c['FP']} | {c['TN']} | {c['FN']} | {p(c['precision'])} | {p(c['recall'])} | {p(c['specificity'])} | {p(c['FPR'])} | {p(c['FNR'])} | {p(c['f1'])} | {p(r['noise_macro_f1'])} | {r['model_bytes']:,} |")
text='''# V2 model comparison — exploratory, no model promotion

Generated from `ml/artifacts/v2/exploratory_20261116_02/summary.json`. Same development units: **861 records, 813 designated groups**, with 725 ordinary, 61 promotion/spam and 75 smishing-like records. This is historical development evidence, not a blind or contemporary external test. No old final-test prediction was rerun for this comparison. The JSON container for processed data was loaded to select allowed memberships; historical test rows were excluded before feature fitting, predictions or metric calculation. The full-pipeline comparison uses reviewed original development texts, recovered by source row. Model-only inputs use typed minimization.

Rows below are model-only. Positive class for counts is smishing-like versus both other classes. Noise is the severe synthetic all-o→0/all-l→1 slice; it is not actual OCR accuracy. Accuracy is multiclass accuracy. Size is exported research JSON, not an installed APK or measured heap.

| Candidate | Macro-F1 | Accuracy | TP | FP | TN | FN | Precision | Recall | Specificity | FPR | FNR | Smishing F1 | Noise macro-F1 | JSON bytes |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
'''+ '\n'.join(rows)
text+='\n\nThe strongest development word configuration reaches '+p(results[1]['macro_f1'])+' macro-F1 versus '+p(results[0]['macro_f1'])+' for the preserved baseline, an absolute '+f(results[1]['macro_f1']-results[0]['macro_f1'])+' score change (2.43 percentage points). That configuration loses substantial quality under the severe substitution slice. Noise-trained character models improve that slice while reducing clean development macro-F1 and smishing recall. None satisfies all promotion conditions.\n\n'
text+='Training-only grouped CV selects **'+s['cv_selected']+'**, demonstrating that CV ranking and the known development ranking differ. We do not select a deployment champion retrospectively from the most attractive table row. Keep `ss-char-lr-0.1.0` as the approved portable baseline. Actual device resource evidence and prospective validation remain unavailable.\n\n'
text+='| Candidate | Paired group bootstrap Δ macro-F1 95% CI | Pipeline coverage | Ordinary high warnings |\n|---|---:|---:|---:|\n'
for r in results:
    ci=r['paired_vs_baseline']['macro_f1_difference_95ci'];report=json.loads((RUN/f"{r['name']}.json").read_text());full=report['full_pipeline']
    text+=f"| {r['name']} | {ci[0]:+.4f} to {ci[1]:+.4f} | {p(r['coverage'])} | {full['ordinary_high']}/{full['ordinary_support']} ({p(r['ordinary_high_rate'])}) |\n"
text+='''
Paired percentile intervals use the same development group resamples for both predictions, 300 replicates, seed 42. They capture sampling variation under the designated grouping assumption, not the full uncertainty from historical data reuse, multiple comparisons or source shift. Do not treat a barely positive interval as independent validation.

Coverage here means final category other than `unable`; the historical v1 report defined acceptance more narrowly (higher/lower only). These denominators must not be mixed. Each per-candidate report stores full-pipeline counts, deferrals by class, total-population automatic smishing-high recall and accepted/abstained argmax error. Original-input deterministic observations can dominate high warnings regardless of the selected model. A high category is an indicator, not a confirmed malicious sender.

Threshold tables separately examine model-led confidence/margin gating and smishing score thresholds. They are not blended authenticity probabilities. Illustrative costs were declared before running; these costs do not estimate monetary harm. The shipped .62/.18 operating settings remain unchanged because this exploration does not justify a production threshold promotion.

![Frozen development trade-offs](../assets/evidence/v2/model_tradeoffs.png)

The first execution (`exploratory_20261116_01`) stopped after a search-register serialization error. Its generated baseline and first word report remain preserved. The retry changed the NumPy boolean serialization only; the declared grid and scientific questions did not change. Run 02 completed every declared configuration. Inspect the experiment register for warnings and per-fold scores. Research coefficients are reproducible but are not bundled as a shipping fallback. A separate tiny fictional fit/export test verifies real scikit-learn arithmetic without distributing raw data or executable private checkpoints.
'''
(ROOT/'docs/MODEL_COMPARISON.md').write_text(text)
cv='''# Hyperparameter search and execution record

Protocol `ss-exploratory-v2`; plan and configuration digests are frozen in the run preregistration. Eight predeclared configurations, 3 train-only StratifiedGroupKFold folds, seed 42. Every fold refits vocabulary, IDF and LR on training parents. Variants are generated after splitting, preserving parent ownership. No fold overlap is recorded. No adaptive grid expansion occurred.

Word tokens: typed minimization → NFKC/ASCII case/fixed whitespace → `[a-z]+(?:\'[a-z]+)?`, tokens of at least 2 ASCII characters; unigram and adjacent bigram features joined by one space. Smoothed train-only IDF, sublinear TF, L2 block norm. Character features preserve the archived code-point 3–5 contract. Hybrid blocks are normalized separately and multiplied by explicit weights; **no final L2 normalization** is added. Research schema 2 records vocabulary order, each IDF and weight, coefficient offsets, calibration and contract versions.

| Candidate | C | Class weight | min_df | Block vocabulary budget | Augmentation severity / block weights | Train-CV macro-F1 | Constraint eligible |
|---|---:|---|---:|---:|---|---:|---|
'''
for r in search:
 c=r['config'];cv+=f"| {c['name']} | {c['C']} | {c['class_weight']} | {c['min_df']} | {c['budget']} | {c.get('noise',c.get('weights','none'))} | {r.get('mean_macro_f1',0):.4f} | {r.get('eligible')} |\n"
cv+=f"\nRun 02 wall time: {s['wall_seconds']:.2f} seconds, peak process RSS {s['peak_process_rss_kib']:,} KiB. These are host-process measurements. Environment: Python {s['environment']['python']}, scikit-learn {s['environment']['sklearn']}, NumPy {s['environment']['numpy']}, 2 CPU threads. Calibration fits only the 574-record/541-group calibration partition; temperature search is bounded exp(-2)…exp(2). Public prediction files contain source IDs, group IDs, labels and estimates, with no original text. Model byte sizes come from actual JSON exports. Phone loading, inference and memory remain pending.\n\nThe executed script digest is `{s['code_sha256']}`. Source base commit `{s['source_commit']}` was accompanied by the explicitly recorded dirty implementation; the public commit contains the script and frozen reports. No claim of a clean pre-existing commit was made.\n"
(ROOT/'docs/HYPERPARAMETER_SEARCH.md').write_text(cv)
(ROOT/'docs/METRIC_DEFINITIONS.md').write_text('''# Metric definitions and denominator policy

The label order is ordinary, promotional-or-spam, smishing-like. Confusion matrix rows are actual classes and columns predictions. For each one-versus-rest class, TP=diagonal, FN=remaining actual row, FP=remaining predicted column, TN=N−TP−FP−FN.

| Metric | Definition |
|---|---|
| Precision | TP/(TP+FP) |
| Recall / sensitivity | TP/(TP+FN) |
| Specificity | TN/(TN+FP) |
| FPR | FP/(FP+TN) |
| FNR | FN/(FN+TP) |
| NPV | TN/(TN+FN) |
| F1 | 2TP/(2TP+FP+FN) |
| F2 | 5TP/(5TP+4FN+FP) |
| Multiclass accuracy | sum of diagonal / N |
| Macro-F1 | unweighted mean across the fixed three labels |
| Weighted-F1 | class F1 weighted by actual support |
| Micro-F1 | equals accuracy for this single-label multiclass task |
| Balanced accuracy | mean recall across the fixed three labels |
| MCC | standard multiclass Matthews correlation coefficient |
| Brier | mean sum of squared probability error across all 3 classes |
| Log loss | three-class natural-log loss, sklearn clipping convention |
| ECE | 10 equal-width top-confidence bins, weighted absolute accuracy−confidence gap |

Undefined individual ratios are JSON `null`; fixed-label macro averages substitute zero for an undefined class term. An absent class is never dropped silently. No predicted positives does not imply perfect precision. Tests cover a known confusion matrix, no predicted positives, absent actual classes, imbalance and a failed minority classifier.

Full-pipeline acceptance means `risk != unable`; checking remains a useful completed observation. Accepted/abstained errors refer to model argmax and retain separate counts. Smishing automatic-high recall counts every actual positive in the denominator, including deferred positives as unresolved automatic detections. Ordinary→smishing argmax, ordinary→any-nonordinary argmax and ordinary→higher-risk product warnings have different numerators and remain separate.

Group bootstrap is paired when comparing candidates, uses designated template components, and does not establish independence beyond the grouping assumptions. Controlled text perturbations are synthetic slices; screenshot OCR accuracy and current Indian campaign performance are not inferred from them.
''')
try:
 import matplotlib;matplotlib.use('Agg')
 import matplotlib.pyplot as plt
 plt.rcParams.update({'font.family':'DejaVu Sans','font.size':10})
 chosen=[results[i] for i in [0,1,4,5,7]];labels=['Archived char','Word C1','Robust char C2','Robust char C4','Hybrid C4']
 fig,axes=plt.subplots(1,2,figsize=(13,4.8),layout='constrained')
 colors=['#487ccf','#8960cb','#d19a35','#bc7635','#358f96']
 for ax,key,title in [(axes[0],'macro_f1','Clean development macro-F1'),(axes[1],'noise_macro_f1','Severe synthetic substitution macro-F1')]:
  bars=ax.barh(labels,[r[key] for r in chosen],color=colors,height=.6);ax.set_xlim(0,1.06);ax.invert_yaxis();ax.set_title(title,loc='left',fontweight='bold');ax.grid(axis='x',alpha=.18);ax.set_axisbelow(True)
  for bar,r in zip(bars,chosen):ax.text(bar.get_width()+.012,bar.get_y()+bar.get_height()/2,f'{r[key]:.3f}',va='center',fontsize=9)
  ax.spines[['top','right']].set_visible(False)
 fig.suptitle('ScamSentry v2 · Same 861 historical development records / 813 groups',fontsize=13,fontweight='bold')
 path=ROOT/'assets/evidence/v2';path.mkdir(parents=True,exist_ok=True);fig.savefig(path/'model_tradeoffs.png',dpi=180,bbox_inches='tight');plt.close(fig)
except ImportError: print('Optional matplotlib unavailable; JSON/Markdown reports generated')
print('V2 research reports generated from frozen aggregates')
