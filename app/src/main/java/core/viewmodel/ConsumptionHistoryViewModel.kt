package core.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import core.data.ConsumptionHistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConsumptionHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ConsumptionHistoryRepository(application.applicationContext)

    // Estado con el historial para UI, Map<fecha, vasos>
    val historial: StateFlow<Map<String, Int>> = repository.historialFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    fun agregarVasos(cantidad: Int) {
        viewModelScope.launch {
            repository.agregarVasos(cantidad)
        }
    }
}
