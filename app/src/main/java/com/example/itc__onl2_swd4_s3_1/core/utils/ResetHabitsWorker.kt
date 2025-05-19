    package com.example.itc__onl2_swd4_s3_1.core.utils

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import com.example.itc__onl2_swd4_s3_1.R
    import com.example.itc__onl2_swd4_s3_1.data.local.database.HabitDatabase
    import kotlinx.coroutines.runBlocking
    import java.time.LocalDate
    import java.time.format.DateTimeFormatter

    class ResetHabitsWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {
            val dao = HabitDatabase.getDatabase(applicationContext).habitDao()
            val res = applicationContext.resources

            runBlocking {
                val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

                val beforeReset = dao.getActiveHabitsNow(today)
                println("${res.getString(R.string.reset_log_before)}: ${beforeReset.map { it.name to it.isCompleted }}")

                dao.resetHabitsCompletion(today)

                val afterReset = dao.getActiveHabitsNow(today)
                println("${res.getString(R.string.reset_log_after)}: ${afterReset.map { it.name to it.isCompleted }}")
            }

            return Result.success()
        }
    }