    package com.example.itc__onl2_swd4_s3_1.ui.ui.utils

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import com.example.itc__onl2_swd4_s3_1.ui.data.HabitDatabase
    import kotlinx.coroutines.runBlocking
    import java.time.LocalDate
    import java.time.format.DateTimeFormatter

    class ResetHabitsWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {
            val dao = HabitDatabase.getDatabase(applicationContext).habitDao()
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

            runBlocking {
                val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

                // جرب تجيب العادات قبل وبعد علشان تتأكد
                val beforeReset = dao.getActiveHabitsNow(today)
                println("Before reset: ${beforeReset.map { it.name to it.isCompleted }}")

                dao.resetHabitsCompletion(today)

                val afterReset = dao.getActiveHabitsNow(today)
                println("After reset: ${afterReset.map { it.name to it.isCompleted }}")
            }

            return Result.success()
        }
    }