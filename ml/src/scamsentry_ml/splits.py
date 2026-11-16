import argparse
from collections import Counter
from sklearn.model_selection import StratifiedGroupKFold
from .common import ROOT, config, digest, read_json, write_json


def verify(manifest):
    value = read_json(manifest)
    ids, groups, digests = {}, {}, {}
    for row in value["membership"]:
        for field, seen in (("record_id", ids), ("group_id", groups), ("normalized_digest", digests)):
            key = row[field]
            if key in seen and seen[key] != row["partition"]:
                raise ValueError(f"Partition leakage in {field}")
            seen[key] = row["partition"]
    if set(ids.values()) != {"train", "development", "calibration", "test"}:
        raise ValueError("Missing partition")
    return value


def create(cfg):
    records = read_json(ROOT / cfg["input"])
    labels, groups = [r["label"] for r in records], [r["group_id"] for r in records]
    folds = StratifiedGroupKFold(n_splits=20, shuffle=True, random_state=cfg["seed"])
    membership = []
    names = ["train"] * 12 + ["development"] * 3 + ["calibration"] * 2 + ["test"] * 3
    for i, (_, test) in enumerate(folds.split(records, labels, groups)):
        for j in test:
            row = records[j]
            membership.append({k: row[k] for k in ("record_id", "group_id", "normalized_digest", "label")} | {"partition": names[i]})
    report = dict(schema_version=1, protocol_id=cfg["protocol_id"], seed=cfg["seed"], processed_digest=digest(ROOT / cfg["input"]), grouping="exact + minimized template + top12 char4 cosine candidates verified Jaccard>=0.80", membership=membership)
    write_json(ROOT / cfg["output"], report)
    verify(ROOT / cfg["output"])
    stats = {}
    for name in set(names):
        part = [r for r in membership if r["partition"] == name]
        stats[name] = dict(records=len(part), groups=len({r["group_id"] for r in part}), classes=dict(Counter(r["label"] for r in part)))
    write_json(ROOT / "artifacts/split_summary.json", stats)
    print(stats)


def main():
    p = argparse.ArgumentParser(description="Create and verify frozen group partitions")
    p.add_argument("action", choices=["create", "verify"])
    p.add_argument("--config")
    p.add_argument("--manifest")
    a = p.parse_args()
    if a.action == "create":
        if not a.config:
            p.error("--config required")
        create(config(a.config))
    else:
        if not a.manifest:
            p.error("--manifest required")
        verify(a.manifest)
        print("No record, normalized-text or designated group leakage")


if __name__ == "__main__":
    main()
