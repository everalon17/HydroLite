package feature.history

import androidx.compose.foundation.layout.*
import androidx.wear.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.viewmodel.ConsumptionHistoryViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HistoryScreen(
    consumptionHistoryViewModel: ConsumptionHistoryViewModel,
    onBack: () -> Unit
) {
    val historial by consumptionHistoryViewModel.historial.collectAsState(initial = emptyMap())

    Scaffold(
        timeText = { TimeText() },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (historial.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay datos aún",
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Historial de consumo",
                    style = MaterialTheme.typography.caption1, // más pequeño y adecuado
                    color = Color.Cyan,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp) // deja espacio para la hora
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 1 // evita que se desborde
                )
                ScalingLazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(historial.toList().sortedByDescending { it.first }) { (fecha, vasos) ->
                        Text(
                            text = "$fecha: $vasos vasos",
                            style = MaterialTheme.typography.body2,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 32.dp)
                        .padding(horizontal = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray
                    )
                ) {
                    Text("Volver", color = Color.White, style = MaterialTheme.typography.caption2)
                }
            }
        }
    }
}
