package com.example.itc__onl2_swd4_s3_1.ui.prayertimes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.ui.homePage.HomeScreen
import com.example.itc__onl2_swd4_s3_1.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            PrayerApp(this)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "prayer_channel", "Prayer Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrayerApp(context: Context) {
    var selectedCity by remember { mutableStateOf("Cairo") }
    var prayerTimes by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var remainingTime by remember { mutableStateOf("0h 0m") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(selectedCity) {
        isLoading = true
        prayerTimes = fetchPrayerTimes(selectedCity)
        if (prayerTimes.isEmpty()) {
            Toast.makeText(context, "Failed to fetch prayer times", Toast.LENGTH_SHORT).show()
        }
        isLoading = false
    }

    LaunchedEffect(prayerTimes) {
        while (true) {
            if (prayerTimes.isNotEmpty()) {
                remainingTime = calculateRemainingTime(prayerTimes)
            } else {
                remainingTime = "No data available"
            }
            delay(1000)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Praying Times",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            CitySelector(selectedCity) { selectedCity = it }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Text(
                    text = "Next Prayer In: $remainingTime",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF03DAC5),
                    fontWeight = FontWeight.Bold
                )

                LazyColumn {
                    items(prayerTimes) { (name, time) ->
                        PrayerRow(name, time, context)
                    }
                }
            }
        }
    }
}

@Composable
fun CitySelector(selectedCity: String, onCitySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val cities = listOf(
        "Alexandria", "Aswan", "Asyut", "Beheira", "Beni Suef", "Cairo", "Dakahlia", "Damietta", "Faiyum", "Gharbia", "Giza", "Ismailia", "Kafr El Sheikh", "Luxor", "Matruh", "Minya", "Monufia", "New Valley", "North Sinai", "Port Said", "Qalyubia", "Qena", "Red Sea", "Sharqia","Sohag", "South Sinai", "Suez"
    )

    Box(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = selectedCity, color = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        onCitySelected(city)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PrayerRow(name: String, time: String, context: Context) {
    var isNotificationEnabled by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$name - $time", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Switch(
                checked = isNotificationEnabled,
                onCheckedChange = {
                    isNotificationEnabled = it
                    if (it) sendNotification(context, name, time)
                },
                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF03DAC5))
            )
        }
    }
}

fun sendNotification(context: Context, prayerName: String, time: String) {
    val notificationManager = NotificationManagerCompat.from(context)
    if (notificationManager.areNotificationsEnabled()) {
        val builder = NotificationCompat.Builder(context, "prayer_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("وقت الصلاة")
            .setContentText("حان الآن وقت صلاة $prayerName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(1, builder.build())
    } else {
        Toast.makeText(context, "Enable notifications in settings", Toast.LENGTH_SHORT).show()
    }
}

suspend fun fetchPrayerTimes(city: String): List<Pair<String, String>> {
    return withContext(Dispatchers.IO) {
        try {
            val url = "https://api.aladhan.com/v1/timingsByCity?city=$city&country=Egypt&method=5"
            val json = URL(url).readText()
            val jsonObject = JSONObject(json)

            if (jsonObject.has("data")) {
                val data = jsonObject.getJSONObject("data").getJSONObject("timings")
                listOf(
                    "Fajr" to data.getString("Fajr"),
                    "Dhuhr" to data.getString("Dhuhr"),
                    "Asr" to data.getString("Asr"),
                    "Maghrib" to data.getString("Maghrib"),
                    "Isha" to data.getString("Isha")
                )
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("API Error", "Error fetching prayer times: ${e.message}")
            emptyList()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateRemainingTime(prayerTimes: List<Pair<String, String>>): String {
    if (prayerTimes.isEmpty()) {
        return "No data available"
    }

    val now = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    for ((name, time) in prayerTimes) {
        val prayerTime = LocalTime.parse(time, formatter)
        if (prayerTime.isAfter(now)) {
            val duration = java.time.Duration.between(now, prayerTime)
            return "$name in ${duration.toHours()}h ${duration.toMinutes() % 60}m"
        }
    }

    val fajrTime = LocalTime.parse(prayerTimes[0].second, formatter)
    val duration = java.time.Duration.between(now, fajrTime.plusHours(24))
    return "Fajr in ${duration.toHours()}h ${duration.toMinutes() % 60}m"
}

@Preview()
@Composable
fun PreviewPrayerApp() {
    val context = LocalContext.current
    PrayerApp(context)
}
