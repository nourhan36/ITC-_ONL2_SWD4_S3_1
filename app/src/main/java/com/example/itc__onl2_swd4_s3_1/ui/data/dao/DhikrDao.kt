package com.example.itc__onl2_swd4_s3_1.ui.data.dao

import androidx.room.*
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.DhikrEntity

@Dao
interface DhikrDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDhikr(dhikr: DhikrEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dhikrList: List<DhikrEntity>)

    @Query("SELECT * FROM dhikr_table")
    suspend fun getAllDhikr(): List<DhikrEntity>

    @Query("SELECT * FROM dhikr_table WHERE category = :category")
    suspend fun getDhikrByCategory(category: String): List<DhikrEntity>

    @Query("DELETE FROM dhikr_table")
    suspend fun deleteAll()
}
