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

@Composable internal fun ResultPage(result: AnalysisResult,historyEnabled: Boolean,saved: Boolean,onEdit: ()->Unit,onSave: ()->Unit,onExport: ()->Unit,onLearn: ()->Unit) {
    val color=Studio.risk(result.risk)
    val estimate=result.estimate
    var technical by rememberSaveable(result.id) { mutableStateOf(false) }
    var original by rememberSaveable(result.id) { mutableStateOf(false) }
    Page {
        Tag("ANALYSIS COMPLETE",color)
        Panel(color=Studio.Raised) {
            Mark(color=color,modifier=Modifier.size(48.dp))
            Text(result.risk.title,style=MaterialTheme.typography.headlineMedium,color=color,modifier=Modifier.semantics { heading(); liveRegion=LiveRegionMode.Polite })
            Text(result.risk.description)
            Text("${result.observations.size} observed patterns · ${result.urls.size} links inspected",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium)
        }
        Text("What we observed",style=MaterialTheme.typography.titleLarge)
        if(result.observations.isEmpty()) Panel { Text("No configured message patterns found"); Text("A missing pattern does not establish legitimacy.",color=Studio.Muted) }
        result.observations.forEach { o -> Panel { Tag("OBSERVED PATTERN",if(o.severity=="high") Studio.Red else Studio.Amber); Text(o.title,style=MaterialTheme.typography.titleMedium); Text(o.detail,color=Studio.Muted) } }
        result.urls.forEachIndexed { i,u -> Panel {
            Tag("LINK ${i+1} • TEXT ONLY")
            Text("Actual host",style=MaterialTheme.typography.labelLarge,color=Studio.Muted)
            SelectionContainer { Text(u.host?:"Unable to parse reliably",fontFamily=FontFamily.Monospace,color=Studio.Blue) }
            SelectionContainer { Text(u.original,style=MaterialTheme.typography.bodyMedium,color=Studio.Muted) }
            u.codes.forEach { code -> Text(when(code) { "userinfo"->"Text before @ is not the destination. The actual host is shown above."; "idn"->"This host uses an internationalized name. Encoded text is shown for inspection; this alone does not indicate fraud."; "unencrypted_scheme"->"The address uses HTTP. HTTPS alone would not verify authenticity either."; "shortener"->"The destination is shortened; offline analysis cannot follow the redirect."; "long_address"->"This is a long address. Length alone does not establish risk."; else->"This address is malformed or uses an unsupported scheme." },color=Studio.Amber,style=MaterialTheme.typography.bodyMedium) }
        } }
        Panel {
            Tag("MODEL INTERPRETATION",Studio.Purple)
            Text(if(estimate!=null) "Text resemblance: ${listOf("ordinary messages","promotion or spam","smishing-like messages")[estimate.probabilities.indices.maxBy { estimate.probabilities[it] }]}" else "Message interpretation is limited",style=MaterialTheme.typography.titleMedium)
            Text("Statistical resemblance is separate from the observations above. It cannot identify who sent the message.",color=Studio.Muted)
            TextButton(onClick={ technical=!technical }) { Text(if(technical) "Hide model details" else "Show model details") }
            if(technical) {
                Text("${result.modelId} · normalizer ${result.normalizerVersion} · policy ${result.policyVersion}",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium)
                Text("Scalar-temperature calibration on a separate historical partition. Estimates may not transfer to new campaigns.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium)
                result.contributions.forEach { Text("Fragment ‘${it.fragment}’: ${"%.4f".format(it.difference)} toward predicted versus ordinary logit",style=MaterialTheme.typography.bodyMedium) }
                if(result.contributions.isNotEmpty()) Text("Overlapping fragments are statistical cues, not semantic proof.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium)
            }
        }
        Panel { Text("Pause and verify",style=MaterialTheme.typography.titleMedium); Text("Use a phone number or app you already trust. Keep authentication secrets private and confirm unexpected payments independently."); result.limitations.forEach { Text(it,color=Studio.Muted,style=MaterialTheme.typography.bodyMedium) } }
        Primary("Edit and analyze again",action=onEdit)
        if(historyEnabled) OutlinedButton(onClick=onSave,enabled=!saved,modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) { Text(if(saved) "Summary saved locally" else "Save a local summary") }
        OutlinedButton(onClick=onExport,modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) { Text("Preview redacted report") }
        TextButton(onClick={ original=!original }) { Text(if(original) "Hide analyzed text" else "Review analyzed text") }
        if(original) Panel { Text(result.text,style=MaterialTheme.typography.bodyMedium) }
        TextButton(onClick=onLearn) { Text("Learn what to check") }
    }
}

