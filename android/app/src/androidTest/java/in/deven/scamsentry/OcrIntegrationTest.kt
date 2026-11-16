package `in`.deven.scamsentry

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import `in`.deven.scamsentry.data.InputFailure
import `in`.deven.scamsentry.data.InputFailureCode
import `in`.deven.scamsentry.data.OcrReader
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OcrIntegrationTest {
    @Test fun bundledLatinOcrReadsOriginalFixtureAndRejectsMalformedBytes()=runBlocking {
        assumeTrue(Build.VERSION.SDK_INT>=29)
        val instrumentation=InstrumentationRegistry.getInstrumentation()
        val context=instrumentation.targetContext
        for((asset,malformed) in listOf("demo/credential_screenshot.png" to false,"demo/malformed.png" to true)) {
            val uri=context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,ContentValues().apply { put(MediaStore.Images.Media.DISPLAY_NAME,"ScamSentry_Test_Fixture.png");put(MediaStore.Images.Media.MIME_TYPE,"image/png") })!!
            try {
                context.contentResolver.openOutputStream(uri)!!.use { out -> instrumentation.context.assets.open(asset).use { it.copyTo(out) } }
                if(malformed) {
                    try { OcrReader(context).read(uri);fail("Malformed fixture was accepted") }
                    catch(e:InputFailure) { assertEquals(InputFailureCode.DECODE_FAILED,e.code) }
                } else {
                    val draft=OcrReader(context).read(uri)
                    assertTrue("Original fictional OTP text must be extracted",draft.text.contains("OTP",ignoreCase=true))
                    assertNotNull(draft.warning)
                    draft.preview.recycle()
                }
            } finally { context.contentResolver.delete(uri,null,null) }
        }
    }
}
