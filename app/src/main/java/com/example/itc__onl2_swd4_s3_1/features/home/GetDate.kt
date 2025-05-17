package com.example.itc__onl2_swd4_s3_1.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*



@Composable
fun CurrentDateApp() {
    var currentDate by remember { mutableStateOf(getCurrentDate()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentDate = getCurrentDate()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = currentDate,
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )
    }
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
    return sdf.format(Date())
}
@Preview
@Composable
private fun PrevDate() {

        CurrentDateApp()

}