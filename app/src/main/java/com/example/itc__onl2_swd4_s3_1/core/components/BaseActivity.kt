package com.example.itc__onl2_swd4_s3_1.core.components

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat

open class BaseActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getSavedLanguage(newBase)
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
    fun Activity.setEdgeToEdgeStatusBar(isDarkTheme: Boolean) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !isDarkTheme
    }

}
