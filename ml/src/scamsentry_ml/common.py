from pathlib import Path
import hashlib
import json
import os
import tempfile
import yaml

ROOT = Path(__file__).resolve().parents[2]
CLASSES = ["ordinary", "promotional-or-spam", "smishing-like"]


def digest(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()


def read_json(path):
    return json.loads(Path(path).read_text(encoding="utf-8"))


def write_json(path, value):
    path = Path(path)
    path.parent.mkdir(parents=True, exist_ok=True)
    fd, tmp = tempfile.mkstemp(dir=path.parent, prefix=".writing-")
    try:
        with os.fdopen(fd, "w", encoding="utf-8") as f:
            json.dump(value, f, ensure_ascii=False, indent=2, allow_nan=False)
            f.write("\n")
        os.replace(tmp, path)
    finally:
        if Path(tmp).exists():
            Path(tmp).unlink()


def config(path):
    data = yaml.safe_load(Path(path).read_text(encoding="utf-8"))
    if not isinstance(data, dict):
        raise ValueError("Configuration must contain an object")
    return data
