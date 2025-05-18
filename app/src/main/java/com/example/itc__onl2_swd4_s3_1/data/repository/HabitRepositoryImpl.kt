package com.example.itc__onl2_swd4_s3_1.data.repository

import com.example.itc__onl2_swd4_s3_1.data.entity.CompletedDayEntity
import com.example.itc__onl2_swd4_s3_1.data.entity.HabitEntity
import com.example.itc__onl2_swd4_s3_1.data.local.database.HabitDatabase
import com.example.itc__onl2_swd4_s3_1.domain.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class HabitRepositoryImpl @Inject constructor(
    private val database: HabitDatabase
) : HabitRepository {

    override suspend fun getCompletedDays(): List<String> = withContext(Dispatchers.IO) {
        return@withContext database.completedDayDao().getAllCompletedDays().first().map { it.date }
    }

    override suspend fun markDayIfCompleted() = withContext(Dispatchers.IO) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val habits = database.habitDao().getActiveHabitsNow(today)
        if (habits.isNotEmpty()) {
            if (habits.all { it.isCompleted }) {
                database.completedDayDao().insert(CompletedDayEntity(today))
            } else {
                database.completedDayDao().deleteByDate(today)
            }
        } else {
            database.completedDayDao().deleteByDate(today)
        }
    }

    override fun getActiveHabits(date: String): Flow<List<HabitEntity>> {
        return database.habitDao().getActiveHabits(date)
    }

    override suspend fun insertHabit(habit: HabitEntity) = withContext(Dispatchers.IO) {
        database.habitDao().insertHabit(habit)
    }

    override suspend fun updateHabit(habit: HabitEntity) = withContext(Dispatchers.IO) {
        database.habitDao().updateHabit(habit)
    }

    override suspend fun deleteHabit(habit: HabitEntity) = withContext(Dispatchers.IO) {
        database.habitDao().deleteHabit(habit)
    }
}
