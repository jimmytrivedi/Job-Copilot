package jimmytrivedi.`in`.job.copilot.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LogRequest(val jd: String, val assessment: Assessment)
