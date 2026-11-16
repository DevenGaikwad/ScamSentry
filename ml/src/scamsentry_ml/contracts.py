"""Portable normalization, transparent observations, and deterministic policy v1."""
from collections import Counter
import math
import re
import unicodedata
from urllib.parse import urlsplit

NORMALIZER = "nfkc_ascii_ws_v1"
POLICY = "ordinal_v1"
MAX_CODEPOINTS = 4000
WHITESPACE = frozenset([*range(9, 14), 32, 133, 160, 5760, *range(8192, 8203), 8232, 8233, 8239, 8287, 12288])
ZERO_WIDTH = "\u200b\u200c\u200d\ufeff"
URL_PATTERN = re.compile(r"(?i)\b(?:https?://|[a-z][a-z0-9+.-]*://|www\.)[^\s<>\"\u200b]+|(?<![@\w])(?:[a-z0-9-]+\.)+(?:[a-z]{2,63}|xn--[a-z0-9-]{2,59})\.?(?![\w.-])(?:[/?:#][^\s<>\"]*)?")
RULES = [
    ("secret_request", "high", "Request for a secret", "Keep one-time codes, PINs and passwords private. Verify the request through a known channel.", r"\b(?:share|send|tell|reveal|reply\s+with|provide|give)\b.{0,55}\b(?:otp|one[ -]time (?:code|password)|pin|password|verification code)\b"),
    ("payment_request", "caution", "Payment request", "Check the recipient and reason independently before sending money.", r"\b(?:pay|transfer|deposit|send money|payment required|processing fee)\b"),
    ("urgency", "info", "Time pressure", "A deadline alone does not establish fraud. Take time to verify unexpected requests.", r"\b(?:urgent|immediately|within \d+ (?:hour|minute)|suspend|blocked|last chance)\b"),
    ("reward", "info", "Reward or offer", "Offers can be legitimate advertising. An unexpected prize still deserves independent checking.", r"\b(?:won|winner|prize|reward|congratulations|offer|discount)\b"),
]


def normalize(text):
    if any(0xD800 <= ord(c) <= 0xDFFF for c in text):
        raise ValueError("Unpaired surrogate in input")
    chars = []
    for ch in unicodedata.normalize("NFKC", text):
        c = ord(ch)
        chars.append(" " if c in WHITESPACE else chr(c + 32) if 65 <= c <= 90 else ch)
    return re.sub(" +", " ", "".join(chars)).strip(" ")


def ngrams(text, low=3, high=5):
    text = normalize(text)
    return [text[i:i + n] for n in range(low, high + 1) for i in range(len(text) - n + 1)]


def privacy_transform(text):
    text = re.sub(r"(?i)\b[\w.+-]+@[\w.-]+\.[a-z]{2,}\b", " emailtoken ", text)
    text = re.sub(r"\b(?:\+?\d[\d ()-]{6,}\d)\b", " numbertoken ", text)
    text = re.sub(r"(?i)https?://[^\s<>]+|www\.[^\s<>]+", " urltoken ", text)
    # Other digit sequences include account references and short codes.
    return re.sub(r"\d+", " numbertoken ", text)


def observations(text):
    from .context_rules import has_active_secret_request
    lower = normalize(text)
    out = []
    for code, severity, title, detail, regex in RULES:
        matched = has_active_secret_request(lower) if code == "secret_request" else bool(re.search(regex, lower, re.S))
        if matched:
            out.append(dict(code=code, source="rule", severity=severity, title=title, detail=detail))
    if any(ch in text for ch in ZERO_WIDTH):
        out.append(dict(code="invisible_char", source="rule", severity="caution", title="Invisible characters", detail="Hidden formatting can change text and links. Review the original carefully."))
    return out


def urls(text):
    found = []
    for match in URL_PATTERN.finditer(text):
        original = match.group().rstrip(".,;!)]}")
        candidate = original if "://" in original else "https://" + original
        codes = []
        try:
            parsed = urlsplit(candidate)
            if parsed.scheme.lower() not in ("http", "https"):
                raise ValueError("Only HTTP and HTTPS syntax supported")
            host = parsed.hostname
            if not host or any(c in host for c in "%\\\u200b\u200c\u200d"):
                raise ValueError("Ambiguous host")
            host = host.encode("idna").decode("ascii").lower()
            if len(host) > 253 or any(not label or len(label) > 63 for label in host.rstrip(".").split(".")):
                raise ValueError("Host length invalid")
            if not re.fullmatch(r"[a-z0-9.:-]+", host):
                raise ValueError("Invalid host characters")
            if parsed.port is not None and not 1 <= parsed.port <= 65535:
                raise ValueError("Invalid port")
            if parsed.username is not None:
                codes.append("userinfo")
            if "xn--" in host:
                codes.append("idn")
            if parsed.scheme == "http":
                codes.append("unencrypted_scheme")
            if host in ("bit.ly", "tinyurl.com", "t.co"):
                codes.append("shortener")
            if len(original) > 180:
                codes.append("long_address")
            found.append(dict(original=original, host=host, codes=codes, valid=True))
        except (ValueError, UnicodeError):
            found.append(dict(original=original, host=None, codes=["unsupported_or_malformed"], valid=False))
    return found


def supported(text):
    letters = [c for c in normalize(text) if c.isalpha()]
    return len(letters) >= 8 and all("a" <= c <= "z" for c in letters)


def decide(text, probabilities, obs, addresses, thresholds=None, quality=False):
    p = thresholds or {"confidence": .62, "margin": .18}
    if not text.strip() or len(text) > MAX_CODEPOINTS:
        return "unable"
    if probabilities is None:
        return "unable"
    if not supported(text) or quality:
        return "unable"
    if any(o["code"] == "secret_request" for o in obs):
        return "higher"
    if any("userinfo" in u["codes"] for u in addresses):
        return "checking"
    ordered = sorted(probabilities, reverse=True)
    if ordered[0] < p["confidence"] or ordered[0] - ordered[1] < p["margin"]:
        return "checking"
    label = max(range(3), key=probabilities.__getitem__)
    if label == 2:
        return "higher"
    if label == 1 or any(o["severity"] == "caution" for o in obs) or any(u["codes"] for u in addresses):
        return "checking"
    return "lower"


class PortableModel:
    def __init__(self, bundle):
        self.bundle = bundle
        self.vocab = {s: i for i, s in enumerate(bundle["vocabulary"])}

    def predict(self, text):
        counts = Counter(self.vocab[g] for g in ngrams(privacy_transform(text)) if g in self.vocab)
        weights = {i: (1 + math.log(n)) * self.bundle["idf"][i] for i, n in counts.items()}
        norm = math.sqrt(sum(v * v for v in weights.values()))
        if norm:
            weights = {i: v / norm for i, v in weights.items()}
        logits = [b + sum(row[i] * v for i, v in weights.items()) for b, row in zip(self.bundle["intercept"], self.bundle["coefficients"])]
        temperature = self.bundle["temperature"]
        exps = [math.exp((x - max(logits)) / temperature) for x in logits]
        probs = [e / sum(exps) for e in exps]
        return dict(normalized=normalize(privacy_transform(text)), logits=logits, probabilities=probs, active_count=len(weights), norm=norm)
