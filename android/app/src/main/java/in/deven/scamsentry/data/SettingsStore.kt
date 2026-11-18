package `in`.deven.scamsentry.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import androidx.datastore.preferences.core.emptyPreferences

private val Context.preferences by preferencesDataStore("settings")
enum class AppTheme(val label: String) { SYSTEM("System"), LIGHT("Light"), DARK("Dark") }
data class PresentationPreferences(val theme: AppTheme = AppTheme.SYSTEM, val animate: Boolean = true)

class SettingsStore(private val context: Context) {
    private val historyKey = booleanPreferencesKey("history_enabled")
    private val onboardKey = booleanPreferencesKey("onboarding_done")
    private val themeKey = stringPreferencesKey("display_theme")
    private val motionKey = booleanPreferencesKey("decorative_motion")
    private val prefs = context.preferences.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
    val historyEnabled = prefs.map { it[historyKey] ?: false }
    val onboardingDone = prefs.map { it[onboardKey] ?: false }
    val presentation = prefs.map { settings ->
        PresentationPreferences(
            theme = AppTheme.entries.firstOrNull { it.name == settings[themeKey] } ?: AppTheme.SYSTEM,
            animate = settings[motionKey] ?: true,
        )
    }
    suspend fun history(enabled: Boolean) { context.preferences.edit { it[historyKey] = enabled } }
    suspend fun onboard() { context.preferences.edit { it[onboardKey] = true } }
    suspend fun theme(choice: AppTheme) { context.preferences.edit { it[themeKey] = choice.name } }
    suspend fun motion(enabled: Boolean) { context.preferences.edit { it[motionKey] = enabled } }
}
