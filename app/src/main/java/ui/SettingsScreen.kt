package com.example.hydrolite.ui

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen(
    currentInterval: Int,
    onIntervalChange: (Int) -> Unit,
    onBack: () -> Unit
) {
    val intervals = listOf(1, 2, 3, 4)
    var selected by remember { mutableStateOf(currentInterval) }

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Intervalo de recordatorios", style = MaterialTheme.typography.title3)
            Spacer(Modifier.height(8.dp))
        }

        items(intervals.size) { index ->
            val value = intervals[index]
            Chip(
                label = { Text("Cada $value hora${if (value > 1) "s" else ""}") },
                onClick = {
                    selected = value
                    onIntervalChange(value)
                },
                colors = ChipDefaults.secondaryChipColors()
            )
        }

        item {
            Spacer(Modifier.height(12.dp))
            Chip(
                label = { Text("Volver") },
                onClick = onBack,
                colors = ChipDefaults.primaryChipColors()
            )
        }
    }
}
