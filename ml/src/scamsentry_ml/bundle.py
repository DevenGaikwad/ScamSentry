import argparse
import math
from pathlib import Path
from .common import CLASSES, digest, read_json


def validate(bundle):
    model = read_json(Path(bundle) / "model.json")
    manifest = read_json(Path(bundle) / "manifest.json")
    if digest(Path(bundle) / "model.json") != manifest["model_sha256"]:
        raise ValueError("Model digest mismatch")
    if model["schema_version"] != 1 or model["normalizer"] != "nfkc_ascii_ws_v1" or model["privacy_transform"] != "typed_tokens_v1" or model["classes"] != CLASSES:
        raise ValueError("Unsupported model contract")
    n = len(model["vocabulary"])
    if not 1 <= n <= 60000 or len(set(model["vocabulary"])) != n:
        raise ValueError("Invalid vocabulary")
    if any(not isinstance(t,str) or not 3<=len(t)<=5 for t in model["vocabulary"]):
        raise ValueError("Invalid character feature")
    if len(model["idf"]) != n or len(model["coefficients"]) != 3 or any(len(row) != n for row in model["coefficients"]) or len(model["intercept"]) != 3:
        raise ValueError("Invalid parameter shape")
    values = model["idf"] + model["intercept"] + sum(model["coefficients"], []) + [model["temperature"]]
    if not all(math.isfinite(v) for v in values) or model["temperature"] <= 0 or any(v <= 0 for v in model["idf"]):
        raise ValueError("Invalid numeric parameters")
    if model["ngram_range"] != [3, 5] or not model["sublinear_tf"] or not model["l2_normalize"]:
        raise ValueError("Feature contract mismatch")
    return model


def main():
    p = argparse.ArgumentParser(description="Validate non-executable model bundle")
    p.add_argument("action", choices=["validate"])
    p.add_argument("--bundle", required=True, type=Path)
    a = p.parse_args()
    model = validate(a.bundle)
    print(f"Valid {model['model_id']}: {len(model['vocabulary'])} features")


if __name__ == "__main__":
    main()
