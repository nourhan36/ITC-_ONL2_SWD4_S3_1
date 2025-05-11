package com.example.itc__onl2_swd4_s3_1.ui.ui.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.itc__onl2_swd4_s3_1.ui.data.HabitDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ResetHabitsWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val habitDao = HabitDatabase.getDatabase(applicationContext).habitDao()
            val allHabits = habitDao.getAllHabitsList()

            val todayIsoDay = LocalDate.now().dayOfWeek.value // ISO: Monday=1, Sunday=7

            for (habit in allHabits) {
                when (habit.repeatType) {
                    "Every Day" -> {
                        // Reset habit completion for today
                        habitDao.insertHabit(habit.copy(isCompleted = false))
                    }
                    "Weekly" -> {
                        // Check if today matches start day
                        if (habit.startDayOfWeek == todayIsoDay) {
                            habitDao.insertHabit(habit.copy(isCompleted = false))
                        }
                    }
                    "None" -> {
                        // Do nothing for one-time habits
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}