package mx.edu.utng.bgma.smarthealthmonitor.data.repository

import android.util.Log
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import mx.edu.utng.bgma.smarthealthmonitor.data.remote.NeonClient

class SyncRepository {
    private val api = NeonClient.service
    private val local = SmartHealthRepository
    private val TAG = "SyncRepository"

    suspend fun syncWithNeon() {
        try {
            // 1. Subir locales no sincronizados
            val noSincronizados = local.obtenerNoSincronizados()
            Log.d(TAG, "⬆️ Sincronizando ${noSincronizados.size} registros locales...")
            
            noSincronizados.forEach { lectura ->
                val response = api.insertLectura(lectura)
                if (response.isSuccessful) {
                    local.marcarSincronizado(lectura.id)
                }
            }

            // 2. Descargar nuevos de Neon
            val remotos = api.getLecturas()
            Log.d(TAG, "✅ ${remotos.size} registros descargados de Neon")
            
            remotos.forEach { remota ->
                local.upsertLectura(remota.copy(sincronizado = true))
            }
            
            Log.d(TAG, "✅ Sync completado")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en Sync: ${e.message}")
        }
    }
}
