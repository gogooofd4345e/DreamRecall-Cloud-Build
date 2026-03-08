package com.dreamrecall.worker

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import java.util.concurrent.TimeUnit

object WorkScheduler {
    private const val WORK_NAME = "DreamReminderWork"

    fun scheduleReminders(context: Context, intervalMinutes: Long) {
        val workRequest = PeriodicWorkRequestBuilder<DreamReminderWorker>(
            intervalMinutes, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE, // Replace existing schedule if user changes settings
            workRequest
        )
    }

    fun cancelReminders(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
