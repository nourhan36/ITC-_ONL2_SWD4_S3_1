package com.example.itc__onl2_swd4_s3_1.domain.repository

import com.example.itc__onl2_swd4_s3_1.data.entity.SalahEntity

interface SalahRepository {
    suspend fun getPrayersForDate(date: String): SalahEntity?
    suspend fun insertOrUpdate(entry: SalahEntity)
    suspend fun getAllRecords(): List<SalahEntity>
}
