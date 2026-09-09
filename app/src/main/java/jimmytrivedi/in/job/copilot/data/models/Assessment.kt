package jimmytrivedi.`in`.job.copilot.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Assessment(
    val match_score: Int,
    val strengths: List<String>,
    val gaps: List<String>,
    val verdict: String,
    val tailored_bullets: List<String>,
)
