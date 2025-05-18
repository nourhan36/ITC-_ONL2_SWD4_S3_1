package com.example.itc__onl2_swd4_s3_1.domain.repository

import com.example.itc__onl2_swd4_s3_1.data.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun getCompletedDays(): List<String>
    suspend fun markDayIfCompleted()
    fun getActiveHabits(date: String): Flow<List<HabitEntity>>
    suspend fun insertHabit(habit: HabitEntity)
    suspend fun updateHabit(habit: HabitEntity)
    suspend fun deleteHabit(habit: HabitEntity)
}
