package com.example.itc__onl2_swd4_s3_1.features.progress_page

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.components.AppNavBar
import com.example.itc__onl2_swd4_s3_1.core.components.BaseActivity
import com.example.itc__onl2_swd4_s3_1.core.components.handleNavClick
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.core.utils.Constants
import com.example.itc__onl2_swd4_s3_1.features.new_habit_setup.presentation.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.itc__onl2_swd4_s3_1.core.utils.ThemeManager

@AndroidEntryPoint
class ProgressTrackerPage : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Observe theme from ThemeManager
            val isDarkTheme by ThemeManager.isDarkMode

            ITC_ONL2_SWD4_S3_1Theme(darkTheme = isDarkTheme) {
                val viewModel: HabitViewModel = hiltViewModel()
                val completedDays = viewModel.allCompletedDays.collectAsState(initial = emptyList()).value
                val today = LocalDate.now()
                val filteredDays = completedDays.mapNotNull {
                    runCatching { LocalDate.parse(it) }.getOrNull()
                }.filter { it.isBefore(today) }

                val (currentStreak, highestStreak) = calculateStreaks(filteredDays.map { it.toString() })
                val firstDay = filteredDays.minOrNull() ?: today
                val totalDaysSinceStart = ChronoUnit.DAYS.between(firstDay, today).toInt().coerceAtLeast(1)
                val progress = filteredDays.size / totalDaysSinceStart.toFloat()

                AppNavBar(
                    selectedIndex = 3,
                    drawerThemeState = ThemeManager.isDarkMode,
                    onIndexChanged = { index -> handleNavClick(this, index) },
                    onThemeToggle = { ThemeManager.toggleDarkMode(it) }
                ) { innerPadding ->
                    CombinedScreen(
                        currentStreak = currentStreak,
                        highestStreak = highestStreak,
                        progress = progress,
                        completedDates = filteredDays,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CombinedScreen(
    currentStreak: Int,
    highestStreak: Int,
    progress: Float,
    completedDates: List<LocalDate>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StreakBar(
            currentStreak = currentStreak,
            highestStreak = highestStreak,
            modifier = Modifier.weight(0.3f)
        )
        CircularProgressBar(progress = progress, modifier = Modifier.weight(1.3f))
        CalendarView(completedDates = completedDates, modifier = Modifier.weight(1.4f))
    }
}

fun calculateStreaks(completedDates: List<String>): Pair<Int, Int> {
    val sortedDates = completedDates.mapNotNull {
        runCatching { LocalDate.parse(it) }.getOrNull()
    }.sorted()

    if (sortedDates.isEmpty()) return Pair(0, 0)

    var currentStreak = 1
    var highestStreak = 1
    var tempStreak = 1
    var previous = sortedDates.first()

    for (i in 1 until sortedDates.size) {
        val current = sortedDates[i]
        if (previous.plusDays(1) == current) {
            tempStreak++
        } else {
            tempStreak = 1
        }
        highestStreak = maxOf(highestStreak, tempStreak)
        previous = current
    }

    val yesterday = LocalDate.now().minusDays(1)
    currentStreak = if (sortedDates.contains(yesterday)) {
        var streak = 1
        var date = yesterday
        while (sortedDates.contains(date.minusDays(1))) {
            streak++
            date = date.minusDays(1)
        }
        streak
    } else 0

    return Pair(currentStreak, highestStreak)
}

@Composable
fun CircularProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val circleSize = 200.dp
    val strokeWidth = 16.dp
    val primaryColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    val progressGradient = Brush.linearGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = stringResource(R.string.your_progress),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
        )
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(circleSize)) {
            Canvas(modifier = Modifier.size(circleSize)) {
                drawArc(
                    color = primaryColor,
                    startAngle = Constants.START_ANGLE,
                    sweepAngle = Constants.FULL_CIRCLE_DEGREES,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    brush = progressGradient,
                    startAngle = Constants.START_ANGLE,
                    sweepAngle = Constants.FULL_CIRCLE_DEGREES * progress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}

@Composable
fun StreakBar(currentStreak: Int, highestStreak: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        StreakItem(
            value = getLocalizedStreak(currentStreak),
            description = stringResource(id = R.string.current_streak),
            modifier = Modifier.weight(1f)
        )
        StreakItem(
            value = getLocalizedStreak(highestStreak),
            description = stringResource(id = R.string.highest_streak),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StreakItem(value: String, description: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(4.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_streak),
            contentDescription = stringResource(id = R.string.streak_image_description),
            modifier = Modifier.size(32.dp)
        )
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun getLocalizedStreak(value: Int): String {
    val context = LocalContext.current

    return when {
        value >= 365 -> {
            val years = value / 365
            context.resources.getQuantityString(R.plurals.streak_years, years, years)
        }
        value >= 30 -> {
            val months = value / 30
            context.resources.getQuantityString(R.plurals.streak_months, months, months)
        }
        value >= 7 -> {
            val weeks = value / 7
            context.resources.getQuantityString(R.plurals.streak_weeks, weeks, weeks)
        }
        else -> {
            context.resources.getQuantityString(R.plurals.streak_days, value, value)
        }
    }
}