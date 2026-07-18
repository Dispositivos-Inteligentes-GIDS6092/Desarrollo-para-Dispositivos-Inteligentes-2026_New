package mx.edu.utng.bgma.smarthealthmonitor.data.remote

import retrofit2.Response
import retrofit2.http.*

interface NeonApiService {
    @POST("sql")
    suspend fun executeSql(@Body request: SqlRequest): Response<SqlResponse>
}

data class SqlRequest(val sql: String)
data class SqlResponse(val rows: List<Map<String, Any>>?)
