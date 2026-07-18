package mx.utng.smarthealthmonitor.tv.domain.model

import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC

data class TvUiState(
    val lecturas: List<LecturaFC> = emptyList(),
    val fcActual: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null,
    val showMqttSuccess: Boolean = false
)
