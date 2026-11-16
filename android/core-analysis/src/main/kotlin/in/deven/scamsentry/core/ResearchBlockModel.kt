package `in`.deven.scamsentry.core

import org.json.JSONObject
import org.json.JSONArray
import kotlin.math.*

/** Research schema 2. Never loaded as a fallback by the shipping application. */
class ResearchBlockModel(json: String) : MessageModel {
    private data class Block(val kind: String,val vocabulary: Map<String,Int>,val idf: DoubleArray,val weight: Double)
    private val blocks: List<Block>
    private val coefficients: Array<DoubleArray>
    private val intercept: DoubleArray
    private val temperature: Double
    override val id: String
    override val confidence: Double
    override val margin: Double
    init {
        val model=JSONObject(json)
        require(model.getInt("schema_version")==2 && !model.getBoolean("final_l2"))
        require(model.getString("normalizer")==TextContract.VERSION && model.getString("privacy_transform")=="typed_tokens_v1")
        require(model.getJSONArray("classes").toString()=="[\"ordinary\",\"promotional-or-spam\",\"smishing-like\"]")
        id=model.getString("model_id")
        val array=model.getJSONArray("blocks");require(array.length() in 1..2)
        blocks=(0 until array.length()).map { i ->
            val b=array.getJSONObject(i);val kind=b.getString("kind");require(kind in listOf("char","word"))
            require(b.getString("feature_version")==if(kind=="char") TextContract.VERSION else "ascii_words_2plus_ngrams_v1")
            require(b.getJSONArray("ngram_range").toString()==if(kind=="char") "[3,5]" else "[1,2]")
            require(b.getBoolean("sublinear_tf") && b.getBoolean("l2_normalize"))
            val terms=b.getJSONArray("vocabulary");require(terms.length() in 1..60000)
            val vocabulary=(0 until terms.length()).associate { terms.getString(it) to it };require(vocabulary.size==terms.length())
            val ids=b.getJSONArray("idf");require(ids.length()==terms.length())
            val idf=DoubleArray(ids.length()) { require(ids.get(it) is Number);ids.getDouble(it).also { v -> require(v.isFinite() && v>0) } }
            val weight=b.getDouble("weight");require(weight.isFinite() && weight>0)
            Block(kind,vocabulary,idf,weight)
        }
        require(blocks.map { it.kind }.distinct().size==blocks.size)
        val total=blocks.sumOf { it.vocabulary.size };val cs=model.getJSONArray("coefficients");require(cs.length()==3)
        coefficients=Array(3) { k -> val row=cs.getJSONArray(k);require(row.length()==total);DoubleArray(total) { require(row.get(it) is Number);row.getDouble(it).also { v -> require(v.isFinite()) } } }
        val ints=model.getJSONArray("intercept");require(ints.length()==3)
        intercept=DoubleArray(3) { ints.getDouble(it).also { v -> require(v.isFinite()) } }
        temperature=model.getDouble("temperature");require(temperature.isFinite() && temperature>0)
        val policy=model.getJSONObject("policy");require(policy.getString("version")=="ordinal_v1")
        confidence=policy.getDouble("confidence");margin=policy.getDouble("margin");require(confidence in 0.0..1.0 && margin in 0.0..1.0)
    }
    fun predictContract(text: String): JSONObject {
        val weights=linkedMapOf<Int,Double>();val norms=mutableListOf<Double>();var offset=0
        for(b in blocks) {
            val features=if(b.kind=="char") TextContract.features(TextContract.minimize(text)) else {
                val tokens=Regex("[a-z]+(?:'[a-z]+)?").findAll(TextContract.normalize(TextContract.minimize(text))).map { it.value }.filter { it.length>=2 }.toList()
                buildList { for(n in 1..2) for(i in 0..(tokens.size-n)) add(tokens.subList(i,i+n).joinToString(" ")) }
            }
            val counts=linkedMapOf<Int,Int>()
            for(f in features) { val i=b.vocabulary[f]?:continue;counts[i]=(counts[i]?:0)+1 }
            val raw=counts.mapValues { (i,n) -> (1+ln(n.toDouble()))*b.idf[i] };val norm=sqrt(raw.values.sumOf { it*it });norms.add(norm)
            if(norm>0) for((i,v) in raw) weights[offset+i]=v/norm*b.weight
            offset+=b.vocabulary.size
        }
        val logits=(0..2).map { k -> intercept[k]+weights.entries.sumOf { (i,v) -> coefficients[k][i]*v } }
        val exps=logits.map { exp((it-logits.max())/temperature) };val sum=exps.sum()
        return JSONObject().put("normalized",TextContract.normalize(TextContract.minimize(text))).put("logits",JSONArray(logits)).put("probabilities",JSONArray(exps.map { it/sum })).put("active_count",weights.size).put("norm",sqrt(weights.values.sumOf { it*it })).put("block_norms",JSONArray(norms))
    }
    override fun infer(text: String): ScoredMessage {
        val p=predictContract(text)
        fun list(key: String)=(0..2).map { p.getJSONArray(key).getDouble(it) }
        return ScoredMessage(Estimate(p.getString("normalized"),list("logits"),list("probabilities"),p.getInt("active_count"),p.getDouble("norm")),emptyList())
    }
}
