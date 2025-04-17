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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSalah(salah: SalahEntity)

    @Query("SELECT * FROM salah_records WHERE date = :date")
    suspend fun getSalahByDate(date: LocalDate): SalahEntity?

    @Query("SELECT * FROM salah_records")
    suspend fun getAllSalah(): List<SalahEntity>

    @Query("DELETE FROM salah_records WHERE date = :date")
    suspend fun deleteSalahByDate(date: LocalDate)
}
