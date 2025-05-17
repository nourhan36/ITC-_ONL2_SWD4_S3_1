package com.example.itc__onl2_swd4_s3_1.data.local.dao

import androidx.room.*
import com.example.itc__onl2_swd4_s3_1.data.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)


    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
        @Query("UPDATE habits SET isCompleted = 0 WHERE startDate <= :today")
        suspend fun resetHabitsCompletion(today: String)

        @Query("SELECT * FROM habits WHERE startDate <= :today")
        fun getActiveHabits(today: String): Flow<List<HabitEntity>>



        @Query("SELECT * FROM habits WHERE startDate <= :today")
        suspend fun getActiveHabitsNow(today: String): List<HabitEntity> // Added suspend


}
