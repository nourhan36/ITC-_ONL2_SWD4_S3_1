package com.example.itc__onl2_swd4_s3_1.data.repository

import com.example.itc__onl2_swd4_s3_1.data.dao.UserSettingsDao
import com.example.itc__onl2_swd4_s3_1.data.entity.UserSettingsEntity

class SettingsRepository(private val dao: UserSettingsDao) {
    suspend fun getDarkMode(): Boolean {
        return dao.getSettings()?.isDarkMode ?: false
    }

    suspend fun saveDarkMode(enabled: Boolean) {
        dao.saveSettings(UserSettingsEntity(isDarkMode = enabled))
    }
}
