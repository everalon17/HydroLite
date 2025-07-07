package com.example.hydrolite.ui

import com.google.accompanist.pager.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.wear.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import com.example.hydrolite.R
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight



@Composable
fun MainScreen(
    totalVasos: Int,
    goalVasos: Int,
    onAddVaso: () -> Unit,
    onPersonalizarRecordatorioClick: () -> Unit = {},
    onPersonalizarMetaClick: () -> Unit = {}
) {
    val pagerState = rememberPagerState()

    VerticalPager(
        count = 3,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { page ->

        when (page) {
            0 -> {
                // Primera página: progreso y vaso grande
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "$totalVasos / $goalVasos vasos",
                        style = MaterialTheme.typography.title3,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CircularProgressIndicator(
                        progress = (totalVasos.toFloat() / goalVasos).coerceIn(0f, 1f),
                        modifier = Modifier.size(70.dp),
                        strokeWidth = 8.dp,
                        indicatorColor = Color.Cyan
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.vaso_grande),
                            contentDescription = "Vaso grande (250ml)",
                            modifier = Modifier
                                .size(60.dp)
                                .clickable { onAddVaso() }
                        )
                    }
                }
            }


            1 -> {
                // Segunda página: Personalizar recordatorio
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_recordatorio),
                        contentDescription = "Icono recordatorios",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Recordatorios",
                        style = MaterialTheme.typography.title3,
                        color = Color.White
                    )
                }
            }

            2 -> {
                // Tercera página: Personalizar meta
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_meta),
                        contentDescription = "Icono meta diaria",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Meta diaria",
                        style = MaterialTheme.typography.title3,
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun getMotivationalMessage(total: Int, goal: Int): String {
    val ratio = total.toFloat() / goal
    return when {
        ratio == 0f -> "¡Hora de empezar!"
        ratio < 0.25f -> "Buen comienzo 💧"
        ratio < 0.5f -> "Sigue así 👌"
        ratio < 0.75f -> "¡Ya casi llegas! 🔥"
        ratio < 1f -> "Últimos vasos 💪"
        else -> "¡Meta cumplida! 🏆"
    }
}
