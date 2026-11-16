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

private val samples=listOf(
    "Let's meet in the library after class. Please bring the project notes.",
    "Fictional account team: send your OTP immediately to prevent account suspension. Visit https://bank.example@review.test/verify",
    "Fictional stationery shop: weekend discount on notebooks. Reply STOP to unsubscribe.",
    "Urgent class reminder: bring your laboratory notebook before tomorrow's session.",
    "पैसे पाठवा. हा फक्त काल्पनिक नमुना आहे."
)

@Composable fun Mark(kind: String="shield", color: Color=Studio.Blue, modifier: Modifier=Modifier.size(24.dp)) {
    Canvas(modifier.clearAndSetSemantics { }) {
        val w=size.width; val h=size.height; val stroke=Stroke(width=w*.075f)
        when(kind) {
            "history" -> { drawCircle(color,radius=w*.34f,style=stroke); drawLine(color,Offset(w*.5f,h*.28f),Offset(w*.5f,h*.52f),strokeWidth=w*.075f); drawLine(color,Offset(w*.5f,h*.52f),Offset(w*.67f,h*.6f),strokeWidth=w*.075f) }
            "learn" -> { drawRoundRect(color,topLeft=Offset(w*.12f,h*.18f),size=androidx.compose.ui.geometry.Size(w*.76f,h*.64f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.07f),style=stroke); drawLine(color,Offset(w*.5f,h*.2f),Offset(w*.5f,h*.8f),strokeWidth=w*.06f) }
            "settings" -> { drawCircle(color,radius=w*.32f,style=stroke); drawCircle(color,radius=w*.1f,style=stroke) }
            "image" -> { drawRoundRect(color,topLeft=Offset(w*.1f,h*.15f),size=androidx.compose.ui.geometry.Size(w*.8f,h*.7f),style=stroke); val p=Path().apply { moveTo(w*.16f,h*.73f); lineTo(w*.4f,h*.45f); lineTo(w*.59f,h*.63f); lineTo(w*.73f,h*.47f); lineTo(w*.87f,h*.71f) }; drawPath(p,color,style=stroke); drawCircle(color,radius=w*.055f,center=Offset(w*.68f,h*.33f)) }
            else -> { val p=Path().apply { moveTo(w*.5f,h*.08f); lineTo(w*.84f,h*.23f); lineTo(w*.8f,h*.58f); quadraticTo(w*.76f,h*.8f,w*.5f,h*.92f); quadraticTo(w*.24f,h*.8f,w*.2f,h*.58f); lineTo(w*.16f,h*.23f); close() }; drawPath(p,color,style=stroke); drawCircle(color,radius=w*.13f,center=Offset(w*.46f,h*.44f),style=stroke); drawLine(color,Offset(w*.56f,h*.55f),Offset(w*.68f,h*.69f),strokeWidth=w*.07f) }
        }
    }
}

@Composable private fun Panel(modifier: Modifier=Modifier, color: Color=Studio.Surface, content: @Composable ColumnScope.()->Unit) {
    Column(modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(color).border(1.dp,Studio.Border,RoundedCornerShape(20.dp)).padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp),content=content)
}
@Composable private fun Tag(text: String, color: Color=Studio.Blue) {
    Text(text,style=MaterialTheme.typography.labelMedium,color=color,modifier=Modifier.clip(RoundedCornerShape(8.dp)).background(color.copy(alpha=.1f)).padding(horizontal=10.dp,vertical=6.dp))
}
@Composable private fun Primary(text: String, enabled: Boolean=true, action: ()->Unit) {
    Button(onClick=action,enabled=enabled,shape=RoundedCornerShape(14.dp),modifier=Modifier.fillMaxWidth().heightIn(min=54.dp),contentPadding=PaddingValues(16.dp)) { Text(text) }
}
@Composable private fun Page(content: @Composable ColumnScope.()->Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(horizontal=24.dp,vertical=20.dp).widthIn(max=760.dp),verticalArrangement=Arrangement.spacedBy(20.dp),content=content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun ScamSentryRoot(vm: AnalysisViewModel) {
    val state by vm.state.collectAsStateWithLifecycle()
    val onboarding by vm.onboardingDone.collectAsStateWithLifecycle()
    val enabled by vm.historyEnabled.collectAsStateWithLifecycle()
    val history by vm.history.collectAsStateWithLifecycle()
    val nav=rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val destination=entry?.destination?.route?:"analyze"
    var exportPreview by remember { mutableStateOf<String?>(null) }
    var showSave by remember { mutableStateOf(false) }
    var deleteAll by remember { mutableStateOf(false) }
    val context=LocalContext.current
    val snackbar=remember { SnackbarHostState() }
    val picker=rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> if(uri!=null) { nav.navigate("analyze") { launchSingleTop=true }; vm.selectImage(uri) } }
    fun selectScreenshot() { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
    fun navigate(route: String) { nav.navigate(route) { popUpTo("analyze") { saveState=true }; launchSingleTop=true; restoreState=true } }
    LaunchedEffect(state.source,state.version) { if(state.source=="Shared text") nav.navigate("analyze") { launchSingleTop=true } }
    LaunchedEffect(state.error) { state.error?.let { snackbar.showSnackbar(it) } }
    BackHandler(state.stage in listOf(Stage.RESULT,Stage.REVIEWING,Stage.EXTRACTING,Stage.ANALYZING)) { if(state.stage==Stage.RESULT) vm.review() else if(state.stage==Stage.REVIEWING) vm.clear() else vm.cancel() }
    Scaffold(
        snackbarHost={ SnackbarHost(snackbar) },
        topBar={ TopAppBar(title={ Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)) { Mark(); Text("ScamSentry",fontFamily=Studio.Heading,fontWeight=FontWeight.Bold,fontSize=21.sp) } },actions={ IconButton(onClick={ navigate("settings") },modifier=Modifier.semantics { contentDescription="Settings and model information" }) { Mark("settings",Studio.Muted) } },colors=TopAppBarDefaults.topAppBarColors(containerColor=Studio.Background)) },
        bottomBar={ NavigationBar(containerColor=Studio.Surface,tonalElevation=0.dp) { listOf(Triple("analyze","Analyze","shield"),Triple("history","History","history"),Triple("learn","Learn","learn")).forEach { (route,label,icon) -> NavigationBarItem(selected=destination==route,onClick={ navigate(route) },icon={ Mark(icon,if(destination==route) Studio.Blue else Studio.Muted) },label={ Text(label) },colors=NavigationBarItemDefaults.colors(indicatorColor=Studio.Blue.copy(alpha=.13f))) } } }
    ) { padding ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(padding).background(Studio.Background)) {
            val wide=maxWidth>720.dp
            Row(Modifier.fillMaxSize(),horizontalArrangement=Arrangement.Center) {
                if(wide) Column(Modifier.width(240.dp).padding(24.dp),verticalArrangement=Arrangement.spacedBy(20.dp)) { Tag("SECURITY STUDIO"); Text("Pause.\nInspect.\nVerify.",style=MaterialTheme.typography.headlineLarge); Text("A little clarity before your next click.",color=Studio.Muted); Mark(modifier=Modifier.size(96.dp)); Text("Experimental English text analysis. Your judgment stays central.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium) }
                Box(Modifier.weight(1f).widthIn(max=760.dp)) {
                    NavHost(nav,startDestination="analyze") {
                        composable("analyze") {
                            when(state.stage) {
                                Stage.EXTRACTING,Stage.ANALYZING -> Page { Spacer(Modifier.height(40.dp)); Mark(modifier=Modifier.size(70.dp)); Text(if(state.stage==Stage.EXTRACTING) "Reading image text" else "Analyzing your message",style=MaterialTheme.typography.headlineMedium); LinearProgressIndicator(modifier=Modifier.fillMaxWidth()); Text("Processing locally on this device.",color=Studio.Muted); OutlinedButton(onClick=vm::cancel) { Text("Cancel") } }
                                Stage.RESULT -> state.result?.let { r -> ResultPage(r,enabled,state.saved,onEdit=vm::review,onSave={ showSave=true },onExport={ exportPreview=ReportExport.preview(r) },onLearn={ navigate("learn") }) }
                                else -> EditorPage(state,vm,onImage=::selectScreenshot)
                            }
                        }
                        composable("history") { HistoryPage(history,enabled,vm,onSettings={ navigate("settings") },onClear={ deleteAll=true }) }
                        composable("learn") { LearnPage() }
                        composable("settings") { SettingsPage(enabled,vm,onAbout={ navigate("about") }) }
                        composable("about") { AboutPage() }
                    }
                }
            }
        }
    }
    if(!onboarding) AlertDialog(onDismissRequest=vm::finishOnboarding,icon={ Mark(modifier=Modifier.size(56.dp)) },title={ Text("A second look, on your terms",fontFamily=Studio.Heading) },text={ Column(verticalArrangement=Arrangement.spacedBy(12.dp)) { Text("Choose a message or screenshot. ScamSentry analyzes supported content locally."); Text("Results show indicators, not verified authenticity. English is the research scope; screenshot text needs your review."); Text("History starts off. No account, inbox access or inference server is needed.",color=Studio.Muted) } },confirmButton={ Button(onClick=vm::finishOnboarding) { Text("Start inspecting") } },dismissButton={ TextButton(onClick=vm::finishOnboarding) { Text("Skip introduction") } })
    exportPreview?.let { preview -> AlertDialog(onDismissRequest={ exportPreview=null },title={ Text("Review before sharing") },text={ Column(Modifier.verticalScroll(rememberScrollState())) { Text(preview,style=MaterialTheme.typography.bodyMedium) } },confirmButton={ Button(onClick={ runCatching { ReportExport.share(context,preview) }; exportPreview=null }) { Text("Share this summary") } },dismissButton={ TextButton(onClick={ exportPreview=null }) { Text("Cancel") } }) }
    if(showSave) AlertDialog(onDismissRequest={ showSave=false },title={ Text("Save a local summary?") },text={ Text("Saves the result category, pattern count, link count, time and model version. Original text, destinations and screenshots are omitted.") },confirmButton={ Button(onClick={ vm.save(); showSave=false }) { Text("Save summary") } },dismissButton={ TextButton(onClick={ showSave=false }) { Text("Cancel") } })
    if(deleteAll) AlertDialog(onDismissRequest={ deleteAll=false },title={ Text("Clear saved summaries?") },text={ Text("Removes ScamSentry's local summaries. Files you shared to other apps are separate.") },confirmButton={ Button(onClick={ vm.clearHistory(); deleteAll=false }) { Text("Clear all") } },dismissButton={ TextButton(onClick={ deleteAll=false }) { Text("Keep summaries") } })
}

@Composable private fun EditorPage(state: EditorState,vm: AnalysisViewModel,onImage: ()->Unit) {
    val context=LocalContext.current
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
            OutlinedTextField(value=state.text,onValueChange={ vm.edit(it) },placeholder={ Text("Paste a message or a link here…") },modifier=Modifier.fillMaxWidth().heightIn(min=170.dp).semantics { contentDescription="Message to analyze" },shape=RoundedCornerShape(12.dp),supportingText={ Text("${state.text.codePointCount(0,state.text.length)} / 4,000 characters • English research scope") },isError=state.text.codePointCount(0,state.text.length)>4000)
            state.error?.let { Text(it,color=Studio.Red,modifier=Modifier.semantics { liveRegion=LiveRegionMode.Polite }) }
            Primary("Analyze message",state.text.isNotBlank() && state.text.codePointCount(0,state.text.length)<=4000,vm::analyze)
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick={
                    val clipboard=context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val value=clipboard.primaryClip?.getItemAt(0)?.text?.toString()
                    vm.paste(value)
                },modifier=Modifier.weight(1f).heightIn(min=48.dp)) { Text("Paste") }
                TextButton(onClick=vm::clear,modifier=Modifier.heightIn(min=48.dp)) { Text("Clear") }
            }
        }
        OutlinedButton(onClick=onImage,modifier=Modifier.fillMaxWidth().heightIn(min=56.dp),shape=RoundedCornerShape(14.dp)) { Mark("image"); Spacer(Modifier.width(12.dp)); Text(if(state.stage==Stage.REVIEWING) "Replace screenshot" else "Choose a screenshot") }
        Box { TextButton(onClick={ sampleMenu=true },modifier=Modifier.heightIn(min=48.dp)) { Text("Try a fictional example") }; DropdownMenu(expanded=sampleMenu,onDismissRequest={ sampleMenu=false }) { listOf("Class coordination","Credential request","Promotional offer","Legitimate urgency","Unsupported language").forEachIndexed { i,label -> DropdownMenuItem(text={ Text(label) },onClick={ vm.edit(samples[i],"Fictional demo");sampleMenu=false }) } } }
        Row(horizontalArrangement=Arrangement.spacedBy(12.dp),verticalAlignment=Alignment.CenterVertically) { Mark(color=Studio.Green); Text("You choose what to inspect. History is off until you enable it.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium) }
    }
}

@Composable private fun ResultPage(result: AnalysisResult,historyEnabled: Boolean,saved: Boolean,onEdit: ()->Unit,onSave: ()->Unit,onExport: ()->Unit,onLearn: ()->Unit) {
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
                Text("${result.modelId} · normalizer nfkc_ascii_ws_v1 · policy ordinal_v1",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium)
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

@Composable private fun HistoryPage(entries: List<HistoryEntry>,enabled: Boolean,vm: AnalysisViewModel,onSettings: ()->Unit,onClear: ()->Unit) {
    val locale=LocalConfiguration.current.locales[0]
    val dateFormat=remember(locale) { SimpleDateFormat("dd MMM yyyy, HH:mm",locale) }
    Page {
        Tag("YOUR DEVICE, YOUR CHOICE")
        Text("Saved summaries",style=MaterialTheme.typography.headlineLarge)
        Text("Only summaries you deliberately save appear here. Original messages and screenshots are never saved to history.",color=Studio.Muted)
        if(!enabled) Panel { Mark("history"); Text("History is off",style=MaterialTheme.typography.titleLarge); Text("Analyze without retaining a record. Enable optional summary history in Settings.",color=Studio.Muted); OutlinedButton(onClick=onSettings) { Text("History settings") } }
        else if(entries.isEmpty()) Panel { Text("Nothing saved yet",style=MaterialTheme.typography.titleLarge); Text("After an analysis, choose Save a local summary.",color=Studio.Muted) }
        entries.forEach { e -> Panel { Text(Risk.entries.firstOrNull { it.wire==e.category }?.title?:"Saved result",style=MaterialTheme.typography.titleMedium); Text(e.summary,color=Studio.Muted); Text(dateFormat.format(Date(e.createdAt)),style=MaterialTheme.typography.bodyMedium,color=Studio.Muted); Text("${e.inputSource} · model ${e.modelId}",style=MaterialTheme.typography.bodyMedium); TextButton(onClick={ vm.delete(e.id) }) { Text("Delete summary",color=Studio.Red) } } }
        if(entries.isNotEmpty()) TextButton(onClick=onClear) { Text("Clear all summaries",color=Studio.Red) }
    }
}

@Composable private fun LearnPage() {
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

@Composable private fun SettingsPage(enabled: Boolean,vm: AnalysisViewModel,onAbout: ()->Unit) {
    var confirmOff by remember { mutableStateOf(false) }
    Page {
        Tag("CONTROL CENTER")
        Text("Keep it personal.\nKeep it local.",style=MaterialTheme.typography.headlineLarge)
        Panel { Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) { Column(Modifier.weight(1f)) { Text("Optional summary history",style=MaterialTheme.typography.titleMedium); Text(if(enabled) "Enabled. Saving is still manual." else "Off by default.",color=Studio.Muted) }; Switch(checked=enabled,onCheckedChange={ if(!it) confirmOff=true else vm.setHistory(true) },modifier=Modifier.semantics { contentDescription="Optional summary history" }) }; Text("Turning this off also clears saved summaries. Raw messages, links and images are never part of history.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium) }
        Panel { Text("Privacy by design",style=MaterialTheme.typography.titleLarge); Text("No account, project analytics or remote explanation service. The app requests no inbox, contact, camera or broad storage permission. Images are chosen through the system picker.",color=Studio.Muted); Text("Draft text and screenshot previews live in memory. Rotation retains the active draft; process death discards it. Exporting creates a redacted summary only, after your review.",color=Studio.Muted); Text("The bundled ML Kit OCR dependency is configured without app network permission. SDK operational diagnostics and fresh offline behavior still need verification on your device.",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium) }
        OutlinedButton(onClick=onAbout,modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) { Text("Model, scope and attribution") }
        Text("ScamSentry 0.1.0 · Research preview",color=Studio.Muted,style=MaterialTheme.typography.bodyMedium)
    }
    if(confirmOff) AlertDialog(onDismissRequest={ confirmOff=false },title={ Text("Turn off history and clear summaries?") },text={ Text("Previously shared files remain in their destination apps.") },confirmButton={ Button(onClick={ vm.setHistory(false);confirmOff=false }) { Text("Turn off and clear") } },dismissButton={ TextButton(onClick={ confirmOff=false }) { Text("Keep history") } })
}

@Composable private fun AboutPage() {
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
