package jimmytrivedi.`in`.job.copilot.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import jimmytrivedi.`in`.job.copilot.data.JobCopilotApi
import jimmytrivedi.`in`.job.copilot.data.NetworkModule
import jimmytrivedi.`in`.job.copilot.data.models.AnalyzeRequest
import jimmytrivedi.`in`.job.copilot.data.models.Assessment
import jimmytrivedi.`in`.job.copilot.data.models.LogRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class UiState(
    val jdText: String = "",
    val assessment: Assessment? = null,
    val isAnalyzing: Boolean = false,
    val isLogging: Boolean = false,
    val loggedAs: String? = null,
    val error: String? = null,
)

class AnalyzeViewModel(
    private val api: JobCopilotApi,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var analyzeJob: Job? = null
    private var logJob: Job? = null

    init {
        viewModelScope.launch {
            try {
                val resp = api.health()
                Log.i(TAG, "health=${resp.status}")
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Log.w(TAG, "health check failed: ${t.message}")
            }
        }
    }

    fun onJdChanged(newText: String) {
        _state.update { it.copy(jdText = newText) }
    }

    fun onClearJd() {
        _state.update { it.copy(jdText = "") }
    }

    fun onAnalyzeClicked() {
        val jd = _state.value.jdText
        if (jd.isBlank() || _state.value.isAnalyzing) return
        analyzeJob?.cancel()
        analyzeJob = viewModelScope.launch {
            _state.update { it.copy(isAnalyzing = true, error = null) }
            try {
                val result = api.analyze(AnalyzeRequest(jd))
                _state.update {
                    it.copy(
                        assessment = result,
                        isAnalyzing = false,
                        loggedAs = null,
                        error = null,
                    )
                }
            } catch (t: Throwable) {
                if (t is CancellationException) {
                    _state.update { it.copy(isAnalyzing = false) }
                    throw t
                }
                Log.w(TAG, "analyze failed", t)
                _state.update {
                    it.copy(
                        isAnalyzing = false,
                        error = friendlyError("Analyze", t),
                    )
                }
            }
        }
    }

    fun onLogClicked() {
        val assessment = _state.value.assessment ?: return
        if (_state.value.isLogging || _state.value.loggedAs != null) return
        logJob?.cancel()
        logJob = viewModelScope.launch {
            _state.update { it.copy(isLogging = true, error = null) }
            try {
                val resp = api.logApplication(LogRequest(_state.value.jdText, assessment))
                _state.update {
                    it.copy(
                        isLogging = false,
                        loggedAs = resp.logged_as,
                        error = null,
                    )
                }
            } catch (t: Throwable) {
                if (t is CancellationException) {
                    _state.update { it.copy(isLogging = false) }
                    throw t
                }
                Log.w(TAG, "log failed", t)
                _state.update {
                    it.copy(
                        isLogging = false,
                        error = friendlyError("Log", t),
                    )
                }
            }
        }
    }

    fun onErrorDismissed() {
        _state.update { it.copy(error = null) }
    }

    private fun friendlyError(op: String, t: Throwable): String = when (t) {
        is SocketTimeoutException -> "$op timed out. The server took too long to respond."
        is UnknownHostException -> "$op failed: can't reach the server."
        else -> "$op failed: ${t.message ?: t::class.simpleName ?: "unknown error"}"
    }

    companion object {
        private const val TAG = "JobCopilot"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AnalyzeViewModel(NetworkModule.api) as T
            }
        }
    }
}
