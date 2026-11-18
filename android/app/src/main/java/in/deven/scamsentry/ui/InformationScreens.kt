package `in`.deven.scamsentry.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import `in`.deven.scamsentry.AnalysisViewModel
import `in`.deven.scamsentry.BuildConfig
import `in`.deven.scamsentry.data.AppTheme
import `in`.deven.scamsentry.data.PresentationPreferences
import java.util.Locale
import org.json.JSONObject

private data class Topic(val title: String, val guidance: String, val example: String)

private val topics = listOf(
    Topic("Check the sender.", "Verify unexpected requests through an official number or app you already use.",
        "Fictional college office: our payment account changed. Transfer the fee today."),
    Topic("Keep codes private.", "A request to reveal a code deserves a separate check. Keep authentication secrets private.",
        "Fictional support notice: send your OTP to keep your account active."),
    Topic("Verify the payment.", "Check the recipient and purpose independently before paying a fee you did not expect.",
        "Fictional parcel notice: pay a release fee within ten minutes."),
    Topic("Find the actual host.", "Text before @ can look familiar. The host in this fictional address is review.test.",
        "https://bank.example@review.test/verify"),
    Topic("Respect the limits.", "A short or unclear message may leave too little context. Verify consequential requests independently.",
        "Fictional example: xqz — act now"),
    Topic("Separate the signals.", "A promotion and a request for a secret are different observations. Read the context of each request.",
        "Fictional stationery shop: weekend sale on notebooks. Reply STOP to unsubscribe."),
)

@Composable
internal fun LearnPage(fill: (String) -> Unit) {
    Page {
        Tag("PRACTICAL KNOW-HOW")
        Heading("Build your second-look instinct.")
        for (topic in topics) {
            HorizontalDivider(color = studio.rule)
            Heading(topic.title, MaterialTheme.typography.titleLarge)
            Text(topic.guidance, color = studio.muted)
            Panel {
                Tag("FICTIONAL EXAMPLE")
                SelectionContainer { Text(topic.example) }
                TextButton(onClick = { fill(topic.example) }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text("Use this example")
                }
            }
        }
    }
}

@Composable
internal fun SettingsPage(
    enabled: Boolean,
    presentation: PresentationPreferences,
    vm: AnalysisViewModel,
    onAbout: () -> Unit,
    onPrivacy: () -> Unit,
) {
    var confirmOff by remember { mutableStateOf(false) }
    Page {
        Tag("SETTINGS")
        Heading("Make it yours.")
        Panel {
            Heading("Optional summary history", MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    if (enabled) "Enabled. Saving is still manual." else "Off by default.",
                    Modifier.weight(1f), color = studio.muted,
                )
                Switch(
                    checked = enabled,
                    onCheckedChange = { if (it) vm.setHistory(true) else confirmOff = true },
                    modifier = Modifier.semantics { contentDescription = "Optional summary history" },
                )
            }
            Text("Turning history off also clears saved summaries.", color = studio.muted)
        }
        Panel {
            Heading("Appearance", MaterialTheme.typography.titleMedium)
            Column(Modifier.selectableGroup()) {
                for (choice in AppTheme.entries) {
                    Row(
                        Modifier.fillMaxWidth().heightIn(min = 48.dp).selectable(
                            selected = presentation.theme == choice,
                            onClick = { vm.setTheme(choice) },
                            role = Role.RadioButton,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        RadioButton(selected = presentation.theme == choice, onClick = null)
                        Text(choice.label)
                    }
                }
            }
            HorizontalDivider(color = studio.rule)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f)) {
                    Text("Pipeline motion", style = MaterialTheme.typography.titleMedium)
                    Text("Follows the device animation setting.", color = studio.muted)
                }
                Switch(
                    checked = presentation.animate,
                    onCheckedChange = vm::setMotion,
                    modifier = Modifier.semantics { contentDescription = "Pipeline motion" },
                )
            }
        }
        OutlinedButton(onClick = onAbout, shape = RectangleShape, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text("Model, scope and attribution")
        }
        OutlinedButton(onClick = onPrivacy, shape = RectangleShape, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text("Privacy and local storage")
        }
        Text("ScamSentry ${BuildConfig.VERSION_NAME} · Research preview", color = studio.muted, style = MaterialTheme.typography.bodyMedium)
    }
    if (confirmOff) AlertDialog(
        onDismissRequest = { confirmOff = false },
        title = { Text("Turn off history and clear summaries?") },
        text = { Text("Previously shared files remain in their destination apps.") },
        confirmButton = {
            Button(onClick = { vm.setHistory(false); confirmOff = false }, shape = RectangleShape) {
                Text("Turn off and clear")
            }
        },
        dismissButton = { TextButton(onClick = { confirmOff = false }) { Text("Keep history") } },
    )
}

@Composable
internal fun PrivacyPage() {
    Page {
        Tag("PRIVACY")
        Heading("Your text stays with you.")
        Panel {
            Heading("On-device analysis", MaterialTheme.typography.titleMedium)
            Text("ScamSentry checks message text and link structure on this device. The Android app requests no network, inbox, contacts, camera or broad storage access.")
        }
        Panel {
            Heading("Screenshot review", MaterialTheme.typography.titleMedium)
            Text("Choose an image using Android's photo picker. Bundled Latin OCR produces editable text. Inspect and correct it before running analysis.")
            Text("A draft lives in app memory. Rotation retains it. Closing the process discards it.", color = studio.muted)
        }
        Panel {
            Heading("Optional history and exports", MaterialTheme.typography.titleMedium)
            Text("History starts off. You can save up to 200 redacted summaries manually after enabling it. Turning it off clears those summaries.")
            Text("Sharing creates a reviewed redacted file. The recipient app manages any copies you send.", color = studio.muted)
        }
    }
}

@Composable
internal fun AboutPage() {
    val context = LocalContext.current
    val manifest = remember(context) {
        runCatching { context.assets.open("model/manifest.json").bufferedReader().use { JSONObject(it.readText()) } }.getOrNull()
    }
    val metrics = remember(context) {
        runCatching { context.assets.open("model/metrics.json").bufferedReader().use { JSONObject(it.readText()) } }.getOrNull()
    }
    Page {
        Tag("EVIDENCE")
        Heading("Know the model. Know its limits.")
        Panel {
            Heading(manifest?.optString("model_id", "Model unavailable") ?: "Model unavailable", MaterialTheme.typography.titleMedium)
            Text("Character TF-IDF and multinomial logistic regression with scalar-temperature calibration. The estimate measures resemblance to historical English short messages.")
            Text("The app reads link structure locally. Marathi, Hindi and Hinglish performance remain unvalidated.", color = studio.muted)
            if (manifest != null) {
                SelectionContainer {
                    Text("Schema ${manifest.optInt("schema_version")} · SHA-256\n${manifest.optString("model_sha256")}",
                        style = MaterialTheme.typography.labelMedium, color = studio.muted)
                }
            }
        }
        Panel {
            Tag("HISTORICAL GROUPED TEST")
            val macro = metrics?.optDouble("macro_f1")
            if (metrics != null && macro != null && macro.isFinite()) {
                Text(percent(macro), style = MaterialTheme.typography.displayLarge)
                Text("Macro F1 across ${metrics.optInt("records")} historical test records.", color = studio.muted)
                val interval = metrics.optJSONArray("group_bootstrap_macro_f1_95ci")
                if (interval != null && interval.length() == 2) {
                    Text("95% group bootstrap interval ${percent(interval.optDouble(0))}–${percent(interval.optDouble(1))}.", color = studio.muted)
                }
                val labels = listOf("Ordinary", "Promotion / spam", "Smishing-like")
                val matrix = metrics.optJSONArray("confusion_matrix")
                if (matrix != null && matrix.length() == labels.size) {
                    HorizontalDivider(color = studio.rule)
                    Heading("Predictions by actual class", MaterialTheme.typography.titleMedium)
                    labels.forEachIndexed { index, title ->
                        val row = matrix.optJSONArray(index)
                        if (row != null && row.length() == labels.size) {
                            val support = (0 until row.length()).sumOf { row.optInt(it) }
                            Heading("$title · $support records", MaterialTheme.typography.titleMedium)
                            labels.forEachIndexed { column, predicted ->
                                Text("$predicted ${row.optInt(column)}", color = studio.muted)
                            }
                        }
                    }
                }
            } else Text("The historical benchmark is unavailable in this build.", color = studio.muted)
            Text("Historical results describe this dataset. Current campaigns and physical-phone performance require separate evaluation.", color = studio.muted)
        }
        Panel {
            Heading("Credit and provenance", MaterialTheme.typography.titleLarge)
            Text("Deven Sachin Gaikwad · B.Tech Electronics and Communication Engineering (AI and ML) · DES Pune University.")
            Text("Dataset by Sandhya Mishra and Devpriya Soni (2022), Mendeley Data V1, DOI 10.17632/f45bkkt8pr.1, CC BY 4.0.", color = studio.muted)
            Text("Bundled type uses Archivo and Geist Mono under SIL OFL 1.1. See the packaged notices for dependencies and corpus preparation.", color = studio.muted)
        }
    }
}

private fun percent(value: Double) = String.format(Locale.ROOT, "%.2f%%", value * 100)
