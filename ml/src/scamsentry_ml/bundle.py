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
    if not all(isinstance(v,(int,float)) and not isinstance(v,bool) and math.isfinite(v) for v in values) or model["temperature"] <= 0 or any(v <= 0 for v in model["idf"]):
        raise ValueError("Invalid numeric parameters")
    if model["ngram_range"] != [3, 5] or model["sublinear_tf"] is not True or model["l2_normalize"] is not True:
        raise ValueError("Feature contract mismatch")
    policy=model["policy"]
    if policy.get("version")!="ordinal_v1": raise ValueError("Unsupported policy version")
    for key in ("confidence","margin"):
        value=policy.get(key)
        if isinstance(value,bool) or not isinstance(value,(int,float)) or not math.isfinite(value) or not 0<=value<=1: raise ValueError(f"Invalid policy {key}")
    if not isinstance(model['model_id'],str) or not model['model_id'] or model['model_id']!=manifest['model_id']: raise ValueError('Model identity mismatch')
    if manifest['schema_version']!=1 or manifest['format']!='utf8-json-float64' or manifest['byte_size']!=(Path(bundle)/'model.json').stat().st_size: raise ValueError('Manifest contract mismatch')
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
