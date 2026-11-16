"""Source-specific ingestion. Raw and processed messages remain ignored by Git."""
import argparse
from collections import Counter
import csv
import hashlib
import re
from .common import ROOT, config, digest, read_json, write_json
from .contracts import normalize, privacy_transform
from .sources import validate_sources

MAPPING = {"ham": 0, "spam": 1, "smishing": 2}


def template(text):
    value = normalize(privacy_transform(text))
    return re.sub(r"[^a-z ]", "", value)


def prepare(cfg):
    sources = validate_sources()
    source = next(s for s in sources if s["source_id"] == cfg["source_id"])
    if source["eligibility_status"] != "eligible":
        raise ValueError("Training source not eligible; complete file review first")
    path = ROOT / cfg["input"]
    if digest(path) != source["download_sha256"]:
        raise ValueError("Raw file changed: checksum does not match source manifest")
    rows = []
    with path.open(encoding=cfg.get("encoding", "utf-8-sig"), newline="") as f:
        reader = csv.DictReader(f)
        required = {cfg["text_column"], cfg["label_column"]}
        if not required <= set(reader.fieldnames or []):
            raise ValueError(f"Missing columns {required}")
        for i, item in enumerate(reader):
            text, label = item[cfg["text_column"]], item[cfg["label_column"]].strip().lower()
            if label not in MAPPING:
                raise ValueError(f"Unknown label at row {i + 2}: {label!r}")
            if not text or not text.strip():
                raise ValueError(f"Empty text at row {i + 2}")
            rows.append(dict(record_id=f"mendeley_v1_{i:05d}", row=i + 2, source_id=source["source_id"], source_version="1", original_label=label, label=MAPPING[label], text=privacy_transform(text), raw_digest=hashlib.sha256(text.encode()).hexdigest(), normalized_digest=hashlib.sha256(normalize(text).encode()).hexdigest(), template=template(text), collection_date=None, language_status="script_heuristic_only", privacy_status="automated_minimization_v1"))
    if len(rows) != source["actual_rows"]:
        raise ValueError("Unexpected parsed count")
    # Union exact and near-template groups globally before splitting.
    parent = list(range(len(rows)))
    def root(i):
        while parent[i] != i:
            parent[i] = parent[parent[i]]
            i = parent[i]
        return i
    def union(a, b):
        a, b = root(a), root(b)
        if a != b:
            parent[b] = a
    for field in ("raw_digest", "normalized_digest", "template"):
        seen = {}
        for i, row in enumerate(rows):
            key = row[field]
            if key in seen:
                union(i, seen[key])
            seen[key] = i
    # Character-shingle cosine retrieval, followed by explicit Jaccard >= .80.
    from sklearn.feature_extraction.text import TfidfVectorizer
    from sklearn.neighbors import NearestNeighbors
    reps = list({r["template"]: i for i, r in enumerate(rows)}.values())
    corpus = [rows[i]["template"] for i in reps]
    vec = TfidfVectorizer(analyzer="char", ngram_range=(4, 4), min_df=1, norm="l2")
    x = vec.fit_transform(corpus)
    nn = NearestNeighbors(n_neighbors=min(12, len(corpus)), metric="cosine", algorithm="brute").fit(x)
    distances, neighbors = nn.kneighbors(x)
    edges = 0
    for ai, (ds, ns) in enumerate(zip(distances, neighbors)):
        a = corpus[ai]
        aset = {a[k:k + 4] for k in range(max(0, len(a) - 3))}
        if len(aset) < 15:
            continue
        for d, bi in zip(ds, ns):
            if bi == ai or d > .22:
                continue
            b = corpus[bi]
            bset = {b[k:k + 4] for k in range(max(0, len(b) - 3))}
            if len(aset & bset) / max(1, len(aset | bset)) >= .80:
                union(reps[ai], reps[bi]); edges += 1
    group_labels = {}
    for i, row in enumerate(rows):
        group_labels.setdefault(root(i), set()).add(row["label"])
    conflicts = {g for g, labels in group_labels.items() if len(labels) != 1}
    kept, quarantine = [], []
    for i, row in enumerate(rows):
        row["group_id"] = f"g_{root(i):05d}"
        del row["template"]
        (quarantine if root(i) in conflicts else kept).append(row)
    # No repeated normalized raw texts as independent learning records.
    unique = {}
    for row in kept:
        unique.setdefault(row["normalized_digest"], row)
    final = list(unique.values())
    write_json(ROOT / cfg["output"], final)
    write_json(ROOT / "artifacts/private/quarantine.json", quarantine)
    summary = dict(input_rows=len(rows), parsed_rows=len(rows), quarantined_records=len(quarantine), conflicting_groups=len(conflicts), deduplicated_rows=len(kept) - len(final), usable_rows=len(final), classes=dict(Counter(r["original_label"] for r in final)), groups=len({r["group_id"] for r in final}), largest_group=max(Counter(r["group_id"] for r in final).values()), near_duplicate_edges=edges, processed_sha256=digest(ROOT / cfg["output"]), max_length=max(len(r["text"]) for r in final), source_sha256=digest(path))
    write_json(ROOT / "artifacts/data_summary.json", summary)
    print({k: v for k, v in summary.items() if k != "classes"})


def audit(cfg):
    rows = read_json(ROOT / cfg["output"])
    summary = dict(records=len(rows), residual_email_patterns=sum(bool(re.search(r"\b[^\s@]+@[^\s@]+\.[^\s@]+", r["text"])) for r in rows), residual_digits=sum(bool(re.search(r"\d", r["text"])) for r in rows), notes=["Names and addresses cannot be reliably removed by regex; no raw records redistributed.", "Privacy transforms are also applied on phone before model features; rules inspect original input."])
    write_json(ROOT / "artifacts/privacy_audit.json", summary)
    print(summary)


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("action", choices=["prepare", "audit"])
    p.add_argument("--config", required=True)
    a = p.parse_args()
    (prepare if a.action == "prepare" else audit)(config(a.config))


if __name__ == "__main__":
    main()
