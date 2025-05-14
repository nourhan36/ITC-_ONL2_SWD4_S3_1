package com.example.itc__onl2_swd4_s3_1.ui.data.dao

import androidx.room.*
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE isCompleted = 1")
    fun getCompletedHabits(): Flow<List<HabitEntity>>

    @Query("DELETE FROM habits")
    suspend fun deleteAllHabits()

    @Query("SELECT * FROM habits WHERE isCompleted = 0")
    fun getIncompleteHabits(): Flow<List<HabitEntity>>
    @Query("SELECT * FROM habits")
    suspend fun getAllHabitsNow(): List<HabitEntity> // for immediate check in viewmodel
}
