package feature.main

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
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    totalVasos: Int,
    goalVasos: Int,
    onAddVaso: () -> Unit,
    onPersonalizarRecordatorioClick: () -> Unit = {},
    onPersonalizarMetaClick: () -> Unit = {},
    onHistorialClick: () -> Unit = {}
) {
    val pagerState = rememberPagerState()

    VerticalPager(
        count = 4,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { page ->
        when (page) {
            0 -> MainPage(totalVasos, goalVasos, onAddVaso)
            1 -> ReminderPage(onPersonalizarRecordatorioClick)
            2 -> GoalPage(onPersonalizarMetaClick)
            3 -> HistoryPage(onHistorialClick)
        }
    }
}

@Composable
fun MainPage(totalVasos: Int, goalVasos: Int, onAddVaso: () -> Unit) {
    val progress = (totalVasos.toFloat() / goalVasos).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600),
        label = "donutVerticalFill"
    )
    val isClicked = remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isClicked.value) 1.15f else 1f,
        animationSpec = tween(200),
        label = "buttonScale"
    )
    val coroutineScope = rememberCoroutineScope()
    val donutThickness = 24.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            // Fondo donut
            Canvas(modifier = Modifier.matchParentSize()) {
                drawArc(
                    color = Color.DarkGray.copy(alpha = 0.18f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = donutThickness.toPx(), cap = StrokeCap.Round)
                )
            }
            // Progreso con gradiente
            Canvas(modifier = Modifier.matchParentSize()) {
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(
                            Color(0xFF00E5FF),
                            Color(0xFF00B8D4),
                            Color(0xFF43EA7F),
                            Color(0xFF00E5FF)
                        )
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(width = donutThickness.toPx(), cap = StrokeCap.Round)
                )
            }
            // Contenido central: contador y botón
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (progress >= 1f) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_meta),
                        contentDescription = "Meta cumplida",
                        tint = Color(0xFFFFD600),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = "$totalVasos / $goalVasos",
                    style = MaterialTheme.typography.display1,
                    color = if (progress >= 1f) Color(0xFF43EA7F) else Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.vaso_grande),
                    contentDescription = "Vaso grande (250ml)",
                    modifier = Modifier
                        .size(60.dp)
                        .scale(scale)
                        .clickable {
                            isClicked.value = true
                            onAddVaso()
                            coroutineScope.launch {
                                delay(200)
                                isClicked.value = false
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun ReminderPage(onPersonalizarRecordatorioClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_recordatorio),
            contentDescription = "Recordatorio",
            tint = Color(0xFF00B8D4),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Personaliza tu recordatorio",
            style = MaterialTheme.typography.title2,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Chip(
            onClick = onPersonalizarRecordatorioClick,
            label = { Text("Configurar", color = Color.Black) },
            colors = ChipDefaults.primaryChipColors(backgroundColor = Color(0xFF00B8D4))
        )
    }
}

@Composable
fun GoalPage(onPersonalizarMetaClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Cambia el icono por uno más representativo, por ejemplo un trofeo
        Icon(
            painter = painterResource(id = R.drawable.ic_trofeo),
            contentDescription = "Meta diaria",
            tint = Color(0xFFFFD600),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Ajusta tu meta diaria",
            style = MaterialTheme.typography.title2,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        Chip(
            onClick = onPersonalizarMetaClick,
            label = { Text("Configurar meta", color = Color.Black) },
            colors = ChipDefaults.primaryChipColors(backgroundColor = Color(0xFF43A047))
        )
    }
}

@Composable
fun HistoryPage(onHistorialClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Cambia el icono por uno más claro, por ejemplo un gráfico o lista
        Icon(
            painter = painterResource(id = R.drawable.ic_grafico),
            contentDescription = "Historial",
            tint = Color(0xFF1976D2),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Historial",
            style = MaterialTheme.typography.title2,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        Chip(
            onClick = onHistorialClick,
            label = { Text("Ver historial", color = Color.Black) },
            colors = ChipDefaults.primaryChipColors(backgroundColor = Color(0xFF1976D2))
        )
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
