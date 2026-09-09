package jimmytrivedi.`in`.job.copilot.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jimmytrivedi.`in`.job.copilot.ui.components.BulletCard
import jimmytrivedi.`in`.job.copilot.ui.components.ListSection
import jimmytrivedi.`in`.job.copilot.ui.components.ScoreBadge

private val StrengthGreen = Color(0xFF2E7D32)
private val GapRed = Color(0xFFC62828)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzeScreen(
    viewModel: AnalyzeViewModel = viewModel(factory = AnalyzeViewModel.Factory),
) {
    val ui by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(ui.error) {
        val err = ui.error ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = err,
            actionLabel = "Retry",
            withDismissAction = true,
        )
        viewModel.onErrorDismissed()
        if (result == SnackbarResult.ActionPerformed) {
            viewModel.onAnalyzeClicked()
        }
    }

    LaunchedEffect(ui.loggedAs) {
        val file = ui.loggedAs ?: return@LaunchedEffect
        snackbarHostState.showSnackbar("Logged as $file")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Job Copilot") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        AnalyzeBody(
            ui = ui,
            onJdChanged = viewModel::onJdChanged,
            onClearJd = viewModel::onClearJd,
            onAnalyzeClicked = viewModel::onAnalyzeClicked,
            onLogClicked = viewModel::onLogClicked,
            contentPadding = padding,
        )
    }
}

@Composable
private fun AnalyzeBody(
    ui: UiState,
    onJdChanged: (String) -> Unit,
    onClearJd: () -> Unit,
    onAnalyzeClicked: () -> Unit,
    onLogClicked: () -> Unit,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = ui.jdText,
            onValueChange = onJdChanged,
            modifier = Modifier.fillMaxWidth(),
            enabled = !ui.isAnalyzing,
            minLines = 8,
            placeholder = { Text("Paste the job description here...") },
            trailingIcon = {
                if (ui.jdText.isNotEmpty() && !ui.isAnalyzing) {
                    IconButton(onClick = onClearJd) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear")
                    }
                }
            },
        )

        Button(
            onClick = onAnalyzeClicked,
            enabled = ui.jdText.isNotBlank() && !ui.isAnalyzing,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (ui.isAnalyzing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(Modifier.size(8.dp))
                Text("Analyzing…")
            } else {
                Text("Analyze")
            }
        }

        val assessment = ui.assessment
        if (assessment != null) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                ScoreBadge(score = assessment.match_score)
            }

            Text(
                text = "“${assessment.verdict}”",
                style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                modifier = Modifier.fillMaxWidth(),
            )

            ListSection(
                title = "Strengths",
                items = assessment.strengths,
                icon = Icons.Outlined.CheckCircle,
                iconTint = StrengthGreen,
            )

            ListSection(
                title = "Gaps",
                items = assessment.gaps,
                icon = Icons.Outlined.WarningAmber,
                iconTint = GapRed,
            )

            if (assessment.tailored_bullets.isNotEmpty()) {
                Text(
                    text = "Tailored bullets for this JD",
                    style = MaterialTheme.typography.titleMedium,
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    assessment.tailored_bullets.forEach { bullet ->
                        BulletCard(text = bullet)
                    }
                }
            }

            OutlinedButton(
                onClick = onLogClicked,
                enabled = !ui.isLogging && ui.loggedAs == null,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (ui.isLogging) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("Logging…")
                } else if (ui.loggedAs != null) {
                    Text("Logged ✓")
                } else {
                    Text("Log this application")
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
