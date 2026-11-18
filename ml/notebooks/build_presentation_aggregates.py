"""Build shareable training EDA and development curves from frozen inputs."""

import argparse
from pathlib import Path
import re
import unicodedata

import numpy as np
from scipy.stats import spearmanr
from sklearn.metrics import average_precision_score, confusion_matrix, roc_auc_score

from scamsentry_ml.common import CLASSES, digest, read_json, write_json
from scamsentry_ml.contracts import normalize
from scamsentry_ml.splits import verify


FEATURES = {
    "characters": "Normalized Unicode code points",
    "tokens": "Unicode word-token count",
    "distinct_token_share": "Distinct word tokens divided by word tokens",
    "punctuation_share": "Unicode punctuation code points divided by code points",
    "number_markers": "Occurrences of the numbertoken placeholder",
    "url_markers": "Occurrences of the urltoken placeholder",
}
HISTORICAL = "runs/baseline_char_lr_001/final_test.json"
DEVELOPMENT = "v2/exploratory_20261116_02"


def features(minimized_text):
    value = normalize(minimized_text)
    tokens = re.findall(r"\w+", value)
    return (
        len(value),
        len(tokens),
        len(set(tokens)) / max(1, len(tokens)),
        sum(unicodedata.category(char).startswith("P") for char in value) / max(1, len(value)),
        tokens.count("numbertoken"),
        tokens.count("urltoken"),
    )


def training_eda(rows, membership):
    training = [row for row in rows if membership[row["record_id"]]["partition"] == "train"]
    if not training:
        raise ValueError("The frozen train partition is empty")
    data = np.array([features(row["text"]) for row in training], dtype=np.float64)
    labels = np.array([row["label"] for row in training])
    if not np.isfinite(data).all() or (np.ptp(data, axis=0) == 0).any():
        raise ValueError("Training audit has non-finite or constant features")
    pearson = np.corrcoef(data, rowvar=False)
    spearman = spearmanr(data, axis=0).statistic
    edges = [0, 25, 50, 75, 100, 150, 200, 300, 500, max(1000, int(data[:, 0].max()) + 1)]
    counts = [np.histogram(data[labels == label, 0], bins=edges)[0].tolist() for label in range(len(CLASSES))]
    if sum(map(sum, counts)) != len(training):
        raise ValueError("Length histogram does not cover the train partition")
    return {
        "partition": "train",
        "records": len(training),
        "groups": len({row["group_id"] for row in training}),
        "class_order": CLASSES,
        "feature_names": list(FEATURES),
        "feature_definitions": FEATURES,
        "pearson": pearson.round(9).tolist(),
        "spearman": spearman.round(9).tolist(),
        "length_bin_edges": edges,
        "length_counts_by_class": counts,
    }


def development_ranking(predictions, expected_metrics, membership):
    expected = {key: row for key, row in membership.items() if row["partition"] == "development"}
    if (
        expected_metrics["partition"] != "development"
        or len(predictions) != len(expected)
        or {row["record_id"] for row in predictions} != set(expected)
        or any(
            row["label"] != expected[row["record_id"]]["label"]
            or row["group_id"] != expected[row["record_id"]]["group_id"]
            for row in predictions
        )
    ):
        raise ValueError("Ranking inputs do not match frozen development membership")
    labels = np.array([row["label"] for row in predictions])
    probabilities = np.array([row["probabilities"] for row in predictions])
    if (
        probabilities.shape != (len(expected), len(CLASSES))
        or not np.isfinite(probabilities).all()
        or not np.all((0 <= probabilities) & (probabilities <= 1))
        or not np.allclose(probabilities.sum(axis=1), 1)
    ):
        raise ValueError("Invalid archived class probabilities")
    if confusion_matrix(labels, probabilities.argmax(axis=1), labels=range(3)).tolist() != expected_metrics[
        "model_only"
    ]["confusion_matrix"]:
        raise ValueError("Score snapshot disagrees with its archived report")

    positive = labels == 2
    support = int(positive.sum())
    scores = probabilities[:, 2]
    # Fixed, public grid. The exact summary scores use all archived scores.
    threshold = np.linspace(0, 1, 101)
    detected = scores[np.newaxis, :] >= threshold[:, np.newaxis]
    true_positive = detected[:, positive].sum(axis=1)
    false_positive = detected[:, ~positive].sum(axis=1)
    flagged = true_positive + false_positive
    precision = np.divide(true_positive, flagged, out=np.ones(101), where=flagged > 0)
    return {
        "partition": "development",
        "model_id": expected_metrics["model_id"],
        "records": len(labels),
        "positive_class": CLASSES[2],
        "positive_support": support,
        "roc_auc": float(roc_auc_score(positive, scores)),
        "average_precision": float(average_precision_score(positive, scores)),
        "threshold": threshold.round(2).tolist(),
        "false_positive_rate": (false_positive / (len(labels) - support)).round(9).tolist(),
        "recall": (true_positive / support).round(9).tolist(),
        "precision": precision.round(9).tolist(),
    }


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--project-root", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    ml = args.project_root / "ml"
    artifacts = ml / "artifacts"
    split_path = artifacts / "splits.json"
    data_path = ml / "data/processed/messages.json"
    public_baseline = read_json(artifacts / HISTORICAL)
    splits = verify(split_path)
    if digest(split_path) != public_baseline["split_sha256"] or digest(data_path) != splits["processed_digest"]:
        raise ValueError("Frozen split or processed data SHA-256 does not match")
    rows = read_json(data_path)
    membership = {row["record_id"]: row for row in splits["membership"]}
    if len(rows) != len(membership) or any(
        row["label"] != membership[row["record_id"]]["label"]
        or row["group_id"] != membership[row["record_id"]]["group_id"]
        for row in rows
    ):
        raise ValueError("Processed rows disagree with the frozen manifest")
    predictions_path = artifacts / DEVELOPMENT / "archived_baseline_predictions.json"
    archived = read_json(artifacts / DEVELOPMENT / "archived_baseline.json")
    if archived["model_id"] != "ss-char-lr-0.1.0":
        raise ValueError("Unexpected score-snapshot model")
    aggregate = {
        "schema_version": 1,
        "protocol_id": splits["protocol_id"],
        "processed_sha256": splits["processed_digest"],
        "split_sha256": public_baseline["split_sha256"],
        "model_sha256": public_baseline["model_sha256"],
        "development_score_snapshot_sha256": digest(predictions_path),
        "training": training_eda(rows, membership),
        "development_ranking": development_ranking(read_json(predictions_path), archived, membership),
    }
    write_json(args.output, aggregate)
    print("Wrote aggregate training statistics and development ranking curves.")


if __name__ == "__main__":
    main()
