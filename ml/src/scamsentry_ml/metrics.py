import numpy as np
from sklearn.metrics import accuracy_score, average_precision_score, brier_score_loss, classification_report, confusion_matrix, f1_score, log_loss, roc_auc_score


def measure(y, probabilities, groups=None, bootstrap=300):
    y, probs = np.asarray(y), np.asarray(probabilities)
    pred = probs.argmax(axis=1)
    confidence = probs.max(axis=1)
    bins = []
    ece = 0.
    for low in np.arange(0, 1, .1):
        mask = (confidence >= low) & (confidence < low + .1 if low < .9 else confidence <= 1)
        n = int(mask.sum())
        if n:
            acc, conf = float((pred[mask] == y[mask]).mean()), float(confidence[mask].mean())
            ece += n / len(y) * abs(acc - conf)
            bins.append(dict(lower=float(low), n=n, accuracy=acc, confidence=conf))
    result = dict(records=len(y), accuracy=accuracy_score(y, pred), macro_f1=f1_score(y, pred, average="macro", zero_division=0), class_report=classification_report(y, pred, labels=[0, 1, 2], output_dict=True, zero_division=0), confusion_matrix=confusion_matrix(y, pred, labels=[0, 1, 2]).tolist(), ordinary_fpr_smishing=float(np.mean(pred[y == 0] == 2)), ordinary_fpr_any_nonordinary=float(np.mean(pred[y == 0] != 0)), log_loss=log_loss(y, probs, labels=[0, 1, 2]), brier_multiclass_sum=float(np.mean(np.sum((probs - np.eye(3)[y]) ** 2, axis=1))), ece_10_equal_width=ece, reliability=bins, smishing_average_precision=average_precision_score(y == 2, probs[:, 2]), smishing_roc_auc=roc_auc_score(y == 2, probs[:, 2]))
    if groups is not None:
        groups = np.asarray(groups)
        unique = np.unique(groups)
        rng = np.random.default_rng(42)
        scores = []
        by_group = {g: np.flatnonzero(groups == g) for g in unique}
        for _ in range(bootstrap):
            indexes = np.concatenate([by_group[g] for g in rng.choice(unique, size=len(unique), replace=True)])
            scores.append(f1_score(y[indexes], pred[indexes], labels=[0, 1, 2], average="macro", zero_division=0))
        result["group_bootstrap_macro_f1_95ci"] = np.quantile(scores, [.025, .975]).tolist()
        result["bootstrap"] = dict(unit="designated template connected component", repetitions=bootstrap, seed=42, method="percentile")
    return result
