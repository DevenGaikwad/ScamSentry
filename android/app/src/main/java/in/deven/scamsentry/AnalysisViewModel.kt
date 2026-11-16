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
    val history = app.history.observe().catch { _state.update { s -> s.copy(error="Saved summaries are unavailable. Message analysis still works.") }; emit(emptyList()) }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    private var work: Job? = null
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
    }
    fun clear() {
        work?.cancel()
        _state.value.preview?.recycle()
        _state.value = EditorState(version=_state.value.version+1)
    }
    fun cancel() {
        work?.cancel()
        _state.update { it.copy(stage=if(it.preview!=null) Stage.REVIEWING else Stage.EDITING,version=it.version+1) }
    }
    fun review() { _state.update { it.copy(stage=if(it.preview!=null) Stage.REVIEWING else Stage.EDITING) } }
    fun selectImage(uri: Uri) {
        work?.cancel()
        _state.value.preview?.recycle()
        val version = _state.value.version+1
        _state.value = EditorState(source="Screenshot",version=version,stage=Stage.EXTRACTING)
        work = viewModelScope.launch {
            try {
                val draft = ocrMutex.withLock { OcrReader(app).read(uri) }
                ensureActive()
                if (_state.value.version==version) _state.update { it.copy(text=draft.text,stage=Stage.REVIEWING,preview=draft.preview,ocrWarning=draft.warning) }
                else draft.preview.recycle()
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
                    val obs = Rules.inspect(snapshot.text)
                    val addresses = UrlInspector.inspect(snapshot.text)
                    val standaloneUrl = addresses.size==1 && snapshot.text.trim()==addresses.first().original
                    val estimate = if(!standaloneUrl && TextContract.scriptSupported(snapshot.text)) engine?.predict(snapshot.text) else null
                    val poorOcr = snapshot.source=="Screenshot" && snapshot.text.trim().length<20
                    val noFeatures = estimate!=null && estimate.activeCount==0
                    val risk = if(standaloneUrl) { if(addresses.first().valid) Risk.CHECKING else Risk.UNABLE } else Policy.decide(snapshot.text,estimate?.probabilities,obs,addresses,engine?.confidence?:.62,engine?.margin?:.18,poorOcr || noFeatures || addresses.size>=40)
                    AnalysisResult(UUID.randomUUID().toString(),snapshot.version,snapshot.text,snapshot.source,risk,estimate,obs,addresses,buildList {
                        if(engine==null) add("The bundled message model is unavailable. Only the listed local observations are available. Reinstall the verified APK or edit the input.")
                        if(!TextContract.scriptSupported(snapshot.text)) add("English messages are the supported research scope. This text is too short or uses a different script; Romanized language detection is also limited.")
                        if(snapshot.source=="Screenshot") add(if(poorOcr) "Very little OCR text was available; interpretation is limited. Replace the image or enter a complete message." else "Text was extracted from a screenshot and reviewed. A mistaken character can still affect this result.")
                        if(noFeatures) add("No usable model features were found. A result cannot be inferred reliably from this input.")
                        if(standaloneUrl) add("Standalone address: lexical observations only. A message-classifier estimate is not a website-risk probability and is not used here.")
                        if(addresses.size>=40) add("Link analysis is limited to the first 40 addresses. Shorten this input to inspect all destinations.")
                        add("This historical research model cannot verify the sender or guarantee legitimacy.")
                        if(addresses.isNotEmpty()) add("Links were inspected as text only. No site was visited or checked against live reputation services.")
                    },engine?.id?:"unavailable",if(estimate!=null) engine?.contributions(snapshot.text,estimate)?:emptyList() else emptyList())
                }
                ensureActive()
                if(_state.value.version==snapshot.version) {
                    _state.value.preview?.recycle()
                    _state.update { it.copy(result=result,stage=Stage.RESULT,preview=null) }
                }
            } catch (e: CancellationException) { throw e }
            catch (_: Exception) { if(_state.value.version==snapshot.version) _state.update { it.copy(stage=Stage.EDITING,error="Analysis could not finish. Review the text and try again. No lower-risk result was generated.") } }
        }
    }
    fun finishOnboarding() { viewModelScope.launch { app.settings.onboard() } }
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
                    app.history.insert(HistoryEntry(result.id,result.completedAt,result.risk.wire,"${result.risk.title}; ${result.observations.size} observed patterns; ${result.urls.size} links. Original content not saved.",result.modelId,inputSource=result.source))
                    true
                }
            } }.onSuccess { saved -> if(saved && _state.value.result?.id==result.id) _state.update { it.copy(saved=true) } }.onFailure { _state.update { it.copy(error="Summary could not be saved. Your analysis is still available.") } }
        }
    }
    fun delete(id: String) { viewModelScope.launch { runCatching { historyMutex.withLock { app.history.delete(id) } }.onSuccess { if(_state.value.result?.id==id) _state.update { it.copy(saved=false) } }.onFailure { _state.update { it.copy(error="Summary could not be deleted. Try again.") } } } }
    fun clearHistory() { viewModelScope.launch { runCatching { historyMutex.withLock { app.history.clear(); _state.update { it.copy(saved=false) } } }.onFailure { _state.update { it.copy(error="Summaries could not be cleared. Try again.") } } } }
    override fun onCleared() { _state.value.preview?.recycle(); super.onCleared() }
}
