# V4 bounded model comparison

This preregistration uses the documented eligible Mendeley V1 corpus and the original frozen group assignments. Check both registered file digests before ingestion. Reuse only the training, calibration and development records. Historical test records will not be fitted, scored or used for a model decision.

## Question and training budget

Can mild label-preserving text corruption and character or word block weighting improve the trade-off between clean short-message classification and sensitivity to noisy text? The recorded v2 development results motivate the question. They do not select a new winner.

The exact four configurations are in [v4_research.json](../ml/configs/v4_research.json). Use three stratified, designated-group training folds, seed 42, and two numerical-library threads. Fit four candidates across three folds and refit at most one winner. Stop after 13 attempted fits or 420 seconds, whichever comes first. Detect a two-GiB process peak between fit stages and stop if it is exceeded. Store the preregistration hash and the outcome of each attempted fit.

Select among candidates with mean smishing recall at least 0.82 and mean ordinary-to-smishing prediction rate at most 0.03. Rank the survivors by 0.75 times clean macro F1 plus 0.25 times mildly perturbed macro F1. Break exact ties with smaller mean vocabulary size and then stable candidate name. Keep synthetic variants wholly inside each training fold and augment every class. Keep held-out perturbations label-preserving and score them separately. The development comparison will contain the retained baseline and at most one selected candidate.

## Calibration and interpretation

Fit scalar temperature using the frozen calibration partition. Retain the existing confidence and margin settings. Measure development quality and its grouped paired interval, model-only and whole-policy behavior, calibration, historic severe-substitution sensitivity and representative benign noise. Domain shift, previous development exposure, correlated variants and the absence of real OCR examples limit these results. The published model package and old reports remain the baseline.

Compare the Python portable export to estimator logits and probabilities on the permitted development partition. Record host size and latency under the stated environment. Development is exploratory. Any candidate needs independent prospective evidence, cross-runtime and resource validation, and physical-phone measurements before promotion. The recorded severe-substitution weakness is a sensitivity check. Its artificial variants do not measure device OCR accuracy.

Raw data, per-message texts, and new fitted vocabularies stay in ignored paths. Publish the aggregate tables, hashes, code and decision. No additional dataset or permission request is required.
