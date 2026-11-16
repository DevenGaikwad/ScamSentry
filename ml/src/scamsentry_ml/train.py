"""Bounded baseline selection using development only, calibration held separately."""
import argparse
import platform
import time
import joblib
import numpy as np
import sklearn
from scipy.optimize import minimize_scalar
from scipy.special import softmax
from sklearn.dummy import DummyClassifier
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import f1_score, log_loss
from sklearn.naive_bayes import MultinomialNB
from sklearn.svm import LinearSVC
from threadpoolctl import threadpool_limits
from .common import ROOT, CLASSES, config, digest, read_json, write_json
from .contracts import NORMALIZER, POLICY, ngrams, normalize, observations
from .metrics import measure
from .splits import verify


def load_partitions():
    manifest = verify(ROOT / "artifacts/splits.json")
    if digest(ROOT / "data/processed/messages.json") != manifest["processed_digest"]:
        raise ValueError("Processed data changed since partition freeze")
    membership = {r["record_id"]: r["partition"] for r in manifest["membership"]}
    rows = read_json(ROOT / "data/processed/messages.json")
    return {p: [r for r in rows if membership[r["record_id"]] == p] for p in ("train", "development", "calibration", "test")}


def run(cfg):
    start = time.perf_counter()
    out = ROOT / "artifacts/runs" / cfg["run_id"]
    if (out / "frozen.json").exists():
        raise ValueError("Run already frozen; use a new run identifier for new experiments")
    parts = load_partitions()
    train, dev, cal = (parts[p] for p in ("train", "development", "calibration"))
    texts = {p: [r["text"] for r in rows] for p, rows in parts.items() if p != "test"}
    labels = {p: np.array([r["label"] for r in rows]) for p, rows in parts.items() if p != "test"}
    char = TfidfVectorizer(analyzer=ngrams, lowercase=False, min_df=cfg["min_df"], max_features=cfg["max_features"], sublinear_tf=True, dtype=np.float64)
    word = TfidfVectorizer(preprocessor=normalize, lowercase=False, ngram_range=(1, 2), min_df=2, max_features=20000, sublinear_tf=True)
    matrices = {}
    for name, vectorizer in (("char", char), ("word", word)):
        matrices[name] = {"train": vectorizer.fit_transform(texts["train"])}
        for p in ("development", "calibration"):
            matrices[name][p] = vectorizer.transform(texts[p])
    table = []
    candidates = []
    settings = [("majority", "char", DummyClassifier(strategy="most_frequent")), ("word_lr", "word", LogisticRegression(C=4, max_iter=450, class_weight="balanced", random_state=42)), ("char_nb", "char", MultinomialNB(alpha=.2)), ("char_svc", "char", LinearSVC(C=1, class_weight="balanced", random_state=42))]
    settings += [(f"char_lr_C{c:g}", "char", LogisticRegression(C=c, max_iter=450, class_weight="balanced", random_state=42)) for c in cfg["candidate_C"]]
    with threadpool_limits(limits=2):
        for name, feature, estimator in settings:
            tick = time.perf_counter()
            estimator.fit(matrices[feature]["train"], labels["train"])
            pred = estimator.predict(matrices[feature]["development"])
            score = f1_score(labels["development"], pred, average="macro")
            table.append(dict(candidate=name, feature=feature, development_macro_f1=score, fit_seconds=time.perf_counter() - tick, development_support=len(dev), final_test_accessed=False))
            candidates.append((name, feature, estimator, score))
    rule_pred = [2 if any(o["code"] == "secret_request" for o in observations(t)) else 1 if any(o["code"] == "reward" for o in observations(t)) else 0 for t in texts["development"]]
    table.append(dict(candidate="transparent_rules", development_macro_f1=f1_score(labels["development"], rule_pred, average="macro"), final_test_accessed=False))
    # A portable LR is the declared deployment family; all comparison results remain visible.
    deployable = [c for c in candidates if c[0].startswith("char_lr")]
    winner = max(deployable, key=lambda c: c[3])
    logits = winner[2].decision_function(matrices["char"]["calibration"])
    objective = lambda log_t: log_loss(labels["calibration"], softmax(logits / np.exp(log_t), axis=1), labels=[0, 1, 2])
    opt = minimize_scalar(objective, bounds=(-2, 2), method="bounded")
    temperature = float(np.exp(opt.x))
    dev_logits = winner[2].decision_function(matrices["char"]["development"])
    dev_probs = softmax(dev_logits / temperature, axis=1)
    model = dict(schema_version=1, model_id="ss-char-lr-0.1.0", task="message_three_class", classes=CLASSES, normalizer=NORMALIZER, privacy_transform="typed_tokens_v1", ngram_range=[3, 5], sublinear_tf=True, l2_normalize=True, vocabulary=char.get_feature_names_out().tolist(), idf=char.idf_.tolist(), coefficients=winner[2].coef_.tolist(), intercept=winner[2].intercept_.tolist(), temperature=temperature, policy=dict(version=POLICY, confidence=cfg["policy_confidence"], margin=cfg["policy_margin"]), scope="Experimental English messages; script check is not language identification", sources=["10.17632/f45bkkt8pr.1"])
    write_json(out / "model.json", model)
    write_json(out / "development.json", measure(labels["development"], dev_probs, [r["group_id"] for r in dev]))
    write_json(out / "comparison.json", table)
    joblib.dump(dict(vectorizer=char, estimator=winner[2]), out / "reference.joblib")
    write_json(out / "run.json", dict(run_id=cfg["run_id"], winner=winner[0], deployment_choice="Best development char LR among bounded C candidates; word/SVC comparisons not automatically deployable", temperature=temperature, fit_partition="train", calibration_partition="calibration", selection_partition="development", environment=dict(python=platform.python_version(), sklearn=sklearn.__version__, platform=platform.platform()), dataset_digest=digest(ROOT / "data/processed/messages.json"), split_digest=digest(ROOT / "artifacts/splits.json"), config=cfg, final_test_accessed=False, wall_seconds=time.perf_counter()-start))
    write_json(out / "frozen.json", dict(protocol_id=cfg["protocol_id"], model_sha256=digest(out / "model.json"), split_sha256=digest(ROOT / "artifacts/splits.json"), policy=model["policy"], scope="Research preview; no broad protection claim"))
    print(dict(winner=winner[0], development_macro_f1=winner[3], temperature=temperature, seconds=time.perf_counter()-start, path=str(out)))


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--config", required=True)
    a = p.parse_args()
    run(config(a.config))


if __name__ == "__main__":
    main()
