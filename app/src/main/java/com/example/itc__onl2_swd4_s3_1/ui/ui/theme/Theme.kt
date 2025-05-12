package com.example.itc__onl2_swd4_s3_1.ui.ui.theme

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
    primary = Color(0xFF9FC9E2),              // أفتح من الأزرق المستخدم في Light
    onPrimary = Color(0xFF162E42),            // لون النص على الزر الأساسي

    primaryContainer = Color(0xFF1B3A4B),      // حاوية الزر الأساسي
    onPrimaryContainer = Color(0xFFDDEEFF),   // النص على الحاوية

    secondary = Color(0xFF6BB3D4),            // أفتح برضو من الأزرق المستخدم
    onSecondary = Color(0xFF0B1E2C),

    secondaryContainer = Color(0xFF274A60),
    onSecondaryContainer = Color(0xFFE3F6FF),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE7DFCF),         // نفس الخلفية بتاعت Light

    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE7DFCF),

    error = Color(0xFFF28B82),
    onError = Color(0xFF000000)
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

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ITC_ONL2_SWD4_S3_1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
@Composable
fun FirebaseAuthDemoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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