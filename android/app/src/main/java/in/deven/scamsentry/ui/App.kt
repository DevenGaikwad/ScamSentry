package `in`.deven.scamsentry.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import `in`.deven.scamsentry.AnalysisViewModel
import `in`.deven.scamsentry.R
import `in`.deven.scamsentry.Stage
import `in`.deven.scamsentry.data.PresentationPreferences
import `in`.deven.scamsentry.data.ReportExport

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun ScamSentryRoot(vm: AnalysisViewModel, presentation: PresentationPreferences) {
    val state by vm.state.collectAsStateWithLifecycle()
    val onboarding by vm.onboardingDone.collectAsStateWithLifecycle()
    val enabled by vm.historyEnabled.collectAsStateWithLifecycle()
    val history by vm.history.collectAsStateWithLifecycle()
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val destination = entry?.destination?.route ?: "analyze"
    var exportPreview by remember(state.result?.id, destination) { mutableStateOf<String?>(null) }
    var showSave by remember(state.result?.id, destination) { mutableStateOf(false) }
    var deleteAll by remember(destination) { mutableStateOf(false) }
    val context = LocalContext.current
    val settingsDescription = stringResource(R.string.settings_description)
    val widthClass = calculateWindowSizeClass(context as Activity).widthSizeClass
    val rail = widthClass != WindowWidthSizeClass.Compact
    val expanded = widthClass == WindowWidthSizeClass.Expanded
    val snackbar = remember { SnackbarHostState() }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            nav.navigate("analyze") { launchSingleTop = true; popUpTo("analyze") }
            vm.selectImage(uri)
        }
    }
    fun selectScreenshot() { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
    fun navigate(route: String) {
        nav.navigate(route) {
            popUpTo("analyze") { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    LaunchedEffect(vm, nav) {
        vm.sharedInput.collect {
            nav.navigate("analyze") { launchSingleTop = true; popUpTo("analyze") }
        }
    }
    LaunchedEffect(state.error) { state.error?.let { snackbar.showSnackbar(it) } }
    BackHandler(destination == "analyze" && state.stage != Stage.EDITING) {
        when (state.stage) {
            Stage.RESULT -> vm.review()
            Stage.REVIEWING -> vm.clear()
            Stage.EXTRACTING, Stage.ANALYZING -> vm.cancel()
            Stage.EDITING -> Unit
        }
    }

    val destinations = listOf(
        Triple("analyze", stringResource(R.string.nav_analyze), "brand"),
        Triple("history", stringResource(R.string.nav_history), "history"),
        Triple("learn", stringResource(R.string.nav_learn), "learn"),
    )
    Scaffold(
        containerColor = studio.paper,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Mark()
                            Text(stringResource(R.string.app_name), fontFamily = Studio.Heading, fontWeight = FontWeight.Black, fontSize = 21.sp)
                        }
                    },
                    actions = {
                        IconButton(onClick = { navigate("settings") }, modifier = Modifier.semantics { contentDescription = settingsDescription }) {
                            Mark(kind = "settings", color = studio.muted)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = studio.paper),
                )
                HorizontalDivider(color = studio.rule)
            }
        },
        bottomBar = {
            if (!rail) Column {
                HorizontalDivider(color = studio.rule)
                NavigationBar(containerColor = studio.paper, tonalElevation = 0.dp) {
                    destinations.forEach { (route, label, icon) ->
                        NavigationBarItem(
                            selected = destination == route, onClick = { navigate(route) },
                            icon = { Mark(kind = icon, color = if (destination == route) studio.ink else studio.muted) },
                            label = { Text(label) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = studio.raised),
                        )
                    }
                }
            }
        },
    ) { padding ->
        Row(
            Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).background(studio.paper),
            horizontalArrangement = Arrangement.Center,
        ) {
            if (rail) {
                NavigationRail(containerColor = studio.paper, modifier = Modifier.fillMaxHeight()) {
                    destinations.forEach { (route, label, icon) ->
                        NavigationRailItem(
                            selected = destination == route, onClick = { navigate(route) },
                            icon = { Mark(kind = icon, color = if (destination == route) studio.ink else studio.muted) },
                            label = { Text(label) },
                        )
                    }
                }
                VerticalDivider(color = studio.rule)
            }
            NavHost(
                nav, startDestination = "analyze", modifier = Modifier.weight(1f),
                enterTransition = { EnterTransition.None }, exitTransition = { ExitTransition.None },
            ) {
                composable("analyze") {
                    when (state.stage) {
                        Stage.EXTRACTING, Stage.ANALYZING -> Page {
                            Tag("ON-DEVICE PROCESSING")
                            Heading(if (state.stage == Stage.EXTRACTING) "Reading image text" else "Analyzing your message")
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            OutlinedButton(onClick = vm::cancel, shape = RectangleShape) { Text(stringResource(R.string.cancel)) }
                        }
                        Stage.RESULT -> state.result?.let { result ->
                            val resultPage: @Composable () -> Unit = {
                                ResultPage(
                                    result, enabled, state.saved, onEdit = vm::review,
                                    onSave = { showSave = true }, onExport = { exportPreview = ReportExport.preview(result) },
                                    onLearn = { navigate("learn") },
                                )
                            }
                            if (expanded) Row(Modifier.fillMaxSize()) {
                                Box(Modifier.weight(1f)) { EditorPage(state, vm, ::selectScreenshot, enabled) }
                                VerticalDivider(color = studio.rule)
                                Box(Modifier.weight(1f)) { resultPage() }
                            } else resultPage()
                        }
                        else -> EditorPage(
                            state, vm, ::selectScreenshot, enabled,
                            animate = presentation.animate && onboarding && destination == "analyze",
                        )
                    }
                }
                composable("history") { HistoryPage(history, enabled, vm, { navigate("settings") }, { deleteAll = true }) }
                composable("learn") {
                    LearnPage { example ->
                        vm.clear()
                        vm.edit(example, "Fictional demo")
                        navigate("analyze")
                    }
                }
                composable("settings") {
                    SettingsPage(enabled, presentation, vm, onAbout = { navigate("about") }, onPrivacy = { navigate("privacy") })
                }
                composable("about") { AboutPage() }
                composable("privacy") { PrivacyPage() }
            }
        }
    }

    if (!onboarding) AlertDialog(
        onDismissRequest = vm::finishOnboarding,
        icon = { Mark(Modifier.size(46.dp)) },
        title = { Text("A second look, on your terms") },
        text = { Text("Inspect messages and review screenshot text on this device. Results show indicators in English short messages. Saving redacted history is optional.") },
        confirmButton = { Button(onClick = vm::finishOnboarding, shape = RectangleShape) { Text("Start inspecting") } },
        dismissButton = { TextButton(onClick = vm::finishOnboarding) { Text("Skip introduction") } },
    )
    exportPreview?.let { preview ->
        AlertDialog(
            onDismissRequest = { exportPreview = null },
            title = { Text("Review before sharing") },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    SelectionContainer { Text(preview, style = MaterialTheme.typography.bodyMedium) }
                }
            },
            confirmButton = {
                Button(onClick = {
                    runCatching { ReportExport.share(context, preview) }
                        .onSuccess { exportPreview = null }.onFailure { vm.exportFailure() }
                }, shape = RectangleShape) { Text("Share this summary") }
            },
            dismissButton = { TextButton(onClick = { exportPreview = null }) { Text(stringResource(R.string.cancel)) } },
        )
    }
    if (showSave) AlertDialog(
        onDismissRequest = { showSave = false },
        title = { Text("Save a local summary?") },
        text = { Text("Saves the result category, pattern count, link count, time and model version. Original text, destinations and screenshots are omitted.") },
        confirmButton = { Button(onClick = { vm.save(); showSave = false }, shape = RectangleShape) { Text("Save summary") } },
        dismissButton = { TextButton(onClick = { showSave = false }) { Text(stringResource(R.string.cancel)) } },
    )
    if (deleteAll) AlertDialog(
        onDismissRequest = { deleteAll = false },
        title = { Text("Clear saved summaries?") },
        text = { Text("Removes ScamSentry's local summaries. Files you shared to other apps are separate.") },
        confirmButton = { Button(onClick = { vm.clearHistory(); deleteAll = false }, shape = RectangleShape) { Text("Clear all") } },
        dismissButton = { TextButton(onClick = { deleteAll = false }) { Text("Keep summaries") } },
    )
}
