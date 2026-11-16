package `in`.deven.scamsentry.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import `in`.deven.scamsentry.AnalysisViewModel
import `in`.deven.scamsentry.core.Risk
import `in`.deven.scamsentry.data.HistoryEntry
import java.text.SimpleDateFormat
import java.util.Date

@Composable internal fun HistoryPage(entries: List<HistoryEntry>,enabled: Boolean,vm: AnalysisViewModel,onSettings: ()->Unit,onClear: ()->Unit) {
    val locale=LocalConfiguration.current.locales[0]
    val dateFormat=remember(locale) { SimpleDateFormat("dd MMM yyyy, HH:mm",locale) }
    LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(24.dp),verticalArrangement=Arrangement.spacedBy(20.dp)) {
        item { Tag("YOUR DEVICE, YOUR CHOICE") }
        item { Text("Saved summaries",style=MaterialTheme.typography.headlineLarge) }
        item { Text("Only summaries you deliberately save appear here. Original messages and screenshots are never saved to history. At most 200 summaries are retained.",color=Studio.Muted) }
        if(!enabled) item { Panel { Mark(kind="history"); Text("History is off",style=MaterialTheme.typography.titleLarge); Text("Analyze without retaining a record. Enable optional summary history in Settings.",color=Studio.Muted); OutlinedButton(onClick=onSettings) { Text("History settings") } } }
        else if(entries.isEmpty()) item { Panel { Text("Nothing saved yet",style=MaterialTheme.typography.titleLarge); Text("After an analysis, choose Save a local summary.",color=Studio.Muted) } }
        items(entries,key={it.id}) { e -> Panel { Text(Risk.entries.firstOrNull { it.wire==e.category }?.title?:"Saved result",style=MaterialTheme.typography.titleMedium); Text(e.summary,color=Studio.Muted); Text(dateFormat.format(Date(e.createdAt)),style=MaterialTheme.typography.bodyMedium,color=Studio.Muted); Text("${e.inputSource} · model ${e.modelId}",style=MaterialTheme.typography.bodyMedium); TextButton(onClick={ vm.delete(e.id) }) { Text("Delete summary",color=Studio.Red) } } }
        if(entries.isNotEmpty()) item { TextButton(onClick=onClear) { Text("Clear all summaries",color=Studio.Red) } }
    }
}
