package mx.edu.utng.bgma.smarthealthmonitor.wear.data

import android.util.Log
import mx.edu.utng.bgma.smarthealthmonitor.data.remote.NeonClient
import mx.edu.utng.bgma.smarthealthmonitor.data.remote.SqlRequest
import java.text.SimpleDateFormat
import java.util.*

class WearNeonRepository {
    private val api = NeonClient.service
    private val TAG = "WearNeonRepository"

    suspend fun publicarANeon(bpm: Int) {
        try {
            val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val estado = if (bpm in 60..100) "Normal" else "Alerta"
            
            val sql = """
                INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora, fecha, sincronizado)
                VALUES ($bpm, '$estado', 'Wearable', '$hora', '$fecha', true)
            """.trimIndent()

            val response = api.executeSql(SqlRequest(sql))
            if (response.isSuccessful) {
                Log.d(TAG, "⌚ FC enviada a Neon: $bpm bpm")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al enviar a Neon: ${e.message}")
        }
    }
}
