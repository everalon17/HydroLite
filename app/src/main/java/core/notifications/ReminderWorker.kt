package core.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.hydrolite.R
import java.util.concurrent.atomic.AtomicInteger

object NotificationHelper {
    private const val CHANNEL_ID = "hydration_reminder_channel"
    private var channelCreated = false
    private val notificationId = AtomicInteger(0)

    fun createChannelIfNeeded(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !channelCreated) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Hydration Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            channelCreated = true
        }
    }

    fun getNextNotificationId(): Int = notificationId.incrementAndGet()
    fun getChannelId(): String = CHANNEL_ID
}

class ReminderWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        NotificationHelper.createChannelIfNeeded(applicationContext)
        showNotification("HydroLite", "¡Hora de tomar un vaso de agua!")
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, NotificationHelper.getChannelId())
            .setSmallIcon(R.drawable.ic_recordatorio)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notificationManager.notify(NotificationHelper.getNextNotificationId(), notification)
    }
}
