package com.example.itc__onl2_swd4_s3_1.features.prayer_times

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.components.BaseActivity
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.core.utils.Constants
import com.example.itc__onl2_swd4_s3_1.features.prayer_times.presentation.PrayerTimesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class PrayerTime : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                PrayerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun PrayerApp(viewModel: PrayerTimesViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    var selectedCity by remember { mutableStateOf("Cairo") }
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    var remainingTime by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(selectedCity) {
        isLoading = true
        viewModel.loadPrayerTimes(selectedCity)
        isLoading = false
    }

    LaunchedEffect(prayerTimes) {
        while (true) {
            remainingTime = if (prayerTimes.isNotEmpty()) {
                calculateRemainingTime(prayerTimes.map { it.name to it.time }, context)
            } else {
                context.getString(R.string.no_data_available)
            }
            delay(1000)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.praying_times),
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            CitySelector(selectedCity, colorScheme) { selectedCity = it }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorScheme.primary)
                }
            } else {
                Text(
                    text = remainingTime,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp),
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                LazyColumn {
                    items(prayerTimes) { prayer ->
                        PrayerRow(prayer.name, prayer.time, colorScheme)
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
    val cities = Constants.CITIES
    val context = LocalContext.current

    val selectedCityLabel = cities.find { it.first == selectedCity }?.second?.let {
        stringResource(id = it)
    } ?: selectedCity

    Box(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            )
        ) {
            Text(text = selectedCityLabel)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            cities.forEach { (key, resId) ->
                val label = stringResource(id = resId)
                DropdownMenuItem(
                    text = { Text(label, color = colorScheme.onSurface) },
                    onClick = {
                        onCitySelected(key)
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
    colorScheme: ColorScheme
) {
    val context = LocalContext.current
    val displayName = getLocalizedPrayerName(name, context)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                text = "$displayName - $time",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateRemainingTime(
    prayerTimes: List<Pair<String, String>>,
    context: Context
): String {
    if (prayerTimes.isEmpty()) return context.getString(R.string.no_data_available)

    val now = java.time.LocalDateTime.now()
    val today = java.time.LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    for ((name, time) in prayerTimes) {
        val prayerTime = LocalTime.parse(time, formatter)
        val prayerDateTime = java.time.LocalDateTime.of(today, prayerTime)

        if (prayerDateTime.isAfter(now)) {
            val duration = java.time.Duration.between(now, prayerDateTime)
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            val prayerNameLocalized = getLocalizedPrayerName(name, context)
            return context.getString(R.string.next_prayer, prayerNameLocalized, hours, minutes)
        }
    }
    val fajrTime = LocalTime.parse(prayerTimes[0].second, formatter)
    val tomorrow = today.plusDays(1)
    val nextFajrDateTime = java.time.LocalDateTime.of(tomorrow, fajrTime)

    val duration = java.time.Duration.between(now, nextFajrDateTime)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    val prayerNameLocalized = getLocalizedPrayerName(prayerTimes[0].first, context)
    return context.getString(R.string.next_prayer, prayerNameLocalized, hours, minutes)
}

fun getLocalizedPrayerName(name: String, context: Context): String {
    return when (name.lowercase()) {
        "fajr" -> context.getString(R.string.fajr)
        "dhuhr" -> context.getString(R.string.dhuhr)
        "asr" -> context.getString(R.string.asr)
        "maghrib" -> context.getString(R.string.maghrib)
        "isha" -> context.getString(R.string.isha)
        else -> name
    }
}


