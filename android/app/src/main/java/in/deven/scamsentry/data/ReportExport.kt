package `in`.deven.scamsentry.data

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import `in`.deven.scamsentry.core.AnalysisResult
import java.io.File

object ReportExport {
    fun preview(result: AnalysisResult): String = buildString {
        appendLine("ScamSentry — redacted analysis summary")
        appendLine("Result: ${result.risk.title}")
        appendLine("Model: ${result.modelId}; policy: ordinal_v1")
        appendLine("Observed patterns:")
        result.observations.forEach { appendLine("• ${it.title}: ${it.detail}") }
        appendLine("Links inspected: ${result.urls.size}; destinations omitted.")
        appendLine("Original text, links, identifiers and screenshots are omitted.")
        appendLine("This result does not verify authenticity. Verify unexpected requests independently.")
    }
    fun share(context: Context, text: String) {
        val dir = File(context.cacheDir,"exports").apply { mkdirs() }
        dir.listFiles()?.forEach { it.delete() }
        val file = File(dir,"ScamSentry_Summary.txt").apply { writeText(text) }
        val uri = FileProvider.getUriForFile(context,"${context.packageName}.exports",file)
        val intent = Intent(Intent.ACTION_SEND).apply { type="text/plain"; putExtra(Intent.EXTRA_STREAM,uri); clipData=android.content.ClipData.newRawUri("Redacted summary",uri); addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) }
        context.startActivity(Intent.createChooser(intent,"Share redacted summary"))
    }
}
