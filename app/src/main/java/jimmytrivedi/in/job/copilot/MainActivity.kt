package jimmytrivedi.`in`.job.copilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import jimmytrivedi.`in`.job.copilot.ui.AnalyzeScreen
import jimmytrivedi.`in`.job.copilot.ui.theme.JobCopilotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobCopilotTheme {
                AnalyzeScreen()
            }
        }
    }
}
