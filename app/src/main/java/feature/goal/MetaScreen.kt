package feature.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*

@Composable
fun MetaScreen(
    currentGoal: Int,
    onGoalChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Define tu meta diaria",
                style = MaterialTheme.typography.title2,
                color = Color.Cyan,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "$currentGoal vasos",
                style = MaterialTheme.typography.display1,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            Chip(
                onClick = { onGoalChange(currentGoal + 1) },
                label = { Text("Aumentar", color = Color.White) },
                colors = ChipDefaults.primaryChipColors(backgroundColor = Color(0xFF00BFA6)),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Chip(
                onClick = {
                    if (currentGoal > 1) onGoalChange(currentGoal - 1)
                },
                label = { Text("Disminuir", color = Color.White) },
                colors = ChipDefaults.primaryChipColors(backgroundColor = Color.DarkGray),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
            ) {
                Text("Guardar", color = Color.Black)
            }
        }

        item {
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}