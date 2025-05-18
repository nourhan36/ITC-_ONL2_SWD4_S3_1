package com.example.itc__onl2_swd4_s3_1.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.itc__onl2_swd4_s3_1.data.entity.SalahEntity

@Dao
interface SalahDao {
    @Query("SELECT * FROM salah_records WHERE date = :date")
    suspend fun getPrayersForDate(date: String): SalahEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: SalahEntity)

    @Query("SELECT * FROM salah_records")
    suspend fun getAllRecords(): List<SalahEntity>

}