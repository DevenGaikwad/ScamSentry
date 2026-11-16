"""Local request context must not be neutralized by unrelated warning text."""
import pytest
from scamsentry_ml.contracts import observations


@pytest.mark.parametrize("text",[
    "Please send your OTP to confirm the account.",
    "Please send your OTP to confirm the account. For example, use the form.",
    "Never share your OTP. Please send your password now.",
    "Do not share this notice. Send your OTP now.",
    "Educational notice. Give your PIN to confirm the account.",
    "Do not share anything except your OTP.",
    "Never share your OTP; however send your password now.",
    "Never share your OTP.\nPlease reveal your verification code.",
    'A scam may say "send your OTP". Now provide your password.',
    "Do not share this notice send your OTP now.",
])
def test_affirmative_request_survives_independent_context(text):
    assert any(o["code"]=="secret_request" for o in observations(text))


@pytest.mark.parametrize("text",[
    "Never share your OTP.",
    "Do not send your password to anyone.",
    "You must not ever reveal your PIN.",
    "Don’t share your OTP with strangers.",
    'Educational example: "Send your OTP" is suspicious.',
    'A scam may say "send your password". Verify independently.',
    'Scam awareness: “give your PIN” is a typical request to avoid.',
    "Your one-time code is 123456. Keep it private.",
])
def test_negated_and_attached_educational_quotes_are_not_active_requests(text):
    assert not any(o["code"]=="secret_request" for o in observations(text))
