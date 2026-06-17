package mx.edu.utng.bgma.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFCDao
import mx.edu.utng.bgma.smarthealthmonitor.data.db.SmartHealthDB

object SmartHealthRepository {
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private val _spo2Flow = MutableStateFlow(0)
    val spo2Flow: StateFlow<Int> = _spo2Flow.asStateFlow()

    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    suspend fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        dao?.insertar(LecturaFC(valorBpm = bpm))
    }

    suspend fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    suspend fun actualizarSpO2(spo2: Int) {
        _spo2Flow.value = spo2
    }

    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()

    suspend fun limpiarHistorialAntiguo(limite: Long = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)) {
        dao?.limpiarViejos(limite)
    }
}
