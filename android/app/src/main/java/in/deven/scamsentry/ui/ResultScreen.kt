package `in`.deven.scamsentry.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import `in`.deven.scamsentry.core.AnalysisResult
import `in`.deven.scamsentry.core.Risk
import java.util.Locale

@Composable internal fun ResultPage(result: AnalysisResult,historyEnabled: Boolean,saved: Boolean,onEdit: ()->Unit,onSave: ()->Unit,onExport: ()->Unit,onLearn: ()->Unit) {
    val color=studio.risk(result.risk)
    val estimate=result.estimate
    var technical by rememberSaveable(result.id) { mutableStateOf(false) }
    var original by rememberSaveable(result.id) { mutableStateOf(false) }
    Page {
        Tag("ANALYSIS COMPLETE",color)
        Panel(color=studio.raised) {
            Tag(when (result.risk) {
                Risk.HIGHER -> "CAUTION  !"
                Risk.CHECKING -> "REVIEW  ?"
                Risk.LOWER -> "FEWER INDICATORS"
                Risk.UNABLE -> "LIMITED  —"
            }, color)
            Text(result.risk.title,style=MaterialTheme.typography.headlineMedium,color=color,modifier=Modifier.semantics { heading(); liveRegion=LiveRegionMode.Polite })
            Text(result.risk.description)
            Text("${result.observations.size} observed patterns · ${result.urls.size} links inspected",color=studio.muted,style=MaterialTheme.typography.bodyMedium)
        }
        Heading("What we observed", MaterialTheme.typography.titleLarge)
        if(result.observations.isEmpty()) Panel { Text("No configured message patterns found"); Text("A missing pattern does not establish legitimacy.",color=studio.muted) }
        result.observations.forEach { o -> Panel { Tag("OBSERVED PATTERN",if(o.severity=="high") studio.danger else studio.caution); Text(o.title,style=MaterialTheme.typography.titleMedium); Text(o.detail,color=studio.muted) } }
        result.urls.forEachIndexed { i,u -> Panel {
            Tag("LINK ${i+1} • TEXT ONLY")
            Text("Actual host",style=MaterialTheme.typography.labelLarge,color=studio.muted)
            SelectionContainer { Text(u.host?:"Unable to parse reliably",fontFamily=Studio.Mono,color=studio.ink) }
            SelectionContainer { Text(u.original,style=MaterialTheme.typography.bodyMedium,color=studio.muted) }
            u.codes.forEach { code -> Text(when(code) { "userinfo"->"Text before @ is not the destination. The actual host is shown above."; "idn"->"This host uses an internationalized name. Encoded text is shown for inspection; this alone does not indicate fraud."; "unencrypted_scheme"->"The address uses HTTP. HTTPS alone would not verify authenticity either."; "shortener"->"The destination is shortened; offline analysis cannot follow the redirect."; "long_address"->"This is a long address. Length alone does not establish risk."; else->"This address is malformed or uses an unsupported scheme." },color=studio.caution,style=MaterialTheme.typography.bodyMedium) }
        } }
        Panel {
            Tag("MODEL INTERPRETATION",studio.muted)
            Text(if(estimate!=null) "Text resemblance: ${listOf("ordinary messages","promotion or spam","smishing-like messages")[estimate.probabilities.indices.maxBy { estimate.probabilities[it] }]}" else "Message interpretation is limited",style=MaterialTheme.typography.titleMedium)
            Text("Statistical resemblance is separate from the observations above. It cannot identify who sent the message.",color=studio.muted)
            TextButton(onClick={ technical=!technical }) { Text(if(technical) "Hide model details" else "Show model details") }
            if(technical) {
                Text("${result.modelId} · normalizer ${result.normalizerVersion} · policy ${result.policyVersion}",color=studio.muted,style=MaterialTheme.typography.bodyMedium)
                Text("Scalar-temperature calibration on a separate historical partition. Estimates may not transfer to new campaigns.",color=studio.muted,style=MaterialTheme.typography.bodyMedium)
                if (estimate != null) {
                    val classes = listOf("Ordinary", "Promotion / spam", "Smishing-like")
                    classes.forEachIndexed { i, name ->
                        Text("$name ${String.format(Locale.ROOT, "%.1f%%", estimate.probabilities[i] * 100)}",
                            style = MaterialTheme.typography.labelMedium)
                    }
                }
                result.contributions.forEach { Text("Fragment ‘${it.fragment}’: ${"%.4f".format(it.difference)} toward predicted versus ordinary logit",style=MaterialTheme.typography.bodyMedium) }
                if(result.contributions.isNotEmpty()) Text("Overlapping fragments are statistical cues, not semantic proof.",color=studio.muted,style=MaterialTheme.typography.bodyMedium)
            }
        }
        Panel { Text("Pause and verify",style=MaterialTheme.typography.titleMedium); Text("Use a phone number or app you already trust. Keep authentication secrets private and confirm unexpected payments independently."); result.limitations.forEach { Text(it,color=studio.muted,style=MaterialTheme.typography.bodyMedium) } }
        Primary("Edit and analyze again",action=onEdit)
        if(historyEnabled) OutlinedButton(onClick=onSave,enabled=!saved,shape=RectangleShape,modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) { Text(if(saved) "Summary saved locally" else "Save a local summary") }
        OutlinedButton(onClick=onExport,shape=RectangleShape,modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) { Text("Preview redacted report") }
        TextButton(onClick={ original=!original }) { Text(if(original) "Hide analyzed text" else "Review analyzed text") }
        if(original) Panel { Text(result.text,style=MaterialTheme.typography.bodyMedium) }
        TextButton(onClick=onLearn) { Text("Learn what to check") }
    }
}

