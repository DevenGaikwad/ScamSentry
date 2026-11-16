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
import androidx.compose.ui.res.stringResource
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
import `in`.deven.scamsentry.R
import `in`.deven.scamsentry.core.*
import `in`.deven.scamsentry.data.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val samples=listOf(
    "Let's meet in the library after class. Please bring the project notes.",
    "Fictional account team: send your OTP immediately to prevent account suspension. Visit https://bank.example@review.test/verify",
    "Fictional stationery shop: weekend discount on notebooks. Reply STOP to unsubscribe.",
    "Urgent class reminder: bring your laboratory notebook before tomorrow's session.",
    "पैसे पाठवा. हा फक्त काल्पनिक नमुना आहे."
)

@Composable internal fun EditorPage(state: EditorState,vm: AnalysisViewModel,onImage: ()->Unit,historyEnabled: Boolean) {
    val context=LocalContext.current
    val messageDescription=stringResource(R.string.message_description)
    var sampleMenu by remember { mutableStateOf(false) }
    Page {
        Tag(if(state.stage==Stage.REVIEWING) "SCREENSHOT REVIEW" else "LOCAL ANALYSIS",Studio.Purple)
        Text(if(state.stage==Stage.REVIEWING) "Read it. Correct it.\nThen inspect it." else "A second look\nbefore you act.",style=MaterialTheme.typography.headlineLarge)
        Text("Inspect suspicious messages and links with clear observations and an on-device research model.",color=Studio.Muted)
        if(state.preview!=null && !state.preview.isRecycled) {
            Image(state.preview.asImageBitmap(),contentDescription="Selected screenshot; inspect the extracted text below",modifier=Modifier.fillMaxWidth().heightIn(max=180.dp).clip(RoundedCornerShape(16.dp)))
        }
        state.ocrWarning?.let { Panel { Text("Review extracted text",color=Studio.Amber,fontWeight=FontWeight.SemiBold); Text(it,color=Studio.Muted) } }
        Panel {
            Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween) { Text("Your message",style=MaterialTheme.typography.titleMedium); Tag(state.source) }
            OutlinedTextField(value=state.text,onValueChange={ vm.edit(it) },placeholder={ Text("Paste a message or a link here…") },modifier=Modifier.fillMaxWidth().heightIn(min=170.dp).semantics { contentDescription=messageDescription },shape=RoundedCornerShape(12.dp),supportingText={ Text("${state.text.codePointCount(0,state.text.length)} / 4,000 characters • English research scope") },isError=state.text.codePointCount(0,state.text.length)>4000)
            state.error?.let { Text(it,color=Studio.Red,modifier=Modifier.semantics { liveRegion=LiveRegionMode.Polite }) }
            Primary(stringResource(R.string.analyze_message),state.text.isNotBlank() && state.text.codePointCount(0,state.text.length)<=4000,vm::analyze)
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick={
                    val clipboard=context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val value=clipboard.primaryClip?.getItemAt(0)?.text?.toString()
                    vm.paste(value)
                },modifier=Modifier.weight(1f).heightIn(min=48.dp)) { Text(stringResource(R.string.paste)) }
                TextButton(onClick=vm::clear,modifier=Modifier.heightIn(min=48.dp)) { Text(stringResource(R.string.clear)) }
            }
        }
        OutlinedButton(onClick=onImage,modifier=Modifier.fillMaxWidth().heightIn(min=56.dp),shape=RoundedCornerShape(14.dp)) { Mark(kind="image"); Spacer(Modifier.width(12.dp)); Text(stringResource(if(state.stage==Stage.REVIEWING) R.string.replace_screenshot else R.string.choose_screenshot)) }
        Box { TextButton(onClick={ sampleMenu=true },modifier=Modifier.heightIn(min=48.dp)) { Text("Try a fictional example") }; DropdownMenu(expanded=sampleMenu,onDismissRequest={ sampleMenu=false }) { listOf("Class coordination","Credential request","Promotional offer","Legitimate urgency","Unsupported language").forEachIndexed { i,label -> DropdownMenuItem(text={ Text(label) },onClick={ vm.edit(samples[i],"Fictional demo");sampleMenu=false }) } } }
        Row(horizontalArrangement=Arrangement.spacedBy(12.dp),verticalAlignment=Alignment.CenterVertically) { Mark(color=Studio.Green); Text(if(historyEnabled) "History is enabled. Saving a redacted summary is still manual." else "You choose what to inspect. History is off.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium) }
    }
}

