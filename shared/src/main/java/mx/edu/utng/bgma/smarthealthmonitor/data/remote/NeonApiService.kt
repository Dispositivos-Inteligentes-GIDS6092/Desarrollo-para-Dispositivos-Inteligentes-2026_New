package mx.edu.utng.bgma.smarthealthmonitor.data.remote

import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC
import retrofit2.Response
import retrofit2.http.*

interface NeonApiService {
    
    @POST("sql")
    suspend fun executeSql(@Body request: SqlRequest): Response<SqlResponse>

    @GET("lecturas")
    suspend fun getLecturas(): List<LecturaFC>

    @POST("lecturas")
    suspend fun insertLectura(@Body lectura: LecturaFC): Response<Void>
}

data class SqlRequest(val sql: String)
data class SqlResponse(val rows: List<Map<String, Any>>?)
