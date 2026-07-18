package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC
import mx.edu.utng.bgma.smarthealthmonitor.wear.mqtt.MqttWearPublisher

class WearDashboardViewModel : ViewModel() {

    private val mqttPublisher = MqttWearPublisher()
    val isMqttConnected: StateFlow<Boolean> = mqttPublisher.isConnected

    init {
// ...
        mqttPublisher.connect()
        // Publicar cada vez que cambia la FC
        SmartHealthRepository.fcFlow
            .onEach { bpm ->
                if (bpm > 0) {
                    val estado = when {
                        bpm < 60 -> "Baja"
                        bpm > 100 -> "Alta"
                        else -> "Normal"
                    }
                    mqttPublisher.publishFC(bpm, estado)
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        mqttPublisher.disconnect()
    }

    fun manualMqttPublish() {
        val randomBpm = (60..120).random()
        viewModelScope.launch {
            SmartHealthRepository.actualizarFC(randomBpm)
        }
    }

    // Flujo de FC con valor por defecto
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) 72 else it }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            72
        )

    // Flujo de pasos
    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0
        )

    // ← NUEVO: historial desde Room
    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
}