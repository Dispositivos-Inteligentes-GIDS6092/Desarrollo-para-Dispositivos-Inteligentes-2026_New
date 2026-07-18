package mx.utng.smarthealthmonitor.tv.data

import android.util.Log
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC
import mx.edu.utng.bgma.smarthealthmonitor.data.remote.NeonClient

class TvNeonRepository {
    private val api = NeonClient.service
    private val TAG = "TvNeonRepository"

    suspend fun obtenerTodoNeon(): List<LecturaFC> {
        return try {
            val lecturas = api.getLecturas()
            Log.d(TAG, "📺 Cargado desde Neon: ${lecturas.size} registros")
            lecturas
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al cargar de Neon: ${e.message}")
            emptyList()
        }
    }
}
