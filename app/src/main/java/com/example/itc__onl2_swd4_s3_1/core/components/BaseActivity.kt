package com.example.itc__onl2_swd4_s3_1.core.components

import android.content.Context
import androidx.activity.ComponentActivity

open class BaseActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getSavedLanguage(newBase)
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
}
