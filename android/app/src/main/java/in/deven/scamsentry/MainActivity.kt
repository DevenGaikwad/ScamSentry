package `in`.deven.scamsentry

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.toArgb
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.deven.scamsentry.ui.studio
import `in`.deven.scamsentry.ui.usesDarkTheme
import `in`.deven.scamsentry.ui.ScamSentryTheme
import `in`.deven.scamsentry.ui.ScamSentryRoot

class MainActivity : ComponentActivity() {
    private val model by viewModels<AnalysisViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)
        if (savedInstanceState == null) model.receive(intent)
        setContent {
            val presentation by model.presentation.collectAsStateWithLifecycle()
            val dark = usesDarkTheme(presentation.theme)
            ScamSentryTheme(dark) {
                val paper = studio.paper.toArgb()
                SideEffect {
                    // The window itself supplies the background behind transparent system bars.
                    window.setBackgroundDrawable(ColorDrawable(paper))
                    WindowCompat.getInsetsController(window, window.decorView).apply {
                        isAppearanceLightStatusBars = !dark
                        isAppearanceLightNavigationBars = !dark
                    }
                }
                ScamSentryRoot(model, presentation)
            }
        }
    }
    override fun onNewIntent(intent: Intent) { super.onNewIntent(intent); setIntent(intent); model.receive(intent) }
}
