package `in`.deven.scamsentry.benchmark

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import java.io.File
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/** Functional R8/resource-shrunk verification. This is not a timing benchmark. */
@RunWith(AndroidJUnit4::class)
class ReleaseLikeSmokeTest {
    @Test fun minifiedTextFlowShowsCredentialObservation() {
        val instrumentation=InstrumentationRegistry.getInstrumentation()
        val context=instrumentation.context
        val device=UiDevice.getInstance(instrumentation)
        val target="in.deven.scamsentry"
        device.executeShellCommand("pm clear $target")
        val launch=Intent().setClassName(target,"$target.MainActivity")
        context.startActivity(launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        device.wait(Until.findObject(By.text("Start inspecting")),10000)?.click()
        val editor=requireNotNull(device.wait(Until.findObject(By.desc("Message to analyze")),15000))
        editor.text="Fictional account team: send your OTP immediately to confirm access."
        UiScrollable(UiSelector().scrollable(true)).scrollTextIntoView("Analyze message")
        requireNotNull(device.findObject(By.text("Analyze message"))).click()
        assertTrue("Minified app must complete local analysis",device.wait(Until.hasObject(By.text("Higher risk")),20000))
        UiScrollable(UiSelector().scrollable(true)).scrollTextIntoView("Request for a secret")
        assertNotNull(device.findObject(By.text("Request for a secret")))
        val additional=InstrumentationRegistry.getArguments().getString("additionalTestOutputDir")
        val output=(additional?.let { File(it) } ?: File(context.getExternalFilesDir(null),"screenshots")).apply { mkdirs() }
        assertTrue(device.takeScreenshot(File(output,"minified_credential_result.png")))
    }
}
