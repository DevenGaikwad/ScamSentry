# Metric definitions and denominator policy

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
