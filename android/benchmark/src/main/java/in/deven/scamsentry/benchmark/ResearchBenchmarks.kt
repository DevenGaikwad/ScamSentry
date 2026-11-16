package `in`.deven.scamsentry.benchmark
import androidx.benchmark.macro.*
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Physical-phone measurements only. No fabricated Baseline Profile is supplied. */
@RunWith(AndroidJUnit4::class)
class ResearchBenchmarks {
    @get:Rule val benchmark = MacrobenchmarkRule()
    private val target = "in.deven.scamsentry"
    @Test fun coldStartup() = benchmark.measureRepeated(packageName=target,metrics=listOf(StartupTimingMetric()),iterations=10,startupMode=StartupMode.COLD,compilationMode=CompilationMode.None(),setupBlock={pressHome()}) { startActivityAndWait() }
    @Test fun warmTextAndResultScroll() = benchmark.measureRepeated(packageName=target,metrics=listOf(FrameTimingMetric()),iterations=10,startupMode=StartupMode.WARM,compilationMode=CompilationMode.None(),setupBlock={
        pressHome(); startActivityAndWait()
        device.wait(Until.findObject(By.text("Start inspecting")),2000)?.click()
        device.findObject(By.text("Edit and analyze again"))?.click()
    }) {
        val editor=requireNotNull(device.wait(Until.findObject(By.desc("Message to analyze")),10000))
        editor.text="Fictional account team: send your OTP immediately."
        device.findObject(By.text("Analyze message")).click()
        check(device.wait(Until.hasObject(By.text("Higher risk")),15000))
        device.swipe(device.displayWidth/2,device.displayHeight*4/5,device.displayWidth/2,device.displayHeight/3,30)
    }
    @Test fun navigationAndLearningScroll() = benchmark.measureRepeated(packageName=target,metrics=listOf(FrameTimingMetric()),iterations=10,startupMode=StartupMode.WARM,compilationMode=CompilationMode.None(),setupBlock={pressHome();startActivityAndWait();device.wait(Until.findObject(By.text("Start inspecting")),2000)?.click()}) {
        device.findObject(By.text("Learn")).click()
        device.swipe(device.displayWidth/2,device.displayHeight*4/5,device.displayWidth/2,device.displayHeight/3,30)
        device.findObject(By.text("History")).click()
        device.findObject(By.text("Analyze")).click()
    }
}
