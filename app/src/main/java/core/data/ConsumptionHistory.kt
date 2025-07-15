package core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

private val HISTORIAL_VASOS = stringPreferencesKey("historial_vasos")

@Serializable
data class ConsumoDia(
    val fecha: String,
    val vasos: Int
)

class ConsumptionHistoryRepository(private val context: Context) {

    private val jsonFormat = Json { ignoreUnknownKeys = true }

    // Lee el historial en formato Map<fecha, vasos>
    val historialFlow: Flow<Map<String, Int>> = context.dataStore.data
        .map { prefs ->
            val jsonString = prefs[HISTORIAL_VASOS] ?: "{}"
            try {
                jsonFormat.decodeFromString<Map<String, Int>>(jsonString)
            } catch (e: Exception) {
                emptyMap()
            }
        }

    // Añade vasos para la fecha actual
    suspend fun agregarVasos(cantidad: Int) {
        val hoy = LocalDate.now().format(DateTimeFormatter.ISO_DATE) // e.g. "2025-07-14"

        context.dataStore.edit { prefs ->
            val jsonString = prefs[HISTORIAL_VASOS] ?: "{}"
            val mapaActual = try {
                jsonFormat.decodeFromString<Map<String, Int>>(jsonString).toMutableMap()
            } catch (e: Exception) {
                mutableMapOf()
            }
            val vasosHoy = mapaActual[hoy] ?: 0
            mapaActual[hoy] = vasosHoy + cantidad

            prefs[HISTORIAL_VASOS] = jsonFormat.encodeToString(mapaActual)
        }
    }
}
