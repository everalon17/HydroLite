package feature.reminder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*

@Composable
fun RecordatorioScreen(
    intervalHours: Int,
    intervalMinutes: Int,
    intervalSeconds: Int,
    onHoursChange: (Int) -> Unit,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Intervalo de recordatorio",
                color = Color.Cyan,
                style = MaterialTheme.typography.caption2,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Chip(
                    label = {
                        Text(
                            text = String.format("%02d", intervalHours),
                            color = Color.White
                        )
                    },
                    onClick = { onHoursChange((intervalHours + 1) % 24) },
                    modifier = Modifier.weight(1f),
                    colors = ChipDefaults.primaryChipColors()
                )
                Text(text = ":", color = Color.White, modifier = Modifier.padding(horizontal = 4.dp))

                Chip(
                    label = {
                        Text(
                            text = String.format("%02d", intervalMinutes),
                            color = Color.White
                        )
                    },
                    onClick = { onMinutesChange((intervalMinutes + 5) % 60) },
                    modifier = Modifier.weight(1f),
                    colors = ChipDefaults.primaryChipColors()
                )
                Text(text = ":", color = Color.White, modifier = Modifier.padding(horizontal = 4.dp))

                Chip(
                    label = {
                        Text(
                            text = String.format("%02d", intervalSeconds),
                            color = Color.White
                        )
                    },
                    onClick = { onSecondsChange((intervalSeconds + 10) % 60) },
                    modifier = Modifier.weight(1f),
                    colors = ChipDefaults.primaryChipColors()
                )
            }
        }

        item {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Cyan,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar")
            }
        }

        item {
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver")
            }
        }
    }
}

