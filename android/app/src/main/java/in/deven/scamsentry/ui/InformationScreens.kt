package `in`.deven.scamsentry.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.*
import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import `in`.deven.scamsentry.*
import `in`.deven.scamsentry.core.*
import `in`.deven.scamsentry.data.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable internal fun LearnPage() {
    val topics=listOf(
        "A familiar name is not identity" to "Sender names, logos and screenshots can be copied. Contact the organization using a channel you independently know.",
        "Keep one-time codes private" to "A notification containing an OTP differs from a request to reveal it. Do not share authentication codes, passwords or PINs.",
        "Give payments a second check" to "Verify the recipient and purpose independently. A deadline or small fee does not establish legitimacy.",
        "Inspect the actual host" to "In https://bank.example@review.test, the destination host is review.test. HTTPS alone does not establish ownership or trust.",
        "Promotion differs from smishing" to "Advertising can be unwanted without proving credential theft. The model keeps ordinary, promotional and smishing-like labels separate.",
        "Uncertainty is useful" to "A short message, unclear screenshot or unsupported language may be impossible to assess well. Edit the text or seek trusted help.",
        "Lower risk is not verified safe" to "Models miss unfamiliar patterns. Your sender and the current website were not authenticated by this app."
    )
    Page { Tag("A LITTLE KNOW-HOW",Studio.Purple); Text("Build your\nsecond-look instinct.",style=MaterialTheme.typography.headlineLarge); Text("Practical checks, without panic. Link examples here are fictional and never opened.",color=Studio.Muted); topics.forEachIndexed { i,(title,body) -> Panel { Tag("${i+1}",Studio.Purple); Text(title,style=MaterialTheme.typography.titleLarge); Text(body,color=Studio.Muted) } } }
}

@Composable internal fun SettingsPage(enabled: Boolean,vm: AnalysisViewModel,onAbout: ()->Unit) {
    var confirmOff by remember { mutableStateOf(false) }
    Page {
        Tag("CONTROL CENTER")
        Text("Keep it personal.\nKeep it local.",style=MaterialTheme.typography.headlineLarge)
        Panel { Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) { Column(Modifier.weight(1f)) { Text("Optional summary history",style=MaterialTheme.typography.titleMedium); Text(if(enabled) "Enabled. Saving is still manual." else "Off by default.",color=Studio.Muted) }; Switch(checked=enabled,onCheckedChange={ if(!it) confirmOff=true else vm.setHistory(true) },modifier=Modifier.semantics { contentDescription="Optional summary history" }) }; Text("Turning this off also clears saved summaries. Raw messages, links and images are never part of history.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium) }
        Panel { Text("Privacy by design",style=MaterialTheme.typography.titleLarge); Text("No account, project analytics or remote explanation service. The app requests no inbox, contact, camera or broad storage permission. Images are chosen through the system picker.",color=Studio.Muted); Text("Draft text and screenshot previews live in memory. Rotation retains the active draft; process death discards it. Exporting creates a redacted summary only, after your review.",color=Studio.Muted); Text("The bundled ML Kit OCR dependency is configured without app network permission. SDK operational diagnostics and fresh offline behavior still need verification on your device.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium) }
        OutlinedButton(onClick=onAbout,modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) { Text("Model, scope and attribution") }
        Text("ScamSentry ${BuildConfig.VERSION_NAME} · Research preview",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium)
    }
    if(confirmOff) AlertDialog(onDismissRequest={ confirmOff=false },title={ Text("Turn off history and clear summaries?") },text={ Text("Previously shared files remain in their destination apps.") },confirmButton={ Button(onClick={ vm.setHistory(false);confirmOff=false }) { Text("Turn off and clear") } },dismissButton={ TextButton(onClick={ confirmOff=false }) { Text("Keep history") } })
}

@Composable internal fun AboutPage() {
    val context=LocalContext.current
    val manifest=remember { runCatching { org.json.JSONObject(context.assets.open("model/manifest.json").bufferedReader().use { it.readText() }) }.getOrNull() }
    val metrics=remember { runCatching { org.json.JSONObject(context.assets.open("model/metrics.json").bufferedReader().use { it.readText() }) }.getOrNull() }
    Page {
        Tag("TRANSPARENT BY DEFAULT",Studio.Purple)
        Text("Know the model.\nKnow its limits.",style=MaterialTheme.typography.headlineLarge)
        Panel { Text(manifest?.optString("model_id")?:"Model unavailable",style=MaterialTheme.typography.titleLarge); Text("Character TF-IDF + multinomial logistic regression. Direct Kotlin inference with scalar-temperature calibration.",color=Studio.Muted); Text("Research scope: English short messages. Romanized and mixed-language coverage is not validated. URL checks are lexical observations; no learned URL classifier or live reputation lookup.",color=Studio.Muted) }
        Panel { Text("Measured evidence",style=MaterialTheme.typography.titleLarge); if(metrics!=null) { Text("Held-out macro-F1: ${"%.3f".format(metrics.optDouble("macro_f1"))}",color=Studio.Purple); Text("${metrics.optInt("records")} historical test records, grouped protocol ss-group-v1. See the packaged evaluation report for class support and uncertainty.",color=Studio.Muted) } else Text("Final evaluation is not bundled in this build. Do not infer accuracy from demo examples.",color=Studio.Muted); Text("A historical benchmark does not establish performance on current Indian scam campaigns. Phone latency and offline OCR require device checks.",color=Studio.Muted) }
        Panel { Text("Attribution",style=MaterialTheme.typography.titleLarge); Text("Deven Sachin Gaikwad · B.Tech ECE, AI/ML · DES Pune University. Mobile App Development and Cybersecurity project.",color=Studio.Muted); Text("Dataset: Sandhya Mishra and Devpriya Soni (2022), Mendeley Data V1, DOI 10.17632/f45bkkt8pr.1. CC BY 4.0. Changes: minimization, deduplication, grouping, quarantine and normalization. No endorsement implied.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium); Text("Space Grotesk and Inter: SIL Open Font License. AndroidX and Kotlin: Apache 2.0. ML Kit is provided under Google's applicable SDK terms. Full notices are in the project package.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium) }
    }
}
