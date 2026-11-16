package `in`.deven.scamsentry

import android.app.Application
import `in`.deven.scamsentry.data.HistoryDatabase
import `in`.deven.scamsentry.data.SettingsStore
import java.io.File

class ScamSentryApp : Application() {
    val settings by lazy { SettingsStore(this) }
    val history by lazy { HistoryDatabase.create(this).entries() }
    override fun onCreate() {
        super.onCreate()
        // Reports contain only redacted derived metadata. Still clean up one-hour-old cache exports on startup.
        File(cacheDir,"exports").listFiles()?.filter { System.currentTimeMillis()-it.lastModified() > 3_600_000 }?.forEach { it.delete() }
    }
}
