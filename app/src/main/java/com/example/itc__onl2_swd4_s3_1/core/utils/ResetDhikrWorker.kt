package com.example.itc__onl2_swd4_s3_1.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.itc__onl2_swd4_s3_1.R

class ResetDhikrWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("DhikrPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        sharedPreferences.all.keys.forEach { key ->
            if (key.startsWith("dhikr_")) {
                editor.putBoolean(key, false)
            }
        }
        editor.apply()

        Handler(applicationContext.mainLooper).post {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.dhikr_reset_toast),
                Toast.LENGTH_SHORT
            ).show()
        }

        return Result.success()
    }
}
