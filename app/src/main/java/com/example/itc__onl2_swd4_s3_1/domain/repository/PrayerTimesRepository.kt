package com.example.itc__onl2_swd4_s3_1.domain.repository

import com.example.itc__onl2_swd4_s3_1.domain.model.PrayerTime

interface PrayerTimesRepository {
    suspend fun getPrayerTimes(city: String): List<PrayerTime>
}
