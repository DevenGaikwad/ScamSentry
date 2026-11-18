"""Run the v4 frozen grouped comparison and publish aggregate evidence only."""
import argparse
from collections import Counter
import csv
import hashlib
import platform
import resource
import signal
import subprocess
import time
from pathlib import Path

import numpy as np
import sklearn
from scipy.optimize import minimize_scalar
from scipy.special import softmax
from sklearn.metrics import log_loss
from sklearn.model_selection import StratifiedGroupKFold
from threadpoolctl import threadpool_limits

from .analysis_v2 import analyze
from .blocks_v2 import PortableBlocks, export_blocks
from .common import ROOT, digest, read_json, write_json
from .contracts import PortableModel, normalize, privacy_transform
from .metrics_v2 import classification, pipeline
from .optimize_v2 import augment, fit, paired_ci, thresholds
from .sources import validate_sources
from .splits import verify

PARTS = ("train", "development", "calibration")
MODEL = ROOT / "artifacts/bundle/model.json"
SPLITS = ROOT / "artifacts/splits.json"
RAW = ROOT / "data/raw/Dataset_5971.csv"


def load_partitions():
    registry = {source["source_id"]: source for source in validate_sources()}
    source = registry["mendeley_smishing_v1"]
    if source["eligibility_status"] != "eligible" or digest(RAW) != source["download_sha256"]:
        raise ValueError("Expected the reviewed original Mendeley CSV")
    membership = {
        row["record_id"]: row for row in verify(SPLITS)["membership"]
        if row["partition"] in PARTS
    }
    parts = {p: [] for p in PARTS}
    actual = {"ham": 0, "spam": 1, "smishing": 2}
    with RAW.open(encoding="utf-8-sig", newline="") as stream:
        for index, raw in enumerate(csv.DictReader(stream)):
            member = membership.get(f"mendeley_v1_{index:05d}")
            if member is None:
                continue
            text = raw["TEXT"]
            actual_digest = hashlib.sha256(normalize(text).encode()).hexdigest()
            if actual_digest != member["normalized_digest"] or actual[raw["LABEL"].strip().lower()] != member["label"]:
                raise ValueError("Frozen row identity mismatch")
            partition = member["partition"]
            parts[partition].append((privacy_transform(text), member["label"], member["group_id"],
                                     text if partition == "development" else None))
    if sum(map(len, parts.values())) != len(membership):
        raise ValueError("Missing frozen records")
    return parts


def view(rows):
    return [row[0] for row in rows], np.array([row[1] for row in rows]), np.array([row[2] for row in rows])


def measure(model, rows):
    texts, y, _ = view(rows)
    probabilities = np.array([model.predict(text)["probabilities"] for text in texts])
    pred = probabilities.argmax(1)
    robustness = {}
    for name, transform in {
        "uppercase": str.upper,
        "extra_spacing": lambda text: text.replace(" ", "  "),
        "zero_width": lambda text: text.replace(" ", "\u200b "),
        "severe_substitution": lambda text: text.replace("o", "0").replace("l", "1"),
    }.items():
        scores = np.array([model.predict(transform(text))["probabilities"] for text in texts])
        robustness[name] = classification(y, scores.argmax(1), scores)
    samples = []
    for text in texts:
        start = time.perf_counter_ns()
        model.predict(text)
        samples.append((time.perf_counter_ns() - start) / 1_000_000)
    risks = [analyze(row[3], model)["risk"] for row in rows]
    return {
        "model_only": classification(y, pred, probabilities),
        "ordinals": pipeline(y, risks, pred),
        "robustness": robustness,
        "threshold_exploration": thresholds(y, probabilities),
        "host_python_ms": dict(p50=float(np.median(samples)), p95=float(np.quantile(samples, .95)), n=len(samples)),
    }, pred


def run(config_path):
    config = read_json(config_path)
    start = time.perf_counter()
    run_id = config["run_id"]
    out = ROOT / "artifacts/v4" / run_id
    out.mkdir(parents=True, exist_ok=False)
    plan = ROOT.parent / "docs/ML_V4_EXPERIMENT.md"
    write_json(out / "preregistration.json", {
        "config": config, "config_sha256": digest(config_path), "plan_sha256": digest(plan),
        "runner_sha256": digest(__file__), "fit_helper_sha256": digest(Path(__file__).with_name("optimize_v2.py")),
        "scope": "Preregistered before fits. Known development set is exploratory.",
        "splits_sha256": digest(SPLITS), "baseline_sha256": digest(MODEL),
        "source_base": subprocess.check_output(["git", "rev-parse", "HEAD"], cwd=ROOT, text=True).strip(),
    })
    parts = load_partitions()
    train, dev, cal = (parts[p] for p in PARTS)
    x, y, groups = view(train)
    tx, ty, tg = view(dev)
    cx, cy, _ = view(cal)
    rng = config["seed"]
    folds = list(StratifiedGroupKFold(n_splits=config["folds"], shuffle=True, random_state=rng).split(x, y, groups))
    count = 0
    search = []
    attempts = []

    def fit_once(candidate, inputs, labels, fold):
        nonlocal count
        if (count >= config["maximum_fits"] or time.perf_counter() - start >= config["wall_seconds"]
                or resource.getrusage(resource.RUSAGE_SELF).ru_maxrss > config["max_peak_rss_kib"]):
            raise TimeoutError("Predeclared experiment resource budget reached")
        count += 1
        entry = dict(number=count, candidate=candidate["name"], fold=fold, status="started")
        attempts.append(entry)
        write_json(out / "fit_attempts.json", attempts)
        try:
            result = fit(candidate, inputs, labels)
        except Exception as error:
            entry.update(status="failed", error_type=type(error).__name__)
            raise
        else:
            entry.update(status="finished", elapsed_s=result[2], warnings=result[3])
            return result
        finally:
            write_json(out / "fit_attempts.json", attempts)

    with threadpool_limits(limits=config["threads"]):
        for candidate in config["candidates"]:
            rows = []
            record = {"candidate": candidate["name"], "folds": rows, "status": "ok"}
            try:
                for fold, (i, j) in enumerate(folds):
                    assert not set(groups[i]) & set(groups[j])
                    f, lr, _, caught = fit_once(candidate, [x[k] for k in i], y[i], fold)
                    held = [x[k] for k in j]
                    clean = classification(y[j], lr.predict(f.transform(held)))
                    mild, _ = augment(held, y[j], config["cv_augmentation_eval_severity"])
                    noise = classification(y[j], lr.predict(f.transform(mild[len(held):])))
                    rows.append(dict(fold=fold, records=len(j), groups=len(set(groups[j])),
                                     class_support=dict(Counter(str(k) for k in y[j])),
                                     clean_macro_f1=clean["macro_f1"], mild_macro_f1=noise["macro_f1"],
                                     smishing_recall=clean["one_vs_rest"]["smishing-like"]["recall"],
                                     ordinary_to_smishing=clean["ordinary_to_smishing"],
                                     features=sum(len(v.vocabulary_) for _, v in f.transformer_list), warnings=caught))
                avg = {key: float(np.mean([r[key] for r in rows])) for key in
                       ("clean_macro_f1", "mild_macro_f1", "smishing_recall", "ordinary_to_smishing", "features")}
                weight = config["cv_clean_f1_weight"]
                record.update(mean=avg, score=weight * avg["clean_macro_f1"] + (1 - weight) * avg["mild_macro_f1"],
                              eligible=avg["smishing_recall"] >= config["cv_min_smishing_recall"]
                              and avg["ordinary_to_smishing"] <= config["cv_max_ordinary_to_smishing"])
            except TimeoutError:
                raise
            except Exception as error:
                record.update(status="failed", error_type=type(error).__name__, eligible=False)
            search.append(record)
            write_json(out / "cv.json", search)
        rank = sorted((r for r in search if r["eligible"]), key=lambda r: (-r["score"], r["mean"]["features"], r["candidate"]))
        baseline = PortableModel(read_json(MODEL))
        base, base_prediction = measure(baseline, dev)
        base.update(model_sha256=digest(MODEL), model_bytes=MODEL.stat().st_size)
        write_json(out / "baseline.json", base)
        selected = rank[0]["candidate"] if rank else None
        gates = {"prospective_data": "BLOCKED", "physical_device": "BLOCKED", "new_model_runtime_parity": "NOT APPLICABLE"}
        if selected is not None:
            candidate = next(item for item in config["candidates"] if item["name"] == selected)
            f, lr, _, _ = fit_once(candidate, x, y, "refit")
            logits = lr.decision_function(f.transform(cx))
            opt = minimize_scalar(lambda z: log_loss(cy, softmax(logits / np.exp(z), axis=1), labels=[0, 1, 2]),
                                  method="bounded", bounds=(-2, 2))
            if not opt.success:
                raise RuntimeError("Temperature fit did not converge")
            temperature = float(np.exp(opt.x))
            bundle = export_blocks(f, lr, temperature, "ss-research-v4-" + selected)
            private = ROOT / "artifacts/private/v4" / run_id / "model.json"
            write_json(private, bundle)
            portable = PortableBlocks(bundle)
            expected = lr.decision_function(f.transform(tx))
            max_diff = float(np.abs(np.array([portable.predict(t)["logits"] for t in tx]) - expected).max())
            if max_diff > 1e-9:
                raise ValueError("Portable reference diverges from estimator")
            report, predicted = measure(portable, dev)
            report.update(candidate=selected, temperature=temperature, export_max_logit_delta=max_diff,
                          model_bytes=private.stat().st_size, model_sha256=digest(private),
                          paired_vs_baseline=paired_ci(ty, base_prediction, predicted, tg, config["bootstrap_repetitions"]))
            write_json(out / "candidate.json", report)
            p = config["promotion"]
            new = report["model_only"]
            old = base["model_only"]
            gates.update(
                development_macro_gain=new["macro_f1"] - old["macro_f1"] >= p["minimum_macro_f1_gain"],
                development_lower_paired_ci_positive=report["paired_vs_baseline"]["macro_f1_difference_95ci"][0] > 0,
                development_recall_floor=new["one_vs_rest"]["smishing-like"]["recall"]
                >= old["one_vs_rest"]["smishing-like"]["recall"] - p["maximum_smishing_recall_drop"],
                development_ordinary_warning_ceiling=report["ordinals"]["ordinary_high_rate"] <= p["maximum_ordinary_high_rate"],
                development_severe_slice_gain=report["robustness"]["severe_substitution"]["macro_f1"]
                > base["robustness"]["severe_substitution"]["macro_f1"],
                python_export_reference="PASS", new_model_runtime_parity="BLOCKED",
            )
    summary = {
        "run": run_id, "selected_by_training_only_cv": selected, "fits": count,
        "partitions": {name: {"records": len(rows), "groups": len({r[2] for r in rows}),
                              "supports": dict(Counter(str(r[1]) for r in rows))} for name, rows in parts.items()},
        "historical_test_scored": False, "development_independent": False,
        "promotion": "RETAIN_SHIPPING_BASELINE", "gates": gates,
        "scope": "CPU Python host timings; no device or browser candidate performance claim",
        "python": platform.python_version(), "sklearn": sklearn.__version__, "numpy": np.__version__,
        "threads": config["threads"], "wall_seconds": time.perf_counter() - start,
        "peak_rss_kib": resource.getrusage(resource.RUSAGE_SELF).ru_maxrss,
    }
    write_json(out / "summary.json", summary)
    print(f"Finished {count} fits; CV selected {selected}; shipping model retained")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--config", type=Path, default=ROOT / "configs/v4_research.json")
    args = parser.parse_args()

    def expired(_signum, _frame):
        raise TimeoutError("Predeclared wall-time limit")

    signal.signal(signal.SIGALRM, expired)
    signal.alarm(read_json(args.config)["wall_seconds"])
    run(args.config)


if __name__ == "__main__":
    main()
