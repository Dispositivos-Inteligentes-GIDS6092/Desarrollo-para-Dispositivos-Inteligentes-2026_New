package mx.utng.smarthealthmonitor.tv.data

import android.util.Log
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC
import mx.edu.utng.bgma.smarthealthmonitor.data.remote.NeonClient
import mx.edu.utng.bgma.smarthealthmonitor.data.remote.SqlRequest

class TvNeonRepository {
    private val api = NeonClient.service
    private val TAG = "TvNeonRepository"

    suspend fun obtenerTodoNeon(): List<LecturaFC> {
        return try {
            val sql = "SELECT bpm, estado, dispositivo, hora, fecha FROM lecturas_fc ORDER BY created_at DESC LIMIT 100"
            val response = api.executeSql(SqlRequest(sql))
            
            if (response.isSuccessful) {
                val rows = response.body()?.rows ?: emptyList()
                Log.d(TAG, "📺 Cargado desde Neon: ${rows.size} registros")
                
                rows.map { row ->
                    LecturaFC(
                        valorBpm = (row["bpm"] as? Double)?.toInt() ?: 0,
                        estado = row["estado"] as? String ?: "Normal",
                        dispositivo = row["dispositivo"] as? String ?: "Unknown",
                        hora = row["hora"] as? String ?: "",
                        fecha = row["fecha"] as? String ?: "",
                        sincronizado = true
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al cargar de Neon: ${e.message}")
            emptyList()
        }
    }
}
