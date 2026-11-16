package `in`.deven.scamsentry

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import `in`.deven.scamsentry.core.AnalysisEngine
import `in`.deven.scamsentry.core.LinearModel
import `in`.deven.scamsentry.core.TextContract
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/** Executes the complete Python contract on Android ICU, not just OpenJDK. */
@RunWith(AndroidJUnit4::class)
class NativeContractTest {
    private fun compare(expected: Any?, actual: Any?, path: String) {
        when(expected) {
            is JSONObject -> {
                assertTrue(path,actual is JSONObject);actual as JSONObject
                assertEquals(path,expected.keys().asSequence().toSet(),actual.keys().asSequence().toSet())
                expected.keys().forEach { k -> compare(expected.get(k),actual.get(k),"$path/$k") }
            }
            is JSONArray -> {
                assertTrue(path,actual is JSONArray);actual as JSONArray
                assertEquals(path,expected.length(),actual.length())
                for(i in 0 until expected.length())compare(expected.get(i),actual.get(i),"$path/$i")
            }
            is Number -> {assertTrue(path,actual is Number);assertEquals(path,expected.toDouble(),(actual as Number).toDouble(),1e-6)}
            else -> assertEquals(path,expected,actual)
        }
    }
    @Test fun packagedModelAndUnicodeContractMatchEveryReference() {
        val assets=InstrumentationRegistry.getInstrumentation().targetContext.assets
        fun read(name:String)=assets.open("model/$name").bufferedReader().use { it.readText() }
        val model=LinearModel(read("model.json"),read("manifest.json"))
        val fixtures=JSONObject(read("full_golden_v2.json")).getJSONArray("fixtures")
        for(i in 0 until fixtures.length()) {
            val f=fixtures.getJSONObject(i);val text=f.getString("text");val id=f.getString("id")
            assertEquals(id,f.getString("minimized"),TextContract.minimize(text))
            val actual=AnalysisEngine.analyze(text,if(f.getBoolean("model_available"))model else null,f.getString("source"))
            compare(f.getJSONObject("expected"),actual.contractJson(),id)
        }
    }
}
