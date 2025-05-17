package com.example.itc__onl2_swd4_s3_1.ui.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.SalahEntity
import java.time.LocalDate

@Dao
interface SalahDao {
    @Query("SELECT * FROM salah_records WHERE date = :date")
    suspend fun getPrayersForDate(date: String): SalahEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: SalahEntity)
}
