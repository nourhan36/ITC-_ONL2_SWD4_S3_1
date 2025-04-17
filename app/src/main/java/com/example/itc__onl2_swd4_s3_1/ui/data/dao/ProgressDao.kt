package com.example.itc__onl2_swd4_s3_1.ui.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.ProgressEntity

@Dao
interface ProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: ProgressEntity)

    @Query("SELECT * FROM progress WHERE date = :date")
    suspend fun getProgressByDate(date: String): ProgressEntity?

    @Query("SELECT * FROM progress ORDER BY date ASC")
    suspend fun getAllProgress(): List<ProgressEntity>

    @Query("SELECT MAX(longestStreak) FROM progress")
    suspend fun getLongestStreak(): Int?

    @Query("SELECT currentStreak FROM progress WHERE date = :date")
    suspend fun getCurrentStreakOnDate(date: String): Int?

    @Query("SELECT date FROM progress WHERE isFullyCompleted = 1")
    suspend fun getFullyCompletedDates(): List<String>

    @Query("""
        SELECT 
        (CAST((completedHabitsCount + completedSalahCount) AS FLOAT) / 
         (totalHabitsCount + totalSalahCount)) 
        FROM progress 
        WHERE date = :date
    """)
    suspend fun getCompletionPercentage(date: String): Float?

    @Query("DELETE FROM progress WHERE date = :date")
    suspend fun deleteProgressByDate(date: String)
}

