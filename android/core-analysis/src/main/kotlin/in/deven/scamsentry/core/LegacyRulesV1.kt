package `in`.deven.scamsentry.core

object LegacyRulesV1 {
    private data class Rule(val code: String, val severity: String, val title: String, val detail: String, val pattern: String)
    private val rules = listOf(
        Rule("secret_request", "high", "Request for a secret", "Keep one-time codes, PINs and passwords private. Verify the request through a known channel.", "\\b(?:share|send|tell|reveal|reply\\s+with|provide|give)\\b.{0,55}\\b(?:otp|one[ -]time (?:code|password)|pin|password|verification code)\\b"),
        Rule("payment_request", "caution", "Payment request", "Check the recipient and reason independently before sending money.", "\\b(?:pay|transfer|deposit|send money|payment required|processing fee)\\b"),
        Rule("urgency", "info", "Time pressure", "A deadline alone does not establish fraud. Take time to verify unexpected requests.", "\\b(?:urgent|immediately|within \\d+ (?:hour|minute)|suspend|blocked|last chance)\\b"),
        Rule("reward", "info", "Reward or offer", "Offers can be legitimate advertising. An unexpected prize still deserves independent checking.", "\\b(?:won|winner|prize|reward|congratulations|offer|discount)\\b")
    )
    fun inspect(text: String): List<Observation> {
        val normalized = TextContract.normalize(text)
        val quoted = Regex("\\b(?:example|educational|scam awareness|quoted|warning about|do not share|never share|don't share)\\b").containsMatchIn(normalized)
        val out = rules.filter { Regex(it.pattern, RegexOption.DOT_MATCHES_ALL).containsMatchIn(normalized) && !(quoted && it.code == "secret_request") }
            .map { Observation(it.code,"rule",it.severity,it.title,it.detail) }.toMutableList()
        if (text.any { it in "\u200b\u200c\u200d\ufeff" }) out.add(Observation("invisible_char", "rule", "caution", "Invisible characters", "Hidden formatting can change text and links. Review the original carefully."))
        return out
    }
}

