package mx.utng.smarthealthmonitor.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFCDao

data class TvState(
    val lecturas: List<LecturaFC> = emptyList(),
    val fcActual: Int = 0
)

class TvViewModel(private val dao: LecturaFCDao) : ViewModel() {

    private val _state = MutableStateFlow(TvState())
    val state: StateFlow<TvState> = _state.asStateFlow()

    init {
        dao.obtenerUltimas().onEach { lecturas ->
            _state.update { it.copy(lecturas = lecturas) }
        }.launchIn(viewModelScope)
    }
}
