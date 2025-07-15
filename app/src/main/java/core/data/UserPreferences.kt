package core.data

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Crea la instancia de DataStore en el contexto
val Context.dataStore by preferencesDataStore(name = "user_preferences")

object PreferenceKeys {
    val GOAL_VASOS = intPreferencesKey("goal_vasos")
}

class UserPreferences(private val context: Context) {

    fun getGoalVasos(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[PreferenceKeys.GOAL_VASOS] ?: 10 // valor por defecto
        }
    }

    suspend fun saveGoalVasos(goal: Int) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.GOAL_VASOS] = goal
        }
    }
}
