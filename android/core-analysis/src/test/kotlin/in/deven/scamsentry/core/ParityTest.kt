package `in`.deven.scamsentry.core

import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import kotlin.math.abs

class ParityTest {
    private val dir get() = File(System.getProperty("model.dir"))
    private fun model() = LinearModel(File(dir,"model.json").readText(), File(dir,"manifest.json").readText())
    @Test fun pythonKotlinGoldenParity() {
        val model = model()
        val golden = JSONObject(File(dir,"golden.json").readText())
        val fixtures = golden.getJSONArray("fixtures")
        var maxDifference = 0.0
        for (i in 0 until fixtures.length()) {
            val f = fixtures.getJSONObject(i)
            val text = f.getString("text")
            val got = model.predict(text)
            assertEquals(f.getString("normalized"), got.normalized)
            assertEquals(f.getInt("active_count"), got.activeCount)
            assertEquals(f.getDouble("norm"), got.norm, 1e-6)
            for (k in 0..2) {
                val diff = abs(f.getJSONArray("probabilities").getDouble(k)-got.probabilities[k])
                maxDifference = maxOf(maxDifference,diff)
                assertEquals(f.getJSONArray("logits").getDouble(k), got.logits[k], 1e-6)
                assertTrue("probability parity $i/$k: $diff", diff < 1e-6)
            }
            val category = Policy.decide(text, got.probabilities, Rules.inspect(text), UrlInspector.inspect(text), model.confidence, model.margin)
            assertEquals(f.getString("category"), category.wire)
        }
        val report = JSONObject().put("fixtures",fixtures.length()).put("max_probability_difference",maxDifference).put("tolerance",1e-6).put("status","pass").put("environment","Host JVM; Android device execution pending")
        File("build/reports/parity.json").apply { parentFile.mkdirs(); writeText(report.toString(2)) }
    }
    @Test fun integrityFailuresAreControlled() {
        assertThrows(IllegalArgumentException::class.java) { LinearModel(File(dir,"model.json").readText()+" ",File(dir,"manifest.json").readText()) }
    }
    @Test fun unicodeAndSurrogateBoundaries() {
        assertEquals("a b 😀", TextContract.normalize(" Ａ\t B  😀 "))
        assertEquals(listOf("😀ab"),TextContract.features("😀ab"))
        assertThrows(IllegalArgumentException::class.java) { TextContract.normalize("\uD800") }
    }
    @Test fun linksStayOfflineAndExposeActualHost() {
        val u = UrlInspector.inspect("https://bank.example@review.test/login").single()
        assertEquals("review.test",u.host)
        assertTrue("userinfo" in u.codes)
        assertEquals(2,UrlInspector.inspect("https://example.org/a, https://review.test/b.").size)
        assertFalse(UrlInspector.inspect("intent://review.test/x").single().valid)
        assertNull(UrlInspector.inspect("https://example.org:99999").single().host)
        assertEquals("bank.example.com.attacker.xyz",UrlInspector.inspect("bank.example.com.attacker.xyz").single().host)
        assertEquals("example.org",UrlInspector.inspect("Review example.org.").single().host)
    }
    @Test fun policyDoesNotTreatUrgencyAsProof() {
        val text="Urgent: please bring the lab notebook before class."
        assertEquals(Risk.LOWER,Policy.decide(text,listOf(.9,.05,.05),Rules.inspect(text),emptyList()))
        assertEquals(Risk.UNABLE,Policy.decide(text,null,emptyList(),emptyList()))
        assertEquals(Risk.UNABLE,Policy.decide("पैसे पाठवा",listOf(.9,.05,.05),emptyList(),emptyList()))
    }
}
