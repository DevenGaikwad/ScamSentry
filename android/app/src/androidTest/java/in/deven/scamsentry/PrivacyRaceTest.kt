package `in`.deven.scamsentry

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import `in`.deven.scamsentry.data.OcrDraft
import `in`.deven.scamsentry.data.OcrReader
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrivacyRaceTest {
    private val instrumentation get()=InstrumentationRegistry.getInstrumentation()
    private val app get()=instrumentation.targetContext.applicationContext as ScamSentryApp
    private fun main(block:()->Unit)=instrumentation.runOnMainSync(block)
    private suspend fun waitFor(predicate:()->Boolean)=withTimeout(15000) { while(!predicate())delay(10) }
    private fun fixture(asset:String):Uri {
        val uri=app.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME,"ScamSentry_Race_Fixture.png")
            put(MediaStore.Images.Media.MIME_TYPE,"image/png")
        })!!
        app.contentResolver.openOutputStream(uri)!!.use { out -> instrumentation.context.assets.open(asset).use { it.copyTo(out) } }
        return uri
    }
    @Test fun cancellationAfterNativeCompletionRecyclesUndeliveredDraft()=runBlocking {
        val started=CompletableDeferred<Unit>();val finish=CompletableDeferred<Unit>()
        val bitmap=Bitmap.createBitmap(8,8,Bitmap.Config.ARGB_8888)
        val reader=OcrReader(app) {
            withContext(NonCancellable) { started.complete(Unit);finish.await();OcrDraft("fictional fixture",null,bitmap) }
        }
        val job=launch(Dispatchers.Default) { reader.read(Uri.parse("content://scamsentry.test/controlled")) }
        try { withTimeout(10000){started.await()};job.cancel();finish.complete(Unit);job.join();assertTrue(bitmap.isRecycled) }
        finally { finish.complete(Unit);job.cancelAndJoin();if(!bitmap.isRecycled)bitmap.recycle() }
    }
    @Test fun rapidImageReplacementKeepsLatestOutcomeAndClearOwnsPreview()=runBlocking {
        val good=fixture("demo/credential_screenshot.png");val bad=fixture("demo/malformed.png")
        val vm=AnalysisViewModel(app);val store=ViewModelStore().apply { put("race",vm) }
        try {
            repeat(3) {
                main { vm.selectImage(good) };delay(20)
                main { vm.selectImage(bad) }
                waitFor { vm.state.value.stage==Stage.EDITING&&vm.state.value.error!=null }
                assertNull(vm.state.value.preview);assertNull(vm.state.value.result)
            }
            main { vm.selectImage(bad);vm.selectImage(good) }
            waitFor { vm.state.value.stage==Stage.REVIEWING }
            val preview=requireNotNull(vm.state.value.preview)
            assertTrue(vm.state.value.text.contains("OTP",ignoreCase=true));assertNull(vm.state.value.error)
            main { vm.clear() }
            assertTrue(preview.isRecycled);assertNull(vm.state.value.preview)
        } finally {
            main { store.clear() }
            app.contentResolver.delete(good,null,null);app.contentResolver.delete(bad,null,null)
        }
    }
    @Test fun queuedSaveCannotSurviveHistoryBeingDisabled()=runBlocking {
        app.history.clear();app.settings.history(true)
        val vm=AnalysisViewModel(app);val store=ViewModelStore().apply { put("history",vm) }
        val observer=launch(Dispatchers.Main) { vm.historyEnabled.collect() }
        try {
            waitFor { vm.historyEnabled.value }
            main { vm.edit("Fictional class reminder: bring the project notes.");vm.analyze() }
            waitFor { vm.state.value.stage==Stage.RESULT }
            main { vm.setHistory(false);vm.save() }
            waitFor { !vm.historyEnabled.value }
            assertNull(withTimeoutOrNull(1500) { app.history.observe().first { it.isNotEmpty() } })
            assertEquals(0,app.history.count())
            assertFalse(vm.state.value.saved)
        } finally {
            observer.cancelAndJoin();main { store.clear() };app.settings.history(false);app.history.clear()
        }
    }
}
