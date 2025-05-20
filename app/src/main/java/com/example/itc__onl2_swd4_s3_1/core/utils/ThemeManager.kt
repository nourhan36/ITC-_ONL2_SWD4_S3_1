package com.example.itc__onl2_swd4_s3_1.core.utils


import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

object ThemeManager {
    private const val PREF_NAME = "theme_settings"
    private const val DARK_MODE_KEY = "is_dark_mode"

    private lateinit var prefs: SharedPreferences
    var isDarkMode = mutableStateOf(false)
        private set

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        isDarkMode.value = prefs.getBoolean(DARK_MODE_KEY, false)
    }

    fun toggleDarkMode(enabled: Boolean) {
        isDarkMode.value = enabled
        prefs.edit().putBoolean(DARK_MODE_KEY, enabled).apply()
    }
}