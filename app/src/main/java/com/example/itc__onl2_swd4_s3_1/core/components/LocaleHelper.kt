package com.example.itc__onl2_swd4_s3_1.core.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.example.itc__onl2_swd4_s3_1.core.utils.Constants

import java.util.*
import androidx.core.content.edit

object LocaleHelper {

    fun setLocale(context: Context, language: String): Context {
        saveLanguage(context, language)
        return updateResources(context, language)
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(Constants.PREF_LANGUAGE_KEY, Constants.DEFAULT_LANGUAGE)
            ?: Constants.DEFAULT_LANGUAGE
    }

    fun saveLanguage(context: Context, language: String) {
        val prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(Constants.PREF_LANGUAGE_KEY, language) }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }
}
