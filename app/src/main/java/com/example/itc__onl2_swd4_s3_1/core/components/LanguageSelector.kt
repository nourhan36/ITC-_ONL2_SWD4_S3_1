package com.example.itc__onl2_swd4_s3_1.core.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.features.home.HomeScreen

@Composable
fun LanguageSelector(
    selectedLanguage: String, // "en" or "ar"
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val languages = listOf("en" to "English", "ar" to "العربية")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.language),
            contentDescription = "Language Icon",
            modifier = Modifier.size(35.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = getLanguageDisplayName(selectedLanguage),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown Icon",
            modifier = Modifier.size(24.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { (code, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        LocaleHelper.setLocale(context, code)
                        LocaleHelper.saveLanguage(context, code)
                        onLanguageSelected(code)
                        restartApp(context)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun getLanguageDisplayName(code: String): String {
    return when (code) {
        "ar" -> "العربية"
        "en" -> "English"
        else -> "English"
    }
}

fun restartApp(context: Context) {
    val intent = Intent(context, HomeScreen::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    context.startActivity(intent)
}
