package com.example.itc__onl2_swd4_s3_1.data.local.dao

import androidx.room.*
import com.example.itc__onl2_swd4_s3_1.data.entity.CompletedDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(day: CompletedDayEntity)

    @Query("SELECT * FROM completed_days")
    fun getAllCompletedDays(): Flow<List<CompletedDayEntity>>

    @Query("DELETE FROM completed_days WHERE date = :date")
    suspend fun deleteByDate(date: String)

}