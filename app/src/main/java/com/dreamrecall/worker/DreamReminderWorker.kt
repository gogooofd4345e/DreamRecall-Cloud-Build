package com.dreamrecall.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dreamrecall.data.DreamDatabase
import com.dreamrecall.data.DreamRepository
import com.dreamrecall.ui.theme.DarkPurpleAccent

class DreamReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = DreamDatabase.getDatabase(applicationContext)
        val repository = DreamRepository(database.dreamDao())

        // Fetch a dream that hasn't been shown recently
        val dreamToShow = repository.getLeastRecentlyShownDream() ?: return Result.success()

        // Create the notification channel
        createNotificationChannel()

        // Create an intent to open the app and pass the Dream ID
        // Note: For a real app, this should point to your MainActivity
        val intent = Intent(applicationContext, Class.forName("com.dreamrecall.MainActivity")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("SHOW_DREAM_ID", dreamToShow.id)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Placeholder icon
            .setContentTitle("Remember this dream?")
            .setContentText(dreamToShow.title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            // Extract the purple color from our Compose theme (approximate ARGB)
            .setColor(android.graphics.Color.parseColor("#1A0033"))

        // Show the notification
        with(NotificationManagerCompat.from(applicationContext)) {
            // Suppressing permission check for this placeholder code
            try {
                notify(NOTIFICATION_ID, builder.build())
                // Mark dream as shown
                repository.markDreamAsShown(dreamToShow.id)
            } catch (e: SecurityException) {
                // Handle missing exact alarm / notification permissions in a real app
                e.printStackTrace()
            }
        }

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Dream Recall Reminders"
            val descriptionText = "Notifications to remind you of your past dreams"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "dream_recall_channel"
        const val NOTIFICATION_ID = 1001
    }
}
