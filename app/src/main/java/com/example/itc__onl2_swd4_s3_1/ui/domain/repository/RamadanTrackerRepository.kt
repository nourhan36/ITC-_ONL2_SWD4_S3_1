package com.example.itc__onl2_swd4_s3_1.ui.domain.repository

import com.example.itc__onl2_swd4_s3_1.ui.domain.model.RamadanTrackerDomainModel

interface RamadanTrackerRepository {
    suspend fun fetchRamadanTrackerData():List<RamadanTrackerDomainModel>
}