package `in`.deven.scamsentry.core

import org.json.JSONArray
import org.json.JSONObject

data class ScoredMessage(val estimate: Estimate, val contributions: List<Contribution>)
interface MessageModel {
    val id: String
    val confidence: Double
    val margin: Double
    fun infer(text: String): ScoredMessage
}

data class DomainAnalysis(
    val risk: Risk, val estimate: Estimate?, val observations: List<Observation>,
    val urls: List<UrlObservation>, val qualityFlags: List<String>, val limitations: List<String>,
    val standaloneUrl: Boolean, val linksTruncated: Boolean, val modelId: String,
    val contributions: List<Contribution> = emptyList()
) {
    fun contractJson(): JSONObject = JSONObject().apply {
        put("schema_version", 2); put("policy_version", AnalysisEngine.POLICY)
        put("observation_version", "local_context_v2"); put("url_version", UrlLexicalV2.VERSION)
        put("normalizer_version", TextContract.VERSION); put("model_id", modelId); put("risk", risk.wire)
        put("estimate", estimate?.let { e -> JSONObject().apply {
            put("normalized", e.normalized); put("logits", JSONArray(e.logits)); put("probabilities", JSONArray(e.probabilities))
            put("active_count", e.activeCount); put("norm", e.norm)
        } } ?: JSONObject.NULL)
        put("observations", JSONArray(observations.map { o -> JSONObject().apply { put("code",o.code); put("source",o.source); put("severity",o.severity); put("title",o.title); put("detail",o.detail) } }))
        put("urls", JSONArray(urls.map { u -> JSONObject().apply { put("original",u.original); put("host",u.host ?: JSONObject.NULL); put("codes",JSONArray(u.codes)); put("valid",u.valid) } }))
        put("quality_flags",JSONArray(qualityFlags)); put("limitations",JSONArray(limitations))
        put("standalone_url",standaloneUrl); put("links_truncated",linksTruncated)
    }
}

/** All Android-independent input, quality, observation and policy decisions live here. */
object AnalysisEngine {
    const val POLICY = "ordinal_v2"
    fun analyze(text: String, model: MessageModel?, source: String = "text"): DomainAnalysis {
        val id = model?.id ?: "unavailable"
        fun result(risk: Risk, estimate: Estimate?, obs: List<Observation>, urls: List<UrlObservation>, flags: List<String>, standalone: Boolean = false, truncated: Boolean = false, contributions: List<Contribution> = emptyList()) =
            DomainAnalysis(risk,estimate,obs,urls,flags,flags.map { Limitations.messages.getValue(it) },standalone,truncated,id,contributions)
        val normalized = try {
            require(text.isNotBlank() && text.codePointCount(0,text.length) <= TextContract.MAX_CODEPOINTS)
            require(text.none { it.code < 32 && it !in "\t\n\r" })
            TextContract.normalize(text)
        } catch (_: IllegalArgumentException) { return result(Risk.UNABLE,null,emptyList(),emptyList(),listOf("invalid_input","research_scope")) }
        val obs = Rules.inspect(text)
        val extraction = UrlLexicalV2.extract(text)
        val urls = extraction.addresses
        val standalone = urls.size == 1 && text.trim() == urls[0].original
        val scope = TextContract.scriptSupported(text)
        val scored = if (model != null && scope && !standalone) model.infer(text) else null
        val estimate = scored?.estimate
        val flags = buildList {
            if (model == null && !standalone) add("model_unavailable")
            if (!scope && !standalone) add("unsupported_scope")
            if (source == "ocr") add(if (text.trim().codePointCount(0,text.trim().length) < 20) "short_ocr" else "ocr_review")
            if (estimate != null && estimate.activeCount < 4) add("weak_features")
            if (normalized.codePointCount(0,normalized.length) > 30 && normalized.replace(" ","").codePoints().distinct().count() < 4) add("repetitive_input")
            if (standalone) add("standalone_url")
            if (extraction.truncated) add("link_limit")
            add("research_scope")
            if (urls.isNotEmpty()) add("lexical_only")
        }
        val risk = when {
            obs.any { it.code == "secret_request" } -> Risk.HIGHER
            standalone -> if (urls[0].valid) Risk.CHECKING else Risk.UNABLE
            flags.any { it in listOf("model_unavailable","unsupported_scope","short_ocr","weak_features","repetitive_input","link_limit") } -> Risk.UNABLE
            else -> {
                val p = requireNotNull(estimate).probabilities
                val sorted = p.sortedDescending()
                when {
                    urls.any { it.codes.isNotEmpty() } || sorted[0] < requireNotNull(model).confidence || sorted[0]-sorted[1] < model.margin -> Risk.CHECKING
                    p.indices.maxBy { p[it] } == 2 -> Risk.HIGHER
                    p.indices.maxBy { p[it] } == 1 || obs.any { it.severity == "caution" } -> Risk.CHECKING
                    else -> Risk.LOWER
                }
            }
        }
        return result(risk,estimate,obs,urls,flags,standalone,extraction.truncated,scored?.contributions ?: emptyList())
    }
}
