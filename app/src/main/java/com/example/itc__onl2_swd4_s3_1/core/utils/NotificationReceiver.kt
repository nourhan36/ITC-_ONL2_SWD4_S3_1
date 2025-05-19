package com.example.itc__onl2_swd4_s3_1.core.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.itc__onl2_swd4_s3_1.data.local.database.HabitDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.itc__onl2_swd4_s3_1.R
import android.content.res.Configuration

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Apply the saved locale
        val localizedContext = applyAppLocale(context)

        Log.d("NotificationReceiver", "⏰ Broadcast received at ${System.currentTimeMillis()}")

        WorkManager.getInstance(localizedContext).enqueue(
            OneTimeWorkRequestBuilder<ResetHabitsWorker>().build()
        )

        CoroutineScope(Dispatchers.IO).launch {
            val dao = HabitDatabase.getDatabase(localizedContext).habitDao()
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val activeHabits = dao.getActiveHabitsNow(today)

            if (activeHabits.isEmpty()) {
                showNotification(
                    localizedContext,
                    localizedContext.getString(R.string.notif_no_habits_title),
                    localizedContext.getString(R.string.notif_no_habits_message)
                )
            } else {
                val incompleteCount = activeHabits.count { !it.isCompleted }

                if (incompleteCount > 0) {
                    showNotification(
                        localizedContext,
                        localizedContext.getString(R.string.notif_incomplete_title),
                        localizedContext.getString(R.string.notif_incomplete_message_plural, incompleteCount)
                    )
                } else {
                    Log.d("NotificationReceiver", "✅ All habits are completed. No notification.")
                }
            }

            scheduleNextDay(localizedContext)
        }
    }



    private fun showNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "habit_reminder",
                context.getString(R.string.notif_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "habit_reminder")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()


        notificationManager.notify(1001, notification)
    }

    private fun scheduleNextDay(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        alarmManager.cancel(pendingIntent)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_MONTH, 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Log.w("NotificationReceiver", "⚠️ Exact alarm not allowed. Skipping next schedule.")
            return
        }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d("NotificationReceiver", "📆 Next notification scheduled at ${calendar.time}")
        } catch (e: SecurityException) {
            Log.e("NotificationReceiver", "❌ SecurityException: ${e.message}")
        }
    }
}
private fun applyAppLocale(context: Context): Context {
    val sharedPref = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    val language = sharedPref.getString("language", "en") ?: "en"

    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.createConfigurationContext(config)
    } else {
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        context
    }
}
