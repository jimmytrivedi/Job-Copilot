package jimmytrivedi.`in`.job.copilot.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogResponse(
    @SerialName("filename") val logged_as: String,
)
