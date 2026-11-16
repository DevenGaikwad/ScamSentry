package `in`.deven.scamsentry.core

import org.json.JSONObject
import java.security.MessageDigest
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.sqrt

/** Immutable, bounded non-executable bundle. No Java/Python object deserialization. */
class LinearModel(modelJson: String, manifestJson: String) {
    private val model: JSONObject
    val id: String
    private val vocabulary: Map<String, Int>
    private val idf: DoubleArray
    private val coefficients: Array<DoubleArray>
    private val intercept: DoubleArray
    private val temperature: Double
    val confidence: Double
    val margin: Double
    init {
        require(modelJson.toByteArray().size <= 15_000_000)
        require(manifestJson.length <= 10_000)
        val expected = JSONObject(manifestJson).getString("model_sha256")
        val actual = MessageDigest.getInstance("SHA-256").digest(modelJson.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }
        require(actual == expected) { "Model integrity mismatch" }
        model = JSONObject(modelJson)
        require(model.getInt("schema_version") == 1 && model.getString("normalizer") == TextContract.VERSION)
        require(model.getString("privacy_transform") == "typed_tokens_v1")
        require(model.getJSONArray("classes").toString() == "[\"ordinary\",\"promotional-or-spam\",\"smishing-like\"]")
        require(model.getJSONArray("ngram_range").toString() == "[3,5]")
        require(model.getBoolean("sublinear_tf") && model.getBoolean("l2_normalize"))
        id = model.getString("model_id")
        val terms = model.getJSONArray("vocabulary")
        require(terms.length() in 1..60000)
        vocabulary = (0 until terms.length()).associate { terms.getString(it) to it }
        require(vocabulary.size == terms.length())
        require(vocabulary.keys.all { it.codePointCount(0,it.length) in 3..5 })
        fun doubles(key: String): DoubleArray {
            val a = model.getJSONArray(key)
            return DoubleArray(a.length()) { a.getDouble(it).also { v -> require(v.isFinite()) } }
        }
        idf = doubles("idf")
        intercept = doubles("intercept")
        val cs = model.getJSONArray("coefficients")
        require(cs.length() == 3)
        coefficients = Array(3) { k -> val row = cs.getJSONArray(k); DoubleArray(row.length()) { row.getDouble(it).also { v -> require(v.isFinite()) } } }
        require(idf.size == vocabulary.size && idf.all { it > 0 } && intercept.size == 3 && coefficients.all { it.size == vocabulary.size })
        temperature = model.getDouble("temperature").also { require(it.isFinite() && it > 0) }
        val policy = model.getJSONObject("policy")
        require(policy.getString("version") == "ordinal_v1")
        confidence = policy.getDouble("confidence").also { require(it in 0.0..1.0) }
        margin = policy.getDouble("margin").also { require(it in 0.0..1.0) }
    }
    private fun weights(text: String): Pair<Map<Int,Double>, Double> {
        require(text.codePointCount(0,text.length) <= TextContract.MAX_CODEPOINTS)
        val counts = HashMap<Int, Int>()
        for (feature in TextContract.features(TextContract.minimize(text))) {
            val i = vocabulary[feature] ?: continue
            counts[i] = (counts[i] ?: 0) + 1
        }
        val weights = counts.mapValues { (i, n) -> (1 + ln(n.toDouble())) * idf[i] }
        val norm = sqrt(weights.values.sumOf { it * it })
        return (if (norm > 0) weights.mapValues { it.value / norm } else weights) to norm
    }
    fun predict(text: String): Estimate {
        val (weights, norm) = weights(text)
        val logits = (0..2).map { k -> intercept[k] + weights.entries.sumOf { (i, v) -> coefficients[k][i] * v } }
        val max = logits.max()
        val exps = logits.map { exp((it - max) / temperature) }
        val sum = exps.sum()
        return Estimate(TextContract.normalize(TextContract.minimize(text)), logits, exps.map { it / sum }, weights.size, norm)
    }
    fun contributions(text: String, prediction: Estimate): List<Contribution> {
        val label = prediction.probabilities.indices.maxBy { prediction.probabilities[it] }
        if (label == 0) return emptyList()
        val inverse = vocabulary.entries.associate { it.value to it.key }
        return weights(text).first.map { (i,v) -> Contribution(inverse.getValue(i), v*(coefficients[label][i]-coefficients[0][i])) }
            .filter { it.difference > 0 }.sortedByDescending { it.difference }.take(5)
    }
}
