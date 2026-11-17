package `in`.deven.scamsentry

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import `in`.deven.scamsentry.core.LinearModel
import `in`.deven.scamsentry.core.Policy
import `in`.deven.scamsentry.core.Rules
import `in`.deven.scamsentry.core.LegacyRulesV1
import `in`.deven.scamsentry.core.UrlInspector
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import `in`.deven.scamsentry.data.ReportExport
import `in`.deven.scamsentry.core.AnalysisResult
import `in`.deven.scamsentry.core.Risk
import java.io.File
import android.graphics.Bitmap
import android.content.Intent

@RunWith(AndroidJUnit4::class)
class WorkflowTest {
    @get:Rule val compose=createAndroidComposeRule<MainActivity>()
    private fun onboard() {
        compose.waitForIdle()
        if(compose.onAllNodesWithText("Start inspecting").fetchSemanticsNodes().isNotEmpty()) compose.onNodeWithText("Start inspecting").performClick()
    }
    @Test fun credentialRequestShowsEvidenceAndEditPath() {
        onboard()
        screenshot("01_analyze_home.png")
        compose.onNodeWithContentDescription("Message to analyze").performTextInput("Send your OTP immediately to keep your account active.")
        compose.onNodeWithText("Analyze message").performScrollTo().performClick()
        compose.waitUntil(20_000) { compose.onAllNodesWithText("Higher risk").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Request for a secret").assertExists()
        screenshot("02_credential_result.png")
        compose.onNodeWithText("Edit and analyze again").performScrollTo().performClick()
        compose.onNodeWithContentDescription("Message to analyze").assertExists()
    }
    @Test fun rotationKeepsDraftAndSharedInputReplacesPendingAnalysis() {
        onboard()
        val original="Fictional class reminder: bring the project notes."
        compose.onNodeWithContentDescription("Message to analyze").performTextInput(original)
        compose.activityRule.scenario.recreate()
        compose.onNodeWithContentDescription("Message to analyze").assertTextContains(original)
        compose.onNodeWithText("Analyze message").performScrollTo().performClick()
        val sharedActivity=compose.activity
        val launchIntent=Intent(sharedActivity.intent)
        val sharedText="Fictional team: send your OTP immediately."
        try {
        compose.runOnUiThread {
            val activity=sharedActivity
            activity.startActivity(Intent(activity,MainActivity::class.java).apply {
                action=Intent.ACTION_SEND; type="text/plain"
                putExtra(Intent.EXTRA_TEXT,sharedText)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            })
        }
        compose.waitUntil(10000) { compose.onAllNodesWithContentDescription("Message to analyze").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithContentDescription("Message to analyze").assertTextContains(sharedText)
        compose.onNodeWithText("Analyze message").performScrollTo().performClick()
        compose.waitUntil(20000) { compose.onAllNodesWithText("Request for a secret").fetchSemanticsNodes().isNotEmpty() }
        } finally {
            // ActivityScenario identifies its activity by the original launch intent.
            // Production onNewIntent correctly updates it; restore only for rule teardown.
            compose.runOnUiThread { sharedActivity.intent=launchIntent }
        }
    }
    private fun screenshot(name: String) {
        compose.waitForIdle()
        val bitmap=requireNotNull(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()) { "Device screenshot unavailable" }
        val additional=androidx.test.platform.app.InstrumentationRegistry.getArguments().getString("additionalTestOutputDir")
        val directory=additional?.let { File(it) } ?: File(compose.activity.getExternalFilesDir(null),"screenshots")
        val path=File(directory,name).apply { parentFile?.mkdirs() }
        try { path.outputStream().use { check(bitmap.compress(Bitmap.CompressFormat.PNG,100,it)) } }
        finally { bitmap.recycle() }
    }
    @Test fun packagedAndroidModelMatchesReference() {
        val assets=compose.activity.assets
        fun read(n:String)=assets.open("model/$n").bufferedReader().use { it.readText() }
        val model=LinearModel(read("model.json"),read("manifest.json"))
        val fixtures=JSONObject(read("golden.json")).getJSONArray("fixtures")
        for(i in 0 until fixtures.length()) {
            val f=fixtures.getJSONObject(i);val text=f.getString("text");val actual=model.predict(text)
            assertEquals(f.getString("normalized"),actual.normalized)
            for(k in 0..2) assertEquals(f.getJSONArray("probabilities").getDouble(k),actual.probabilities[k],1e-6)
            assertEquals(f.getString("category"),Policy.decide(text,actual.probabilities,LegacyRulesV1.inspect(text),UrlInspector.inspect(text),model.confidence,model.margin).wire)
        }
    }
    @Test fun exportDoesNotIncludeOriginalIdentifiers() {
        val result=AnalysisResult("fixture",1,"Call 9876543210 and visit https://review.test/?token=private","Typed",Risk.CHECKING,null,emptyList(),UrlInspector.inspect("https://review.test/?token=private"),emptyList(),"fixture",emptyList())
        val text=ReportExport.preview(result)
        assert(!text.contains("9876543210"));assert(!text.contains("token=private"));assert(!text.contains("review.test"))
    }
    @Test fun manualSummarySaveDeleteAndDisableControls() {
        onboard()
        compose.onNodeWithContentDescription("Settings and model information").performClick()
        if(compose.onAllNodesWithText("Off by default.").fetchSemanticsNodes().isNotEmpty()) compose.onNodeWithContentDescription("Optional summary history").performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText("Enabled. Saving is still manual.").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Analyze").performClick()
        compose.onNodeWithContentDescription("Message to analyze").performTextInput("Let's meet in the library after class.")
        compose.onNodeWithText("Analyze message").performScrollTo().performClick()
        compose.waitUntil(20_000) { compose.onAllNodesWithText("Save a local summary").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Save a local summary").performScrollTo().performClick()
        compose.onNodeWithText("Save summary").performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText("Summary saved locally").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("History").performClick()
        compose.onNodeWithText("Delete summary").performScrollTo().performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText("Nothing saved yet").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithContentDescription("Settings and model information").performClick()
        compose.onNodeWithContentDescription("Optional summary history").performClick()
        compose.onNodeWithText("Turn off and clear").performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText("Off by default.").fetchSemanticsNodes().isNotEmpty() }
    }
}
