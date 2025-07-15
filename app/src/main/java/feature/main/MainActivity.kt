package com.example.hydrolite.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.hydrolite.R
import core.notifications.ReminderWorker
import core.ui.theme.HydroLiteTheme
import feature.main.MainScreen
import feature.goal.MetaScreen
import feature.reminder.RecordatorioScreen
import feature.history.HistoryScreen
import core.viewmodel.ConsumptionHistoryViewModel
import core.viewmodel.UserPreferencesViewModel
import core.viewmodel.UserPreferencesViewModelFactory
import java.util.concurrent.TimeUnit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    private lateinit var userPrefsViewModel: UserPreferencesViewModel
    private lateinit var consumptionHistoryViewModel: ConsumptionHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        createNotificationChannel(this)

        val userPrefsFactory = UserPreferencesViewModelFactory(applicationContext)
        userPrefsViewModel = ViewModelProvider(this, userPrefsFactory)[UserPreferencesViewModel::class.java]

        // Crear ConsumptionHistoryViewModel, asumiendo que tiene constructor que recibe context
        consumptionHistoryViewModel = ViewModelProvider(this)[ConsumptionHistoryViewModel::class.java]

        setContent {
            HydroLiteTheme {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    userPrefsViewModel = userPrefsViewModel,
                    consumptionHistoryViewModel = consumptionHistoryViewModel
                )
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "water_reminder_channel",
                "Recordatorios de agua",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para recordatorios de hidratación"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    userPrefsViewModel: UserPreferencesViewModel,
    consumptionHistoryViewModel: ConsumptionHistoryViewModel
) {
    var intervalHours by remember { mutableStateOf(0) }
    var intervalMinutes by remember { mutableStateOf(0) }
    var intervalSeconds by remember { mutableStateOf(10) }
    val context = LocalContext.current
    val metaDiariaVasos by userPrefsViewModel.goalVasos.collectAsState(initial = 10)
    val historial by consumptionHistoryViewModel.historial.collectAsState()
    val hoy = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    val totalVasos = historial[hoy] ?: 0

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                totalVasos = totalVasos,
                goalVasos = metaDiariaVasos,
                onAddVaso = {
                    consumptionHistoryViewModel.agregarVasos(1)
                },
                onPersonalizarRecordatorioClick = { navController.navigate("reminder") },
                onPersonalizarMetaClick = { navController.navigate("goal") },
                onHistorialClick = { navController.navigate("history") }
            )
        }
        composable("reminder") {
            RecordatorioScreen(
                intervalHours = intervalHours,
                intervalMinutes = intervalMinutes,
                intervalSeconds = intervalSeconds,
                onHoursChange = { intervalHours = it },
                onMinutesChange = { intervalMinutes = it },
                onSecondsChange = { intervalSeconds = it },
                onConfirm = {
                    val totalSeconds = intervalHours * 3600 + intervalMinutes * 60 + intervalSeconds
                    if (totalSeconds >= 900) {
                        val periodicRequest = androidx.work.PeriodicWorkRequestBuilder<ReminderWorker>(
                            totalSeconds.toLong(), java.util.concurrent.TimeUnit.SECONDS
                        ).build()
                        androidx.work.WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                            "hydration_reminder",
                            androidx.work.ExistingPeriodicWorkPolicy.REPLACE,
                            periodicRequest
                        )
                    } else {
                        val oneTimeRequest = androidx.work.OneTimeWorkRequestBuilder<ReminderWorker>()
                            .setInitialDelay(totalSeconds.toLong(), java.util.concurrent.TimeUnit.SECONDS)
                            .build()
                        androidx.work.WorkManager.getInstance(context).enqueue(oneTimeRequest)
                    }
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("goal") {
            MetaScreen(
                currentGoal = metaDiariaVasos,
                onGoalChange = { userPrefsViewModel.saveGoalVasos(it) },
                onConfirm = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable("history") {
            HistoryScreen(
                consumptionHistoryViewModel = consumptionHistoryViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    HydroLiteTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}
