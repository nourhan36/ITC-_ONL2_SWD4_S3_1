package com.example.itc__onl2_swd4_s3_1.ui.Home

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
    // State to hold the current date
    var currentDate by remember { mutableStateOf(getCurrentDate()) }

    // Update the date every second (optional)
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Update every 1 second
            currentDate = getCurrentDate()
        }
    }

    // UI Layout
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

// Function to get the current date in a specific format
fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
    return sdf.format(Date())
}
@Preview
@Composable
private fun prevDate() {

        CurrentDateApp()

}