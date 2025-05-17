package com.example.itc__onl2_swd4_s3_1.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9FC9E2),
    onPrimary = Color(0xFF162E42),
    primaryContainer = Color(0xFF1B3A4B),
    onPrimaryContainer = Color(0xFFDDEEFF),
    secondary = Color(0xFF6BB3D4),
    onSecondary = Color(0xFF0B1E2C),
    secondaryContainer = Color(0xFF274A60),
    onSecondaryContainer = Color(0xFFE3F6FF),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE7DFCF),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE7DFCF),
    error = Color(0xFFF28B82),
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF162E42),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF162E42).copy(alpha = 0.2f),
    onPrimaryContainer = Color(0xFF162E42),
    secondary = Color(0xFF407EA2),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF407EA2).copy(alpha = 0.2f),
    onSecondaryContainer = Color(0xFF407EA2),
    background = Color(0xFFE7DFCF),
    onBackground = Color(0xFF162E42),
    surface = Color(0xFFF5F0E6),
    onSurface = Color(0xFF162E42),
    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun ITC_ONL2_SWD4_S3_1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

