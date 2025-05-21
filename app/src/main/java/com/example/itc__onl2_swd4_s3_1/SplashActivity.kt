package com.example.itc__onl2_swd4_s3_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.itc__onl2_swd4_s3_1.core.theme.SplashTheme
import com.example.itc__onl2_swd4_s3_1.core.utils.ThemeManager
import com.example.itc__onl2_swd4_s3_1.features.home.HomeScreen
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()

            SplashTheme {
                SplashScreen {
                    // Go to HomeScreen (or any other activity)
                    startActivity(Intent(this, HomeScreen::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashEnd: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF011A42)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }

    LaunchedEffect(Unit) {
        delay(2000)
        onSplashEnd()
    }
}

