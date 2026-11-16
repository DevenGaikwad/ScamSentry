package `in`.deven.scamsentry.core
import org.json.*
import org.junit.Assert.*
import org.junit.Test
import java.io.File
class ResearchBlockParityTest {
    @Test fun realSyntheticFitWordAndHybridParity() {
        val models=JSONObject(File(System.getProperty("parity.dir"),"research_blocks_synthetic_v2.json").readText()).getJSONArray("models")
        for(i in 0 until models.length()) {
            val m=models.getJSONObject(i);val model=ResearchBlockModel(m.getJSONObject("bundle").toString());val cases=m.getJSONArray("fixtures")
            for(j in 0 until cases.length()) {
                val f=cases.getJSONObject(j);val actual=model.predictContract(f.getString("text"));val expected=f.getJSONObject("expected")
                assertEquals(expected.getString("normalized"),actual.getString("normalized"));assertEquals(expected.getInt("active_count"),actual.getInt("active_count"))
                assertEquals(expected.getDouble("norm"),actual.getDouble("norm"),1e-6)
                for(key in listOf("logits","probabilities","block_norms")) for(k in 0 until expected.getJSONArray(key).length()) assertEquals("$i/$j/$key/$k",expected.getJSONArray(key).getDouble(k),actual.getJSONArray(key).getDouble(k),1e-6)
            }
        }
    }
}
