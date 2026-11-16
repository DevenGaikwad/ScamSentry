"""Bounded local request contexts v2; pattern evidence, never sender authentication."""
import re

OBSERVATION_VERSION = "local_context_v2"
REQUEST = re.compile(r"(?=\b(share|send|tell|reveal|reply\s+with|provide|give)\b([^.!?;:]{0,55}?)\b(otp|one[ -]time (?:code|password)|pin|password|verification code)\b)")
NEGATION = re.compile(r"\b(?:never|do not|don't|don’t|must not|should not|cannot|can't|can’t|not to)\s+(?:(?:ever|again|please|really|just|even|you|under any circumstances)\s+){0,3}$")
EXCEPTION = re.compile(r"\b(?:except|unless|but|however|instead|actually)\b")
QUOTE = re.compile(r'["“]([^"”]{1,180})["”]')
EDUCATION_BEFORE = re.compile(r"(?:educational(?: example)?|quoted example|scam awareness|(?:a )?scam (?:may )?says?|attackers? (?:may )?(?:ask|say))\s*[:,-]?\s*$")
EDUCATION_AFTER = re.compile(r"^\s*(?:is|are)\s+(?:a |an )?(?:typical |common )?(?:scam|example|quoted|educational)")


def has_active_secret_request(normalized):
    quotes = list(QUOTE.finditer(normalized))
    for match in REQUEST.finditer(normalized):
        start, end = match.start(1), match.end(3)
        clause_start = max(normalized.rfind(c, 0, start) for c in ".!?;") + 1
        prefix = re.sub(r"\s+", " ", normalized[max(clause_start, start-80):start].replace(",", " "))
        if NEGATION.search(prefix) and not EXCEPTION.search(match.group(2)):
            continue
        educational_quote = False
        for quote in quotes:
            if quote.start(1) <= start and end <= quote.end(1):
                before = normalized[max(0,quote.start()-90):quote.start()]
                after = normalized[quote.end():quote.end()+80]
                if EDUCATION_BEFORE.search(before) or EDUCATION_AFTER.search(after):
                    educational_quote = True
                    break
        if not educational_quote:
            return True
    return False
