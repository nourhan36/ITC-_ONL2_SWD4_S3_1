package com.example.itc__onl2_swd4_s3_1

import android.app.Application
import android.content.Context
import com.example.itc__onl2_swd4_s3_1.core.components.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    override fun attachBaseContext(base: Context) {
        val langContext = LocaleHelper.setLocale(base, LocaleHelper.getSavedLanguage(base))
        super.attachBaseContext(langContext)
    }
}
