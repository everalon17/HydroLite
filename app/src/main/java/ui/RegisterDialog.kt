package com.example.hydrolite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterDialog(
    mlPerVaso: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var cantidadVasos by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("¿Cuántos vasos?") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${cantidadVasos} vaso(s) de ${mlPerVaso} ml")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { if (cantidadVasos > 1) cantidadVasos-- }) {
                        Text("-")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { if (cantidadVasos < 10) cantidadVasos++ }) {
                        Text("+")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val total = cantidadVasos * mlPerVaso
                onConfirm(total)
            }) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}