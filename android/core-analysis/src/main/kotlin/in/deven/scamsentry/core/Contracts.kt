package `in`.deven.scamsentry.core

import java.net.IDN
import java.net.URI
import java.text.Normalizer

data class Observation(val code: String, val source: String, val severity: String, val title: String, val detail: String)
data class UrlObservation(val original: String, val host: String?, val codes: List<String>, val valid: Boolean)
data class Estimate(val normalized: String, val logits: List<Double>, val probabilities: List<Double>, val activeCount: Int, val norm: Double)
data class Contribution(val fragment: String, val difference: Double)
enum class Risk(val wire: String, val title: String, val description: String) {
    HIGHER("higher", "Higher risk", "There are indicators that deserve careful independent verification."),
    CHECKING("checking", "Needs checking", "There are mixed signals or limited context. Verify before acting."),
    LOWER("lower", "Lower observed risk", "Fewer indicators were found in the supported analysis. This does not verify the sender."),
    UNABLE("unable", "Unable to assess reliably", "The available analysis is limited. Review the input and verify independently.")
}
data class AnalysisResult(val id: String, val inputVersion: Long, val text: String, val source: String, val risk: Risk, val estimate: Estimate?, val observations: List<Observation>, val urls: List<UrlObservation>, val limitations: List<String>, val modelId: String, val contributions: List<Contribution>, val completedAt: Long = System.currentTimeMillis(), val policyVersion: String = AnalysisEngine.POLICY, val schemaVersion: Int = 2, val normalizerVersion: String = TextContract.VERSION, val observationVersion: String = "local_context_v2")

object TextContract {
    const val MAX_CODEPOINTS = 4000
    const val VERSION = "nfkc_ascii_ws_v1"
    private val whitespace = (9..13).toSet() + setOf(32,133,160,5760,8232,8233,8239,8287,12288) + (8192..8202).toSet()
    fun normalize(text: String): String {
        var i = 0
        while (i < text.length) {
            val c = text[i]
            if (Character.isHighSurrogate(c)) {
                require(i + 1 < text.length && Character.isLowSurrogate(text[i + 1])) { "Unpaired surrogate" }
                i += 2
            } else {
                require(!Character.isLowSurrogate(c)) { "Unpaired surrogate" }; i++
            }
        }
        val out = StringBuilder()
        Normalizer.normalize(text, Normalizer.Form.NFKC).codePoints().forEach { c ->
            out.appendCodePoint(if (c in whitespace) 32 else if (c in 65..90) c + 32 else c)
        }
        return out.toString().replace(Regex(" +"), " ").trim(' ')
    }
    private val email = Regex("(?i)${UnicodePattern.BOUNDARY}[${UnicodePattern.WORD_CONTENT}.+-]+@[${UnicodePattern.WORD_CONTENT}.-]+\\.[a-z]{2,}${UnicodePattern.BOUNDARY}")
    private val phone = Regex("${UnicodePattern.BOUNDARY}(?:\\+?\\p{Nd}[\\p{Nd} ()-]{6,}\\p{Nd})${UnicodePattern.BOUNDARY}")
    private val address = Regex("(?i)https?://[^${UnicodePattern.SPACE_CONTENT}<>]+|www\\.[^${UnicodePattern.SPACE_CONTENT}<>]+")
    private val digits = Regex("\\p{Nd}+")
    fun minimize(text: String): String = text
        .replace(email, " emailtoken ")
        .replace(phone, " numbertoken ")
        .replace(address, " urltoken ")
        .replace(digits, " numbertoken ")
    fun features(text: String): List<String> {
        val cps = normalize(text).codePoints().toArray()
        val out = ArrayList<String>()
        for (n in 3..5) for (i in 0..(cps.size - n)) out.add(String(cps, i, n))
        return out
    }
    fun scriptSupported(text: String): Boolean {
        val letters = normalize(text).codePoints().toArray().filter { Character.isLetter(it) }
        return letters.size >= 8 && letters.all { it in 97..122 }
    }
}

object Rules {
    private data class Rule(val code: String, val severity: String, val title: String, val detail: String, val pattern: String)
    private val rules = listOf(
        Rule("secret_request", "high", "Request for a secret", "Keep one-time codes, PINs and passwords private. Verify the request through a known channel.", "\\b(?:share|send|tell|reveal|reply\\s+with|provide|give)\\b.{0,55}\\b(?:otp|one[ -]time (?:code|password)|pin|password|verification code)\\b"),
        Rule("payment_request", "caution", "Payment request", "Check the recipient and reason independently before sending money.", "\\b(?:pay|transfer|deposit|send money|payment required|processing fee)\\b"),
        Rule("urgency", "info", "Time pressure", "A deadline alone does not establish fraud. Take time to verify unexpected requests.", "\\b(?:urgent|immediately|within \\d+ (?:hour|minute)|suspend|blocked|last chance)\\b"),
        Rule("reward", "info", "Reward or offer", "Offers can be legitimate advertising. An unexpected prize still deserves independent checking.", "\\b(?:won|winner|prize|reward|congratulations|offer|discount)\\b")
    )
    fun inspect(text: String): List<Observation> {
        val normalized = TextContract.normalize(text)
        val out = rules.filter { if(it.code=="secret_request") ContextRulesV2.hasActiveRequest(normalized) else Regex(it.pattern, RegexOption.DOT_MATCHES_ALL).containsMatchIn(normalized) }
            .map { Observation(it.code,"rule",it.severity,it.title,it.detail) }.toMutableList()
        if (text.any { it in "\u200b\u200c\u200d\ufeff" }) out.add(Observation("invisible_char", "rule", "caution", "Invisible characters", "Hidden formatting can change text and links. Review the original carefully."))
        return out
    }
}

object UrlInspector {
    fun inspect(text: String): List<UrlObservation> = UrlLexicalV2.extract(text).addresses
}

object Policy {
    fun decide(text: String, probabilities: List<Double>?, obs: List<Observation>, urls: List<UrlObservation>, confidence: Double = .62, margin: Double = .18, quality: Boolean = false): Risk {
        if (text.isBlank() || text.codePointCount(0,text.length) > TextContract.MAX_CODEPOINTS || probabilities == null) return Risk.UNABLE
        if (!TextContract.scriptSupported(text) || quality) return Risk.UNABLE
        if (obs.any { it.code == "secret_request" }) return Risk.HIGHER
        if (urls.any { "userinfo" in it.codes }) return Risk.CHECKING
        val sorted = probabilities.sortedDescending()
        if (sorted[0] < confidence || sorted[0] - sorted[1] < margin) return Risk.CHECKING
        val label = probabilities.indices.maxBy { probabilities[it] }
        if (label == 2) return Risk.HIGHER
        if (label == 1 || obs.any { it.severity == "caution" } || urls.any { it.codes.isNotEmpty() }) return Risk.CHECKING
        return Risk.LOWER
    }
}
