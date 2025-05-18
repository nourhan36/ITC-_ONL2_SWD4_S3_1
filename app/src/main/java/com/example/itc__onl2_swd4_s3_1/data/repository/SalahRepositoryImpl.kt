package com.example.itc__onl2_swd4_s3_1.data.repository

import com.example.itc__onl2_swd4_s3_1.data.entity.SalahEntity
import com.example.itc__onl2_swd4_s3_1.data.local.dao.SalahDao
import com.example.itc__onl2_swd4_s3_1.domain.repository.SalahRepository
import javax.inject.Inject

class SalahRepositoryImpl @Inject constructor(
    private val dao: SalahDao
) : SalahRepository {
    override suspend fun getPrayersForDate(date: String): SalahEntity? = dao.getPrayersForDate(date)

    override suspend fun insertOrUpdate(entry: SalahEntity) = dao.insertOrUpdate(entry)

    override suspend fun getAllRecords(): List<SalahEntity> = dao.getAllRecords()
}
