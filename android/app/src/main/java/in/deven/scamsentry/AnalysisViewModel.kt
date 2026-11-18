package `in`.deven.scamsentry

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import `in`.deven.scamsentry.core.*
import `in`.deven.scamsentry.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

enum class Stage { EDITING, EXTRACTING, REVIEWING, ANALYZING, RESULT }
data class EditorState(val text: String="", val source: String="Typed", val version: Long=0, val stage: Stage=Stage.EDITING, val result: AnalysisResult?=null, val error: String?=null, val preview: Bitmap?=null, val ocrWarning: String?=null, val saved: Boolean=false)

class AnalysisViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as ScamSentryApp
    private val _state = MutableStateFlow(EditorState())
    val state = _state.asStateFlow()
    val historyEnabled = app.settings.historyEnabled.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),false)
    val onboardingDone = app.settings.onboardingDone.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),false)
    val presentation = app.settings.presentation.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PresentationPreferences())
    private val pendingShare = Channel<Unit>(Channel.CONFLATED)
    val sharedInput = pendingShare.receiveAsFlow()
    val history = app.history.observe().catch { _state.update { s -> s.copy(error="Saved summaries are unavailable. Message analysis still works.") }; emit(emptyList()) }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    private var work: Job? = null
    private var previewOwner: OcrDraft? = null
    private fun releasePreview() { previewOwner?.close(); previewOwner = null }
    fun exportFailure() { _state.update { it.copy(error="The summary could not be shared. Your analysis is still available; try another destination app.") } }
    private var model: LinearModel? = null
    private var modelTried = false
    private val modelMutex = Mutex()
    private val ocrMutex = Mutex()
    private val historyMutex = Mutex()
    private suspend fun engine(): LinearModel? = modelMutex.withLock { withContext(Dispatchers.Default) {
        if (!modelTried) {
            modelTried = true
            model = runCatching {
                val assets = app.assets
                val json = assets.open("model/model.json").bufferedReader().use { it.readText() }
                val manifest = assets.open("model/manifest.json").bufferedReader().use { it.readText() }
                LinearModel(json,manifest)
            }.getOrNull()
        }
        model
    } }
    fun edit(text: String, source: String? = null) {
        if(text.length>16_000) { _state.update { it.copy(error="Text is too large. Choose a shorter excerpt; nothing was truncated.") }; return }
        work?.cancel()
        _state.update { it.copy(text=text,source=source?:it.source,version=it.version+1,result=null,stage=if(it.preview!=null) Stage.REVIEWING else Stage.EDITING,error=null,saved=false) }
    }
    fun paste(text: String?) {
        if(text.isNullOrBlank()) { _state.update { it.copy(error="The clipboard contains no plain text. Type a message or choose a screenshot.") }; return }
        if(text.length>16_000) { _state.update { it.copy(error="Clipboard text is too large. Copy a shorter excerpt; nothing was truncated.") }; return }
        clear()
        edit(text,"Pasted")
    }
    fun receive(intent: Intent?) {
        if (intent?.action != Intent.ACTION_SEND || intent.type != "text/plain") return
        // Avoid deserializing arbitrary Parcelables. Shared content is reviewed, never opened.
        val text = runCatching { intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString() }.getOrNull() ?: return
        if (text.length > 16_000) { _state.update { it.copy(error="Shared text is too large. Paste a shorter excerpt.") }; return }
        clear()
        edit(text,"Shared text")
        pendingShare.trySend(Unit)
    }
    fun clear() {
        work?.cancel()
        releasePreview()
        _state.value = EditorState(version=_state.value.version+1)
    }
    fun cancel() {
        work?.cancel()
        _state.update { it.copy(stage=if(it.preview!=null) Stage.REVIEWING else Stage.EDITING,version=it.version+1) }
    }
    fun review() { _state.update { it.copy(stage=if(it.preview!=null) Stage.REVIEWING else Stage.EDITING) } }
    fun selectImage(uri: Uri) {
        work?.cancel()
        releasePreview()
        val version = _state.value.version+1
        _state.value = EditorState(source="Screenshot",version=version,stage=Stage.EXTRACTING)
        work = viewModelScope.launch {
            try {
                val draft = ocrMutex.withLock { OcrReader(app).read(uri) }
                var transferred = false
                try {
                    ensureActive()
                    if (_state.value.version==version) {
                        previewOwner = draft
                        _state.update { it.copy(text=draft.text,stage=Stage.REVIEWING,preview=draft.preview,ocrWarning=draft.warning) }
                        transferred = true
                    }
                } finally { if (!transferred) draft.close() }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { if (_state.value.version==version) _state.update { it.copy(stage=Stage.EDITING,error=(e as? InputFailure)?.publicMessage?:"Image text could not be read. Replace it or type the message.") } }
        }
    }
    fun analyze() {
        val snapshot = _state.value
        if (snapshot.stage in listOf(Stage.ANALYZING,Stage.EXTRACTING)) return
        if (snapshot.text.isBlank()) { _state.update { it.copy(error="Enter a message or select a screenshot first.") }; return }
        if (snapshot.text.codePointCount(0,snapshot.text.length)>TextContract.MAX_CODEPOINTS) { _state.update { it.copy(error="Use 4,000 characters or fewer. Nothing was silently truncated.") }; return }
        work?.cancel()
        _state.update { it.copy(stage=Stage.ANALYZING,error=null) }
        work = viewModelScope.launch {
            try {
                val engine = engine()
                val result = withContext(Dispatchers.Default) {
                    val domain = AnalysisEngine.analyze(snapshot.text,engine,if (snapshot.source=="Screenshot") "ocr" else "text")
                    AnalysisResult(UUID.randomUUID().toString(),snapshot.version,snapshot.text,snapshot.source,domain.risk,domain.estimate,domain.observations,domain.urls,domain.limitations,domain.modelId,domain.contributions)

                }
                ensureActive()
                if(_state.value.version==snapshot.version) {
                    releasePreview()
                    _state.update { it.copy(result=result,stage=Stage.RESULT,preview=null) }
                }
            } catch (e: CancellationException) { throw e }
            catch (_: Exception) { if(_state.value.version==snapshot.version) _state.update { it.copy(stage=Stage.EDITING,error="Analysis could not finish. Review the text and try again. No lower-risk result was generated.") } }
        }
    }
    fun finishOnboarding() { viewModelScope.launch { app.settings.onboard() } }
    fun setTheme(choice: AppTheme) = changePresentation { app.settings.theme(choice) }
    fun setMotion(enabled: Boolean) = changePresentation { app.settings.motion(enabled) }

    private fun changePresentation(update: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                update()
            } catch (_: java.io.IOException) {
                _state.update { it.copy(error="The display preference could not be saved. Try again.") }
            }
        }
    }
    fun setHistory(enabled: Boolean) { viewModelScope.launch {
        runCatching { historyMutex.withLock {
            if(!enabled) app.history.clear()
            app.settings.history(enabled)
            if(!enabled) _state.update { it.copy(saved=false) }
        } }.onFailure { _state.update { it.copy(error="History setting could not be changed. Try again.") } }
    } }
    fun save() {
        val result = _state.value.result ?: return
        if(!historyEnabled.value) return
        viewModelScope.launch {
            runCatching { historyMutex.withLock {
                if(!app.settings.historyEnabled.first()) false else {
                    app.history.insert(HistoryEntry(result.id,result.completedAt,result.risk.wire,"${result.risk.title}; ${result.observations.size} observed patterns; ${result.urls.size} links. Original content not saved.",result.modelId,policyVersion=result.policyVersion,inputSource=result.source))
                    true
                }
            } }.onSuccess { saved -> if(saved && _state.value.result?.id==result.id) _state.update { it.copy(saved=true) } }.onFailure { _state.update { it.copy(error="Summary could not be saved. Your analysis is still available.") } }
        }
    }
    fun delete(id: String) { viewModelScope.launch { runCatching { historyMutex.withLock { app.history.delete(id) } }.onSuccess { if(_state.value.result?.id==id) _state.update { it.copy(saved=false) } }.onFailure { _state.update { it.copy(error="Summary could not be deleted. Try again.") } } } }
    fun clearHistory() { viewModelScope.launch { runCatching { historyMutex.withLock { app.history.clear(); _state.update { it.copy(saved=false) } } }.onFailure { _state.update { it.copy(error="Summaries could not be cleared. Try again.") } } } }
    override fun onCleared() { releasePreview() }
}
