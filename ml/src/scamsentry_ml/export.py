import argparse
from pathlib import Path
import shutil
from .common import ROOT, digest, read_json, write_json
from .contracts import PortableModel, decide, observations, urls
from .bundle import validate

FIXTURES = ["Let's meet in the library after class.", "Please bring your lab notebook before 9 tomorrow.", "Send your OTP immediately to keep the account active.", "Fictional shop: 20 percent discount this weekend. Reply STOP to unsubscribe.", 'Educational example: never share your OTP. A scam may say "send your password".', "https://example.org/notes", "https://bank.example@review.test/login", "https://xn--bcher-kva.example", "Meet\t me\n at 5", "ＡＢＣ full width café e\u0301 😀", "पैसे पाठवा", "", "abc\u200bdefghijk", "aaaa" * 1000, "  ", "Visit https://example.org/a, and https://review.test/b.", "https://example.org\u00a0after the link", "A code １２３４ and an emoji 😀 in this message.", "Email bücher@example.org after class.", "Quoted example: Send your password immediately.", "https://[2001:db8::1]/notes", "https://example.org:99999", "intent://review.test/path", "zzzqqqwwwyyy" ]


def export(path):
    path = ROOT / path if not path.is_absolute() else path
    frozen = read_json(path / "frozen.json")
    if digest(path / "model.json") != frozen["model_sha256"]:
        raise ValueError("Model changed since freeze")
    out = ROOT / "artifacts/bundle"
    out.mkdir(parents=True, exist_ok=True)
    shutil.copy2(path / "model.json", out / "model.json")
    write_json(out / "manifest.json", dict(schema_version=1, model_sha256=digest(out / "model.json"), model_id="ss-char-lr-0.1.0", protocol_id=frozen["protocol_id"], evaluation_report="../runs/baseline_char_lr_001/final_test.json", format="utf8-json-float64", byte_size=(out / "model.json").stat().st_size))
    model = PortableModel(validate(out))
    fixtures = []
    for i, text in enumerate(FIXTURES):
        pred = model.predict(text)
        fixtures.append(dict(id=f"golden_{i:02d}", text=text, **pred, category=decide(text, pred["probabilities"], observations(text), urls(text), model.bundle["policy"]), urls=urls(text)))
    write_json(out / "golden.json", dict(tolerance=1e-6, model_sha256=frozen["model_sha256"], fixtures=fixtures))
    android = ROOT.parent / "android/app/src/main/assets/model"
    android.mkdir(parents=True, exist_ok=True)
    for name in ("model.json", "manifest.json", "golden.json"):
        shutil.copy2(out / name, android / name)
    print(f"Exported {out}; Android assets synchronized")


def main():
    p = argparse.ArgumentParser(description="Export frozen model and fictional parity fixtures")
    p.add_argument("--run", required=True, type=Path)
    a = p.parse_args()
    export(a.run)


if __name__ == "__main__":
    main()
