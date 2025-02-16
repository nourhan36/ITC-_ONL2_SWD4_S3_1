package com.example.itc__onl2_swd4_s3_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.itc__onl2_swd4_s3_1.ui.theme.ITC_ONL2_SWD4_S3_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {

            }
        }
    }
}