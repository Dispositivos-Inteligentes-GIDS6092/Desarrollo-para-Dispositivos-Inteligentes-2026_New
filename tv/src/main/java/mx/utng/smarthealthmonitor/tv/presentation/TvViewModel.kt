package mx.utng.smarthealthmonitor.tv.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.data.TvNeonRepository
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState
import mx.utng.smarthealthmonitor.tv.mqtt.MqttTvSubscriber

class TvViewModel(
    private val repository: SmartHealthRepository
) : ViewModel() {

    private val neonRepository = TvNeonRepository()
    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    private val mqttSubscriber = MqttTvSubscriber { tvMsg ->
        viewModelScope.launch {
            repository.actualizarFC(tvMsg.bpm)
        }
    }

    init {
        mqttSubscriber.connect()
        cargarDatosDesdeNeon()
        // Observar historial reactivo del Room DAO
        viewModelScope.launch {
            repository.obtenerHistorial()
                .catch { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
                .collect { lecturas ->
                    _state.update { it.copy(lecturas = lecturas, isLoading = false) }
                }
        }

        // Observar FC actual (StateFlow del sensor)
        viewModelScope.launch {
            repository.fcActual.collect { bpm ->
                _state.update { it.copy(fcActual = bpm) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
    }

    private fun cargarDatosDesdeNeon() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val remotos = neonRepository.obtenerTodoNeon()
            if (remotos.isNotEmpty()) {
                _state.update { it.copy(lecturas = remotos, isLoading = false) }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun simularMensajeRecibido() {
        val randomBpm = (60..120).random()
        viewModelScope.launch {
            repository.actualizarFC(randomBpm)
            _state.update { it.copy(showMqttSuccess = true) }
            kotlinx.coroutines.delay(3000)
            _state.update { it.copy(showMqttSuccess = false) }
        }
    }
}
