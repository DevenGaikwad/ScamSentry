import argparse
import time
import numpy as np
from .common import ROOT, digest, read_json, write_json
from .contracts import PortableModel, decide, observations, urls
from .metrics import measure
from .train import load_partitions


def evaluate(path, protocol):
    path = ROOT / path if not path.is_absolute() else path
    frozen = read_json(path / "frozen.json")
    if protocol != frozen["protocol_id"] or digest(path / "model.json") != frozen["model_sha256"] or digest(ROOT / "artifacts/splits.json") != frozen["split_sha256"]:
        raise ValueError("Frozen protocol/model/split guard failed")
    if (path / "final_test.json").exists():
        raise ValueError("Final test already accessed; use saved report, do not tune against it")
    model = PortableModel(read_json(path / "model.json"))
    rows = load_partitions()["test"]
    probs, categories, times = [], [], []
    for r in rows:
        tick = time.perf_counter()
        pred = model.predict(r["text"])
        times.append((time.perf_counter()-tick)*1000)
        probs.append(pred["probabilities"])
        categories.append(decide(r["text"], pred["probabilities"], observations(r["text"]), urls(r["text"]), model.bundle["policy"]))
    result = measure([r["label"] for r in rows], probs, [r["group_id"] for r in rows])
    accepted = np.array([x in ("higher", "lower") for x in categories])
    errors = np.array(probs).argmax(axis=1) != np.array([r["label"] for r in rows])
    result.update(dict(protocol_id=protocol, model_sha256=digest(path / "model.json"), split_sha256=frozen["split_sha256"], class_order=model.bundle["classes"], policy_categories={c: categories.count(c) for c in set(categories)}, selective_coverage=float(accepted.mean()), accepted_error=float(errors[accepted].mean()) if accepted.any() else None, abstained_error=float(errors[~accepted].mean()) if (~accepted).any() else None, policy_ordinary_higher_risk_rate=float(np.mean([c=="higher" for c,r in zip(categories,rows) if r["label"]==0])), runtime=dict(environment="Linux Python CPU reference, not phone", warm_p50_ms=float(np.median(times)), warm_p95_ms=float(np.quantile(times,.95))), limitations=["Historical single-source benchmark", "Grouping is heuristic; latent campaigns may still overlap", "No real-device performance evidence", "Calibration is specific to benchmark distribution"]))
    write_json(path / "final_test.json", result)
    write_json(path / "test_access.json", dict(protocol_id=protocol, model_sha256=frozen["model_sha256"], final_test_accessed=True, report="final_test.json"))
    print({k:result[k] for k in ("records","macro_f1","ordinary_fpr_smishing","selective_coverage")})


def main():
    from pathlib import Path
    p = argparse.ArgumentParser(description="One guarded final test access on frozen model")
    p.add_argument("--run", type=Path, required=True)
    p.add_argument("--protocol", required=True)
    a = p.parse_args()
    evaluate(a.run, a.protocol)


if __name__ == "__main__":
    main()
