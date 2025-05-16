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
                // Reset completion for habits that have started
                dao.resetHabitsCompletion(today)
            }
            return Result.success()
        }
    }