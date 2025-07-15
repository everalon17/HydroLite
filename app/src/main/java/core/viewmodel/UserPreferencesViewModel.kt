package core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.content.Context
import androidx.lifecycle.ViewModelProvider


class UserPreferencesViewModel(private val prefs: UserPreferences) : ViewModel() {

    private val _goalVasos = MutableStateFlow(10)
    val goalVasos: StateFlow<Int> = _goalVasos

    init {
        viewModelScope.launch {
            prefs.getGoalVasos().collectLatest { value ->
                _goalVasos.value = value
            }
        }
    }

    fun saveGoalVasos(newGoal: Int) {
        viewModelScope.launch {
            prefs.saveGoalVasos(newGoal)
        }
    }
}

// Factory dentro del mismo archivo (opcional)

class UserPreferencesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java)) {
            val prefs = UserPreferences(context)
            @Suppress("UNCHECKED_CAST")
            return UserPreferencesViewModel(prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

