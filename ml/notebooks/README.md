# ScamSentry ML evidence notebook

[Open in Colab](https://colab.research.google.com/github/DevenGaikwad/ScamSentry/blob/main/ml/notebooks/ScamSentry_ML_Evidence.ipynb) · [Notebook source](ScamSentry_ML_Evidence.ipynb) · [README figures](../../assets/evidence/ml-notebook/FIGURE_PROVENANCE.json)

Use a CPU runtime and choose **Runtime → Run all**. The default run reads hash-checked, aggregate evidence from the checkout or public repository. It exports ten PNGs, seven CSVs and a SHA-256 figure manifest under **scamsentry_evidence/**. NumPy, pandas, Matplotlib and IPython are required. The chart manifest records actual versions. Local Jupyter can run the same notebook after installing those packages.

| Notebook view | Exact scope |
| --- | --- |
| Split and dataset charts | 5,740 retained examples across four frozen partitions |
| Training lengths and correlation | 3,444 minimized training examples in 3,244 groups |
| Confusion, classification, calibration and robustness | Archived historical test, 861 records in 813 groups |
| ROC and precision–recall | Archived baseline on known development, 861 records and 75 smishing-like positives |
| Learning and estimator comparison | Earlier known development evaluation and three single-seed training sizes |
| V4 gate | Training-only grouped cross-validation with 12 fits and an 82% clean smishing recall floor |

The training correlation export includes both Pearson and Spearman values. The ranking figure shows a 101-point threshold grid and exact summary scores from the archived probabilities. Historical test AP and AUROC remain separate. The model card gives the interpretation and limits. [Model card](../../docs/MODEL_CARD.md) · [Historical results](../../docs/EVALUATION_RESULTS.md) · [V4 results](../../docs/ML_V4_RESULTS.md)

## Rebuild the aggregate input

Use a scratch checkout of the evidence commit 01765677ea3e7a95b8983498168604cfec83090c and its Python 3.12 lockfile. From that checkout, obtain the registered CSV via **scripts/fetch_source.py**, prepare it with **scamsentry_ml.data prepare --config configs/data.yaml**, and verify **ml/artifacts/splits.json** using the repository split verifier. See the ML reproduction guide for the exact working directories and commands. [ML guide](../README.md)

Invoke the builder from your current checkout, with the scratch checkout's **ml/src** on **PYTHONPATH** and the frozen checkout as **--project-root**. Save the output to a separate path and compare it with **presentation_aggregates.json**.

~~~bash
PYTHONPATH="/path/to/frozen-checkout/ml/src" python ml/notebooks/build_presentation_aggregates.py \
  --project-root "/path/to/frozen-checkout" \
  --output "/path/to/scratch/presentation_aggregates.json"
~~~

The builder verifies the split and processed-data digests, then cross-checks labels, groups and the development confusion matrix. Its output contains histograms, correlations and threshold aggregates. Source corpus credit and licence details are in the notebook and data record. [Published numerical input](presentation_aggregates.json) · [Data record](../../docs/DATA_SOURCES.md)

## Demonstrate the training workflow

Set **RUN_TRAINING_DEMO** in the notebook when you want to run a separate C2 fit. Use a Python 3.12 CPU runtime with access to GitHub, PyPI and the registered dataset publisher. If the publisher download fails, set **TRAINING_CSV** to a runtime copy of the exact registered CSV. The SHA-256 check applies to either route. Training recreates the frozen split, uses an isolated pinned environment, and reports development metrics. The temporary checkout and trained files are cleaned up when the cell completes.

For a short faculty demo, show the split chart, historical confusion and training correlation, then point to the OCR-like substitution result and V4 screening decision. The original model's reported test metrics come from its frozen release record.
