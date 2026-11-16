package `in`.deven.scamsentry

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import `in`.deven.scamsentry.ui.ScamSentryTheme
import `in`.deven.scamsentry.ui.ScamSentryRoot

class MainActivity : ComponentActivity() {
    private val model by viewModels<AnalysisViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if(savedInstanceState==null) model.receive(intent)
        setContent { ScamSentryTheme { ScamSentryRoot(model) } }
    }
    override fun onNewIntent(intent: Intent) { super.onNewIntent(intent); setIntent(intent); model.receive(intent) }
}
