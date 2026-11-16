# Reproduce the ML pipeline

Run from `ml` after installing the local package and pinned requirements. The app bundle is already exported; raw/processed messages are not distributed. Obtain the primary source from the registered original publisher, verify the pinned digest, and keep it under the ignored `data/raw` directory.

```sh
python -m scamsentry_ml.sources validate
python -m scamsentry_ml.data prepare --config configs/data.yaml
python -m scamsentry_ml.data audit --config configs/data.yaml
python -m scamsentry_ml.eda
python -m scamsentry_ml.splits create --config configs/splits.yaml
python -m scamsentry_ml.splits verify --manifest artifacts/splits.json
python -m scamsentry_ml.train --config configs/baseline.yaml
python -m scamsentry_ml.compare
python -m scamsentry_ml.evaluate --run artifacts/runs/baseline_char_lr_001 --protocol ss-group-v1
python -m scamsentry_ml.export --run artifacts/runs/baseline_char_lr_001
python -m scamsentry_ml.robustness --config configs/robustness.yaml
python -m scamsentry_ml.bundle validate --bundle artifacts/bundle
python -m pytest
```

These are the executed command contracts. The archived `baseline_char_lr_001` is frozen; to run training again, use a new run ID in a copied config and an appropriate new protocol identity. Do not overwrite the archived test report. Export currently copies to the single app bundle destination, so review before replacing a release artifact. Existing source hashes must match; no toy data fallback exists.

Report files contain aggregates and stable local identifiers, never raw message examples. The local reference joblib and neural checkpoint are trusted developer outputs only and are not distributed; the phone uses validated JSON. Core tests can run against the exported bundle without a dataset. The sklearn-reference comparison test skips when the ignored private joblib is absent; the checked-in Kotlin golden suite remains available.
