package com.example.itc__onl2_swd4_s3_1.features.prayer_times

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.domain.model.PrayerTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class PrayerTime : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                PrayerApp(this)
            }
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
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun PrayerApp(context: Context, viewModel: PrayerTimesViewModel = hiltViewModel()) {
    var selectedCity by remember { mutableStateOf("Cairo") }
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    var remainingTime by remember { mutableStateOf("0h 0m") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(selectedCity) {
        isLoading = true
        viewModel.loadPrayerTimes(selectedCity)
        isLoading = false
    }

    LaunchedEffect(prayerTimes) {
        while (true) {
            if (prayerTimes.isNotEmpty()) {
                remainingTime = calculateRemainingTime(prayerTimes.map { it.name to it.time })
            } else {
                remainingTime = "No data available"
            }
            delay(1000)
        }
    }

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Praying Times",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorScheme.primary
                )
            )
        },
        containerColor = colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            CitySelector(selectedCity, colorScheme) { selectedCity = it }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorScheme.primary)
                }
            } else {
                Text(
                    text = "Next Prayer In: $remainingTime",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp),
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                LazyColumn {
                    items(prayerTimes) { prayer ->
                        PrayerRow(prayer.name, prayer.time, context, colorScheme)
                    }
                }
            }
        }
    }
}

@Composable
fun CitySelector(
    selectedCity: String,
    colorScheme: ColorScheme,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val cities = listOf(
        "Alexandria", "Aswan", "Asyut", "Beheira", "Beni Suef", "Cairo", "Dakahlia",
        "Damietta", "Faiyum", "Gharbia", "Giza", "Ismailia", "Kafr El Sheikh", "Luxor",
        "Matruh", "Minya", "Monufia", "New Valley", "North Sinai", "Port Said",
        "Qalyubia", "Qena", "Red Sea", "Sharqia", "Sohag", "South Sinai", "Suez"
    )

    Box(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            )
        ) {
            Text(text = selectedCity)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city, color = colorScheme.onSurface) },
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
fun PrayerRow(
    name: String,
    time: String,
    context: Context,
    colorScheme: ColorScheme
) {
    var isNotificationEnabled by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$name - $time",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
            Switch(
                checked = isNotificationEnabled,
                onCheckedChange = {
                    isNotificationEnabled = it
                    if (it) sendNotification(context, name)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorScheme.primary,
                    checkedTrackColor = colorScheme.primaryContainer,
                    uncheckedThumbColor = colorScheme.outline,
                    uncheckedTrackColor = colorScheme.surfaceVariant
                )
            )
        }
    }
}

fun sendNotification(context: Context, prayerName: String) {
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

@RequiresApi(Build.VERSION_CODES.O)
fun calculateRemainingTime(prayerTimes: List<Pair<String, String>>): String {
    if (prayerTimes.isEmpty()) return "No data available"

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
