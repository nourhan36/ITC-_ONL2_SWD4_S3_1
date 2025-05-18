package com.example.itc__onl2_swd4_s3_1.data.repository

import com.example.itc__onl2_swd4_s3_1.domain.model.PrayerTime
import com.example.itc__onl2_swd4_s3_1.domain.repository.PrayerTimesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.inject.Inject

class PrayerTimesRepositoryImpl @Inject constructor() : PrayerTimesRepository {
    override suspend fun getPrayerTimes(city: String): List<PrayerTime> {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://api.aladhan.com/v1/timingsByCity?city=$city&country=Egypt&method=5"
                val json = URL(url).readText()
                val jsonObject = JSONObject(json)

                if (jsonObject.has("data")) {
                    val data = jsonObject.getJSONObject("data").getJSONObject("timings")
                    listOf(
                        PrayerTime("Fajr", data.getString("Fajr")),
                        PrayerTime("Dhuhr", data.getString("Dhuhr")),
                        PrayerTime("Asr", data.getString("Asr")),
                        PrayerTime("Maghrib", data.getString("Maghrib")),
                        PrayerTime("Isha", data.getString("Isha"))
                    )
                } else emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
