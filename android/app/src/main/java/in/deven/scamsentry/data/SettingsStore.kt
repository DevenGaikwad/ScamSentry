package `in`.deven.scamsentry.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import androidx.datastore.preferences.core.emptyPreferences

private val Context.preferences by preferencesDataStore("settings")
class SettingsStore(private val context: Context) {
    private val historyKey = booleanPreferencesKey("history_enabled")
    private val onboardKey = booleanPreferencesKey("onboarding_done")
    private val prefs = context.preferences.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
    val historyEnabled = prefs.map { it[historyKey] ?: false }
    val onboardingDone = prefs.map { it[onboardKey] ?: false }
    suspend fun history(enabled: Boolean) { context.preferences.edit { it[historyKey] = enabled } }
    suspend fun onboard() { context.preferences.edit { it[onboardKey] = true } }
}
