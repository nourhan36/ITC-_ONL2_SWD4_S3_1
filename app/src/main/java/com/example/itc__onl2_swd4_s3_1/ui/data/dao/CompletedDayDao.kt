package com.example.itc__onl2_swd4_s3_1.ui.data.dao

import androidx.room.*
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.CompletedDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(day: CompletedDayEntity)

    @Query("SELECT * FROM completed_days")
    fun getAllCompletedDays(): Flow<List<CompletedDayEntity>>

    @Query("DELETE FROM completed_days")
    suspend fun deleteAll()
}