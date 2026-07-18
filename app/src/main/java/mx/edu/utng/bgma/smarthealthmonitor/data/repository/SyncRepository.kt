package mx.edu.utng.bgma.smarthealthmonitor.data.repository

import android.util.Log
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC
import mx.edu.utng.bgma.smarthealthmonitor.data.remote.NeonClient
import mx.edu.utng.bgma.smarthealthmonitor.data.remote.SqlRequest

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
                val sql = """
                    INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora, fecha, sincronizado)
                    VALUES (${lectura.valorBpm}, '${lectura.estado}', '${lectura.dispositivo}', '${lectura.hora}', '${lectura.fecha}', true)
                """.trimIndent()
                
                val response = api.executeSql(SqlRequest(sql))
                if (response.isSuccessful) {
                    local.marcarSincronizado(lectura.id)
                }
            }

            // 2. Descargar nuevos de Neon (ejemplo: últimos 50)
            val sqlSelect = "SELECT bpm, estado, dispositivo, hora, fecha FROM lecturas_fc ORDER BY created_at DESC LIMIT 50"
            val response = api.executeSql(SqlRequest(sqlSelect))
            
            if (response.isSuccessful) {
                val rows = response.body()?.rows ?: emptyList()
                Log.d(TAG, "✅ ${rows.size} registros descargados de Neon")
                
                rows.forEach { row ->
                    val lectura = LecturaFC(
                        valorBpm = (row["bpm"] as? Double)?.toInt() ?: 0,
                        estado = row["estado"] as? String ?: "Normal",
                        dispositivo = row["dispositivo"] as? String ?: "Unknown",
                        hora = row["hora"] as? String ?: "",
                        fecha = row["fecha"] as? String ?: "",
                        sincronizado = true
                    )
                    local.upsertLectura(lectura)
                }
            }
            
            Log.d(TAG, "✅ Sync completado")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en Sync: ${e.message}")
        }
    }
}
