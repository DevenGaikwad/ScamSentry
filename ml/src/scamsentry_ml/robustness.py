import argparse
from collections import defaultdict
import numpy as np
from .common import ROOT, config, read_json, write_json
from .contracts import PortableModel, normalize
from .metrics import measure
from .train import load_partitions


def main():
    p = argparse.ArgumentParser(description="Frozen held-out perturbation audit; no model tuning")
    p.add_argument("--config", required=True)
    a = p.parse_args()
    cfg = config(a.config)
    model = PortableModel(read_json(ROOT / cfg["model"]))
    test = load_partitions()["test"]
    transformations = {"case": str.upper, "spacing": lambda t: t.replace(" ", "  \t"), "zero_width": lambda t: t.replace(" ", "\u200b "), "ocr_substitution": lambda t: t.replace("o", "0").replace("l", "1")}
    result = {}
    for name, transform in transformations.items():
        probs = [model.predict(transform(r["text"]))["probabilities"] for r in test]
        result[name] = measure([r["label"] for r in test], probs, bootstrap=0)
    write_json(ROOT / "artifacts/robustness.json", dict(parent_partition="frozen test", independent_parent_groups=len({r["group_id"] for r in test}), synthetic_perturbations=True, limitations="OCR substitutions are text perturbations, not measured OCR extraction. Meaning-preservation not independently annotated.", families=result))
    print({k: v["macro_f1"] for k, v in result.items()})


if __name__ == "__main__":
    main()
