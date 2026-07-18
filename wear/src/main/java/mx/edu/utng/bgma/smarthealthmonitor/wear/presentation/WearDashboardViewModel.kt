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
import mx.edu.utng.bgma.smarthealthmonitor.wear.data.WearNeonRepository
import mx.edu.utng.bgma.smarthealthmonitor.wear.mqtt.MqttWearPublisher

class WearDashboardViewModel : ViewModel() {

    private val mqttPublisher = MqttWearPublisher()
    private val neonRepository = WearNeonRepository()
    val isMqttConnected: StateFlow<Boolean> = mqttPublisher.isConnected

    init {
        mqttPublisher.connect()
        
        // Observar FC y publicar de forma segura
        SmartHealthRepository.fcFlow
            .onEach { bpm ->
                if (bpm > 0) {
                    val estado = when {
                        bpm < 60 -> "Baja"
                        bpm > 100 -> "Alta"
                        else -> "Normal"
                    }
                    
                    // Publicar a MQTT (asíncrono internamente)
                    mqttPublisher.publishFC(bpm, estado)
                    
                    // Publicar a Neon (suspend function)
                    viewModelScope.launch {
                        neonRepository.publicarANeon(bpm)
                    }
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
            SmartHealthRepository.actualizarFC(randomBpm, "Wearable")
        }
    }

    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) 72 else it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 72)

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val historial: StateFlow<List<LecturaFC>> = SmartHealthRepository.obtenerHistorial()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
