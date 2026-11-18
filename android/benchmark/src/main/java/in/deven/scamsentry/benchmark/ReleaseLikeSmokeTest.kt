package `in`.deven.scamsentry.benchmark

import android.content.ContentValues
import android.content.Intent
import android.os.SystemClock
import android.provider.MediaStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import java.io.File
import java.util.regex.Pattern
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/** External functional verification of the minified app; no phone timing claim. */
@RunWith(AndroidJUnit4::class)
class ReleaseLikeSmokeTest {
    private val instrumentation get()=InstrumentationRegistry.getInstrumentation()
    private val context get()=instrumentation.context
    private val target="in.deven.scamsentry"
    private fun output():File {
        val additional=InstrumentationRegistry.getArguments().getString("additionalTestOutputDir")
        return (additional?.let { File(it) } ?: File(context.getExternalFilesDir(null),"screenshots")).apply { mkdirs() }
    }
    private fun device()=UiDevice.getInstance(instrumentation)
    private fun capture(name:String) { assertTrue(device().takeScreenshot(File(output(),name))) }
    private fun checkedJourney(name:String,body:()->Unit) {
        try { body() }
        catch(failure:Throwable) {
            runCatching { device().dumpWindowHierarchy(File(output(),"failure_"+name+".xml")) }
            runCatching { capture("failure_"+name+".png") }
            throw failure
        }
    }
    private fun startClean():UiDevice {
        val device=device()
        assertTrue(device.executeShellCommand("pm clear "+target).contains("Success"))
        context.startActivity(Intent().setClassName(target,target+".MainActivity")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        requireNotNull(device.wait(Until.findObject(By.text("Start inspecting")),15000)).click()
        return device
    }
    private fun analyzeCredential(device:UiDevice) {
        UiScrollable(UiSelector().scrollable(true).packageName(target)).scrollTextIntoView("Analyze message")
        requireNotNull(device.findObject(By.text("Analyze message"))).click()
        assertTrue("Minified app must complete local analysis",
            device.wait(Until.hasObject(By.text("Higher risk")),30000))
        UiScrollable(UiSelector().scrollable(true).packageName(target)).scrollTextIntoView("Request for a secret")
        assertNotNull(device.findObject(By.text("Request for a secret")))
    }
    @Test fun minifiedTextFlowShowsCredentialObservation()=checkedJourney("text") {
        val device=startClean()
        assertTrue(device.wait(Until.hasObject(By.text("A second look. Before you act.")), 10000))
        capture("minified_analyze_home.png")
        val editor=requireNotNull(device.wait(Until.findObject(By.clazz("android.widget.EditText").pkg(target)),15000))
        val message="Fictional account team: send your OTP immediately to confirm access."
        editor.text=message
        assertTrue(editor.wait(Until.textEquals(message),5000))
        analyzeCredential(device)
        capture("minified_credential_result.png")
    }
    @Test fun minifiedScreenshotReviewRunsBundledOcrAndUsesEditedText()=checkedJourney("ocr") {
        val resolver=context.contentResolver
        val uri=requireNotNull(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME,"ScamSentry_Minified_OCR.png")
            put(MediaStore.Images.Media.MIME_TYPE,"image/png")
            put(MediaStore.Images.Media.IS_PENDING,1)
        }))
        try {
            resolver.openOutputStream(uri)!!.use { out ->
                context.assets.open("demo/credential_screenshot.png").use { it.copyTo(out) }
            }
            resolver.update(uri,ContentValues().apply { put(MediaStore.Images.Media.IS_PENDING,0) },null,null)
            val device=startClean()
            UiScrollable(UiSelector().scrollable(true).packageName(target)).scrollTextIntoView("Choose a screenshot")
            requireNotNull(device.findObject(By.text("Choose a screenshot"))).click()
            // Same thumbnail resource used by the AOSP PhotoPicker CTS helpers.
            val thumbnail=requireNotNull(device.wait(
                Until.findObject(By.res(Pattern.compile(".*:id/icon_thumbnail"))),20000)) {
                "The owned fictional fixture was not visible in the system photo picker"
            }
            thumbnail.click()
            val deadline=SystemClock.uptimeMillis()+30000
            var recognized:UiObject2?=null
            while(SystemClock.uptimeMillis()<deadline && recognized==null) {
                val editor=device.findObject(By.clazz("android.widget.EditText").pkg(target))
                if(editor!=null && editor.text.orEmpty().contains("OTP",ignoreCase=true)) {
                    recognized=editor
                } else {
                    val scroll=UiScrollable(UiSelector().scrollable(true).packageName(target))
                    if(scroll.exists()) {
                        scroll.scrollToBeginning(4)
                        scroll.scrollTextIntoView("Your message")
                    }
                    SystemClock.sleep(100)
                }
            }
            val editor=requireNotNull(recognized) { "Bundled OCR did not populate the review editor with the fixture's OTP text" }
            capture("minified_ocr_review.png")
            val corrected="Fictional user-corrected message: send your OTP immediately."
            editor.text=corrected
            assertTrue("The OCR review must accept a user correction",editor.wait(Until.textEquals(corrected),5000))
            analyzeCredential(device)
            capture("minified_ocr_result.png")
        } finally { resolver.delete(uri,null,null) }
    }
}
