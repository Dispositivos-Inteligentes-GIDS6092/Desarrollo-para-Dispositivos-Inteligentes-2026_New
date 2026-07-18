package mx.edu.utng.bgma.smarthealthmonitor.wear.data

import android.util.Log
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC
import mx.edu.utng.bgma.smarthealthmonitor.data.remote.NeonClient

class WearNeonRepository {
    private val api = NeonClient.service
    private val TAG = "WearNeonRepository"

    suspend fun publicarANeon(bpm: Int) {
        try {
            val lectura = LecturaFC(
                valorBpm = bpm,
                dispositivo = "Wearable"
            )
            val response = api.insertLectura(lectura)
            if (response.isSuccessful) {
                Log.d(TAG, "⌚ FC enviada a Neon: $bpm bpm")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al enviar a Neon: ${e.message}")
        }
    }
}
