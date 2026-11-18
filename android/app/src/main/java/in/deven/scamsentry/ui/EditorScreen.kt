package `in`.deven.scamsentry.ui

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.deven.scamsentry.AnalysisViewModel
import `in`.deven.scamsentry.EditorState
import `in`.deven.scamsentry.R
import `in`.deven.scamsentry.Stage
import `in`.deven.scamsentry.core.TextContract

private val samples=listOf(
    "Let's meet in the library after class. Please bring the project notes.",
    "Fictional account team: send your OTP immediately to prevent account suspension. Visit https://bank.example@review.test/verify",
    "Fictional stationery shop: weekend discount on notebooks. Reply STOP to unsubscribe.",
    "Urgent class reminder: bring your laboratory notebook before tomorrow's session.",
    "पैसे पाठवा. हा फक्त काल्पनिक नमुना आहे."
)

@Composable internal fun EditorPage(state: EditorState,vm: AnalysisViewModel,onImage: ()->Unit,historyEnabled: Boolean, animate: Boolean = false) {
    val context=LocalContext.current
    val messageDescription=stringResource(R.string.message_description)
    var sampleMenu by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf(false) }
    val reviewing = state.stage == Stage.REVIEWING
    val count = state.text.codePointCount(0, state.text.length)
    Page {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Tag(if (reviewing) "SCREENSHOT REVIEW" else "LOCAL INSPECTION")
            Heading(if (reviewing) "Read it. Correct it. Then inspect it." else "A second look. Before you act.")
            if (!reviewing) EditorialPipeline(animate && !editing && !sampleMenu)
            Text(
                if (reviewing) "Correct the extracted text below before choosing Analyze."
                else "Inspect a message or link on this device.",
                color = studio.muted,
            )
        }
        if(state.preview!=null && !state.preview.isRecycled) {
            Image(state.preview.asImageBitmap(),contentDescription="Selected screenshot; inspect the extracted text below",modifier=Modifier.fillMaxWidth().heightIn(max = 160.dp))
        }
        state.ocrWarning?.let { Panel { Text("Review extracted text",color=studio.caution,fontWeight=FontWeight.SemiBold); Text(it,color=studio.muted) } }
        Panel {
            Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween) { Text("Your message",style=MaterialTheme.typography.titleMedium); Tag(state.source) }
            OutlinedTextField(value=state.text,onValueChange={ vm.edit(it) },placeholder={ Text("Paste a message or a link here…") },modifier=Modifier.fillMaxWidth().heightIn(min = 128.dp).onFocusChanged { editing = it.isFocused }.semantics { contentDescription = messageDescription }, shape = RectangleShape, supportingText = { Text("$count / 4,000 characters · English research scope") }, isError = count > TextContract.MAX_CODEPOINTS)
            state.error?.let { Text(it,color=studio.danger,modifier=Modifier.semantics { liveRegion=LiveRegionMode.Polite }) }
            Primary(stringResource(R.string.analyze_message),state.text.isNotBlank() && count <= TextContract.MAX_CODEPOINTS,vm::analyze)
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick={
                    val clipboard=context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val value=clipboard.primaryClip?.getItemAt(0)?.text?.toString()
                    vm.paste(value)
                },modifier=Modifier.weight(1f).heightIn(min=48.dp)) { Text(stringResource(R.string.paste)) }
                TextButton(onClick=vm::clear,modifier=Modifier.heightIn(min=48.dp)) { Text(stringResource(R.string.clear)) }
            }
        }
        OutlinedButton(onClick=onImage,modifier=Modifier.fillMaxWidth().heightIn(min=56.dp),shape=RectangleShape) { Mark(kind="image"); Spacer(Modifier.width(12.dp)); Text(stringResource(if(state.stage==Stage.REVIEWING) R.string.replace_screenshot else R.string.choose_screenshot)) }
        Box { TextButton(onClick={ sampleMenu=true },modifier=Modifier.heightIn(min=48.dp)) { Text("Try a fictional example") }; DropdownMenu(expanded=sampleMenu,onDismissRequest={ sampleMenu=false }) { listOf("Class coordination","Credential request","Promotional offer","Legitimate urgency","Unsupported language").forEachIndexed { i,label -> DropdownMenuItem(text={ Text(label) },onClick={ vm.edit(samples[i],"Fictional demo");sampleMenu=false }) } } }
        Row(horizontalArrangement=Arrangement.spacedBy(12.dp),verticalAlignment=Alignment.CenterVertically) { Text(if(historyEnabled) "History is enabled. Saving a redacted summary is still manual." else "You choose what to inspect. History is off.",color=studio.muted,style=MaterialTheme.typography.bodyMedium) }
    }
}

