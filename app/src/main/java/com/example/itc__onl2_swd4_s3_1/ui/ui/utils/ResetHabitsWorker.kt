package com.example.itc__onl2_swd4_s3_1.ui.ui.utils

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.itc__onl2_swd4_s3_1.ui.data.HabitDatabase
import kotlinx.coroutines.runBlocking

class ResetHabitsWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val dao = HabitDatabase.getDatabase(applicationContext).habitDao()

        runBlocking {
            dao.deleteAllHabits()
        }
        return Result.success()
    }
}