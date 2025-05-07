package com.example.itc__onl2_swd4_s3_1.ui.ui.dhikr

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

class ResetDhikrWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("DhikrPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Clear all saved checkbox states
        sharedPreferences.all.keys.forEach { key ->
            if (key.startsWith("dhikr_")) {
                editor.putBoolean(key, false)
            }
        }
        editor.apply()

        // Show a toast (requires a handler for UI thread)
        Handler(applicationContext.mainLooper).post {
            Toast.makeText(applicationContext, "Dhikr reset!", Toast.LENGTH_SHORT).show()
        }

        return Result.success()
    }
}