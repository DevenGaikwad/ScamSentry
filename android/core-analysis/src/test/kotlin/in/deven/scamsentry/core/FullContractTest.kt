package `in`.deven.scamsentry.core
import org.json.*
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class FullContractTest {
    private fun compare(expected: Any?, actual: Any?, path: String) {
        when (expected) {
            is JSONObject -> {
                assertTrue(path, actual is JSONObject)
                actual as JSONObject
                assertEquals(path,expected.keySet(),actual.keySet())
                expected.keys().forEach { k -> compare(expected.get(k),actual.get(k),"$path/$k") }
            }
            is JSONArray -> {
                assertTrue(path,actual is JSONArray); actual as JSONArray
                assertEquals(path,expected.length(),actual.length())
                for (i in 0 until expected.length()) compare(expected.get(i),actual.get(i),"$path/$i")
            }
            is Number -> { assertTrue(path,actual is Number); assertEquals(path,expected.toDouble(),(actual as Number).toDouble(),1e-6) }
            else -> assertEquals(path,expected,actual)
        }
    }
    @Test fun fullPythonKotlinContracts() {
        val dir=File(System.getProperty("model.dir"))
        val model=LinearModel(File(dir,"model.json").readText(),File(dir,"manifest.json").readText())
        val fixtures=JSONObject(File(dir,"full_golden_v2.json").readText()).getJSONArray("fixtures")
        for (i in 0 until fixtures.length()) {
            val f=fixtures.getJSONObject(i)
            assertEquals(f.getString("id"),f.getString("minimized"),TextContract.minimize(f.getString("text")))
            val actual=AnalysisEngine.analyze(f.getString("text"),if(f.getBoolean("model_available")) model else null,f.getString("source"))
            compare(f.getJSONObject("expected"),actual.contractJson(),f.getString("id"))
        }
        File("build/reports/full-contract-parity.json").apply { parentFile.mkdirs(); writeText(JSONObject().put("status","PASS").put("fixtures",fixtures.length()).put("tolerance",1e-6).put("scope","Host JVM; complete domain outputs").toString(2)) }
    }
}
