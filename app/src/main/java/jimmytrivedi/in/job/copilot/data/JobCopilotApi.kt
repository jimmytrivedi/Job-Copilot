package jimmytrivedi.`in`.job.copilot.data

import jimmytrivedi.`in`.job.copilot.data.models.AnalyzeRequest
import jimmytrivedi.`in`.job.copilot.data.models.Assessment
import jimmytrivedi.`in`.job.copilot.data.models.HealthResponse
import jimmytrivedi.`in`.job.copilot.data.models.LogRequest
import jimmytrivedi.`in`.job.copilot.data.models.LogResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JobCopilotApi {
    @GET("/health")
    suspend fun health(): HealthResponse

    @POST("/analyze")
    suspend fun analyze(@Body request: AnalyzeRequest): Assessment

    @POST("/log-application")
    suspend fun logApplication(@Body request: LogRequest): LogResponse
}
