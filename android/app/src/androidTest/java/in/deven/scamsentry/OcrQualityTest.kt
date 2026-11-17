package `in`.deven.scamsentry

import android.content.ContentValues
import android.graphics.*
import android.provider.MediaStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import `in`.deven.scamsentry.core.AnalysisEngine
import `in`.deven.scamsentry.core.LinearModel
import `in`.deven.scamsentry.data.OcrReader
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.Random

/** Six controlled rendering slices, not an estimate of population OCR accuracy. */
@RunWith(AndroidJUnit4::class)
class OcrQualityTest {
    private fun <T> distance(a:List<T>,b:List<T>):Int {
        var previous=IntArray(b.size+1){it}
        for(i in a.indices){val row=IntArray(b.size+1);row[0]=i+1
            for(j in b.indices)row[j+1]=minOf(row[j]+1,previous[j+1]+1,previous[j]+if(a[i]==b[j])0 else 1)
            previous=row
        };return previous[b.size]
    }
    @Test fun recordExtractionErrorSeparatelyFromClassification()=runBlocking {
        val instrumentation=InstrumentationRegistry.getInstrumentation();val context=instrumentation.targetContext
        val lines=listOf("Fictional account team", "Send your OTP immediately.", "Verify through a known contact.", "https://review.test/notes")
        val reference=lines.joinToString(" ").lowercase()
        data class Slice(val name:String,val width:Int,val height:Int,val size:Float,val font:String,val foreground:Int,val background:Int,val noise:Boolean=false)
        val slices=listOf(
            Slice("clean_sans",720,190,20f,"sans-serif",Color.BLACK,Color.WHITE),
            Slice("small_sans",480,140,14f,"sans-serif",Color.BLACK,Color.WHITE),
            Slice("large_serif",1000,240,28f,"serif",Color.BLACK,Color.WHITE),
            Slice("low_contrast",720,200,22f,"sans-serif",Color.rgb(160,160,160),Color.rgb(235,235,235)),
            Slice("dark",720,200,22f,"sans-serif",Color.WHITE,Color.rgb(24,24,24)),
            Slice("salt_noise",720,190,20f,"sans-serif",Color.BLACK,Color.WHITE,true))
        fun read(name:String)=context.assets.open("model/$name").bufferedReader().use { it.readText() }
        val model=LinearModel(read("model.json"),read("manifest.json"));val rows=JSONArray()
        for(s in slices){
            val image=Bitmap.createBitmap(s.width,s.height,Bitmap.Config.ARGB_8888)
            val canvas=Canvas(image);canvas.drawColor(s.background)
            val paint=Paint(Paint.ANTI_ALIAS_FLAG).apply { color=s.foreground;textSize=s.size;typeface=Typeface.create(s.font,Typeface.NORMAL) }
            lines.forEachIndexed { i,line -> canvas.drawText(line,16f,16f+s.size+(s.size*1.45f*i),paint) }
            if(s.noise){val random=Random(42);repeat(s.width*s.height/50){image.setPixel(random.nextInt(s.width),random.nextInt(s.height),if(random.nextBoolean())Color.BLACK else Color.WHITE)}}
            val uri=context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,ContentValues().apply { put(MediaStore.Images.Media.DISPLAY_NAME,"ScamSentry_Quality_${s.name}.png");put(MediaStore.Images.Media.MIME_TYPE,"image/png") })!!
            try {
                context.contentResolver.openOutputStream(uri)!!.use { assertTrue(image.compress(Bitmap.CompressFormat.PNG,100,it)) }
                OcrReader(context).read(uri).use { draft ->
                    val extracted=draft.text.lowercase().replace(Regex("\\s+")," ").trim()
                    val chars=distance(reference.codePoints().toArray().toList(),extracted.codePoints().toArray().toList());val words=distance(reference.split(' '),extracted.split(' ').filter { it.isNotBlank() })
                    val analysis=AnalysisEngine.analyze(draft.text,model,"ocr")
                    rows.put(JSONObject().apply {put("slice",s.name);put("width",s.width);put("height",s.height);put("font",s.font);put("font_pixels",s.size.toDouble());put("noise_seed",if(s.noise)42 else JSONObject.NULL);put("reference_characters",reference.length);put("character_edits",chars);put("cer",chars.toDouble()/reference.length);put("reference_words",reference.split(' ').size);put("word_edits",words);put("wer",words.toDouble()/reference.split(' ').size);put("observed_risk",analysis.risk.wire);put("reference_risk",AnalysisEngine.analyze(reference,model).risk.wire);put("ocr_warning_present",draft.warning!=null)})
                    assertNotNull(draft.warning)
                }
            } finally { image.recycle();context.contentResolver.delete(uri,null,null) }
        }
        val additional=InstrumentationRegistry.getArguments().getString("additionalTestOutputDir")
        val output=(additional?.let{File(it)}?:context.getExternalFilesDir(null)!!).apply{mkdirs()}
        File(output,"ocr_quality.json").writeText(JSONObject().apply {put("scope","Six synthetic rendering slices of one fictional English message; extraction errors are separate from model classifications; not population accuracy");put("model_id",model.id);put("cases",rows);put("physical_phone",false)}.toString(2))
    }
}
