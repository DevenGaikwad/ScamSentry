package `in`.deven.scamsentry
import `in`.deven.scamsentry.core.*
import `in`.deven.scamsentry.data.ReportExport
import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Files
class ExportPrivacyTest {
    @Test fun previewOmitsIdentifiersAndUsesResultVersion() {
        val text="Fictional OTP 987654 visit https://review.test/?secret=canary"
        val result=AnalysisResult("fictional",1,text,"Typed",Risk.CHECKING,null,Rules.inspect(text),UrlInspector.inspect(text),emptyList(),"fixture-model",emptyList(),policyVersion="fixture-policy")
        val report=ReportExport.preview(result)
        assertFalse(report.contains("987654"));assertFalse(report.contains("review.test"));assertFalse(report.contains("canary"));assertTrue(report.contains("fixture-policy"))
    }
    @Test fun independentReportsSurviveLaterExportAndExpiredFilesArePruned() {
        val root=Files.createTempDirectory("redacted-report-test").toFile()
        try {
            val first=ReportExport.prepareFile(root,"One redacted summary")
            val second=ReportExport.prepareFile(root,"Another summary")
            assertNotEquals(first,second);assertTrue(first.exists());assertTrue(second.exists())
            first.setLastModified(1)
            ReportExport.prepareFile(root,"Third summary")
            assertFalse(first.exists());assertTrue(second.exists())
        } finally { root.deleteRecursively() }
    }
}
