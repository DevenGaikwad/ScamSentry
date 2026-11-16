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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
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
    val settingsDescription=stringResource(R.string.settings_description)
    val widthClass=calculateWindowSizeClass(context as Activity).widthSizeClass
    val rail=widthClass!=WindowWidthSizeClass.Compact
    val expanded=widthClass==WindowWidthSizeClass.Expanded
    val snackbar=remember { SnackbarHostState() }
    val picker=rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> if(uri!=null) { nav.navigate("analyze") { launchSingleTop=true }; vm.selectImage(uri) } }
    fun selectScreenshot() { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
    fun navigate(route: String) { nav.navigate(route) { popUpTo("analyze") { saveState=true }; launchSingleTop=true; restoreState=true } }
    LaunchedEffect(state.source,state.version) { if(state.source=="Shared text") nav.navigate("analyze") { launchSingleTop=true } }
    LaunchedEffect(state.error) { state.error?.let { snackbar.showSnackbar(it) } }
    BackHandler(state.stage in listOf(Stage.RESULT,Stage.REVIEWING,Stage.EXTRACTING,Stage.ANALYZING)) { if(state.stage==Stage.RESULT) vm.review() else if(state.stage==Stage.REVIEWING) vm.clear() else vm.cancel() }
    Scaffold(
        snackbarHost={ SnackbarHost(snackbar) },
        topBar={ TopAppBar(title={ Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)) { Mark(); Text(stringResource(R.string.app_name),fontFamily=Studio.Heading,fontWeight=FontWeight.Bold,fontSize=21.sp) } },actions={ IconButton(onClick={ navigate("settings") },modifier=Modifier.semantics { contentDescription=settingsDescription }) { Mark(kind="settings",color=Studio.Muted) } },colors=TopAppBarDefaults.topAppBarColors(containerColor=Studio.Background)) },
        bottomBar={ if(!rail) NavigationBar(containerColor=Studio.Surface,tonalElevation=0.dp) { listOf(Triple("analyze",stringResource(R.string.nav_analyze),"shield"),Triple("history",stringResource(R.string.nav_history),"history"),Triple("learn",stringResource(R.string.nav_learn),"learn")).forEach { (route,label,icon) -> NavigationBarItem(selected=destination==route,onClick={ navigate(route) },icon={ Mark(kind=icon,color=if(destination==route) Studio.Blue else Studio.Muted) },label={ Text(label) },colors=NavigationBarItemDefaults.colors(indicatorColor=Studio.Blue.copy(alpha=.13f))) } } }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).background(Studio.Background)) {
            val wide=expanded
            Row(Modifier.fillMaxSize(),horizontalArrangement=Arrangement.Center) {
                if(rail) NavigationRail(containerColor=Studio.Surface,modifier=Modifier.fillMaxHeight()) {
                    Spacer(Modifier.height(18.dp))
                    listOf(Triple("analyze",stringResource(R.string.nav_analyze),"shield"),Triple("history",stringResource(R.string.nav_history),"history"),Triple("learn",stringResource(R.string.nav_learn),"learn")).forEach { (route,label,icon) ->
                        NavigationRailItem(selected=destination==route,onClick={ navigate(route) },icon={ Mark(kind=icon,color=if(destination==route) Studio.Blue else Studio.Muted) },label={ Text(label) })
                    }
                }
                Box(Modifier.weight(1f).widthIn(max=1280.dp)) {
                    NavHost(nav,startDestination="analyze") {
                        composable("analyze") {
                            when(state.stage) {
                                Stage.EXTRACTING,Stage.ANALYZING -> Page { Spacer(Modifier.height(40.dp)); Mark(modifier=Modifier.size(70.dp)); Text(if(state.stage==Stage.EXTRACTING) "Reading image text" else "Analyzing your message",style=MaterialTheme.typography.headlineMedium); LinearProgressIndicator(modifier=Modifier.fillMaxWidth()); Text("Processing locally on this device.",color=Studio.Muted); OutlinedButton(onClick=vm::cancel) { Text(stringResource(R.string.cancel)) } }
                                Stage.RESULT -> state.result?.let { r ->
                                    if(wide) Row(Modifier.fillMaxSize()) {
                                        Box(Modifier.weight(1f)) { EditorPage(state,vm,onImage=::selectScreenshot,historyEnabled=enabled) }
                                        Box(Modifier.weight(1f)) { ResultPage(r,enabled,state.saved,onEdit=vm::review,onSave={ showSave=true },onExport={ exportPreview=ReportExport.preview(r) },onLearn={ navigate("learn") }) }
                                    } else ResultPage(r,enabled,state.saved,onEdit=vm::review,onSave={ showSave=true },onExport={ exportPreview=ReportExport.preview(r) },onLearn={ navigate("learn") }) }
                                else -> EditorPage(state,vm,onImage=::selectScreenshot,historyEnabled=enabled)
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
    exportPreview?.let { preview -> AlertDialog(onDismissRequest={ exportPreview=null },title={ Text("Review before sharing") },text={ Column(Modifier.verticalScroll(rememberScrollState())) { Text(preview,style=MaterialTheme.typography.bodyMedium) } },confirmButton={ Button(onClick={ runCatching { ReportExport.share(context,preview) }.onSuccess { exportPreview=null }.onFailure { vm.exportFailure() } }) { Text("Share this summary") } },dismissButton={ TextButton(onClick={ exportPreview=null }) { Text(stringResource(R.string.cancel)) } }) }
    if(showSave) AlertDialog(onDismissRequest={ showSave=false },title={ Text("Save a local summary?") },text={ Text("Saves the result category, pattern count, link count, time and model version. Original text, destinations and screenshots are omitted.") },confirmButton={ Button(onClick={ vm.save(); showSave=false }) { Text("Save summary") } },dismissButton={ TextButton(onClick={ showSave=false }) { Text(stringResource(R.string.cancel)) } })
    if(deleteAll) AlertDialog(onDismissRequest={ deleteAll=false },title={ Text("Clear saved summaries?") },text={ Text("Removes ScamSentry's local summaries. Files you shared to other apps are separate.") },confirmButton={ Button(onClick={ vm.clearHistory(); deleteAll=false }) { Text("Clear all") } },dismissButton={ TextButton(onClick={ deleteAll=false }) { Text("Keep summaries") } })
}

