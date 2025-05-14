// ProgressTrackerPage.kt
package com.example.itc__onl2_swd4_s3_1.ui.ui.ProgressPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.ui.ui.utils.Constants
import com.example.itc__onl2_swd4_s3_1.ui.ui.newHabitSetup.HabitViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class ProgressTrackerPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                val viewModel: HabitViewModel = viewModel()

                // Filter: Only include completed days that are from yesterday or earlier
                val completedDays by viewModel.allCompletedDays.collectAsState(initial = emptyList())
                val today = LocalDate.now()
                val filteredDays = completedDays.mapNotNull {
                    runCatching { LocalDate.parse(it) }.getOrNull()
                }.filter { it.isBefore(today) } // ✅ exclude today even if it's completed
                val (currentStreak, highestStreak) = remember(filteredDays) {
                    calculateStreaks(filteredDays.map { it.toString() })
                }

                val currentMonthDays = filteredDays.mapNotNull {
                    runCatching { it.dayOfMonth }.getOrNull()
                }

                val firstDay = filteredDays.minOrNull() ?: today
                val totalDaysSinceStart = java.time.temporal.ChronoUnit.DAYS.between(firstDay, today).toInt().coerceAtLeast(1)
                val progress = filteredDays.size / totalDaysSinceStart.toFloat()

                CombinedScreen(
                    currentStreak = currentStreak,
                    highestStreak = highestStreak,
                    progress = progress,
                    completedDays = currentMonthDays
                )
            }
        }
    }
}

@Composable
fun CombinedScreen(currentStreak: Int, highestStreak: Int, progress: Float, completedDays: List<Int>) {
    Column(
        modifier = Modifier
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
        CalendarView(completedDays = completedDays, modifier = Modifier.weight(1.4f))
    }
}

fun formatStreak(value: Int): String {
    return when {
        value >= 365 -> "${value / 365} year${if (value / 365 > 1) "s" else ""}"
        value >= 30 -> "${value / 30} month${if (value / 30 > 1) "s" else ""}"
        value >= 7 -> "${value / 7} week${if (value / 7 > 1) "s" else ""}"
        else -> "$value day${if (value > 1) "s" else ""}"
    }
}

@Composable
fun StreakBar(currentStreak: Int, highestStreak: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        StreakItem(
            value = formatStreak(currentStreak),
            description = stringResource(id = R.string.current_streak),
            modifier = Modifier.weight(1f)
        )
        StreakItem(
            value = formatStreak(highestStreak),
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
            text = stringResource(id = R.string.your_progress),
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
                text = "${(progress * 100).toInt()}${stringResource(id = R.string.progress_percentage)}",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        val message = when {
            progress >= Constants.PROGRESS_COMPLETE -> stringResource(id = R.string.completion_message_congrats)
            progress >= Constants.PROGRESS_ALMOST -> stringResource(id = R.string.completion_message_almost_there)
            progress >= Constants.PROGRESS_GOOD -> stringResource(id = R.string.completion_message_great_job)
            else -> stringResource(id = R.string.completion_message_keep_pushing)
        }

        Text(
            text = message,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
        )
    }
}

//@Composable
//fun StreakBar(currentWeeks: Int, highestWeeks: Int, modifier: Modifier = Modifier) {
//    Row(modifier = modifier) {
//        StreakItem(
//            weeks = currentWeeks,
//            description = stringResource(id = R.string.current_streak),
//            modifier = Modifier.weight(1f)
//        )
//        StreakItem(
//            weeks = highestWeeks,
//            description = stringResource(id = R.string.highest_streak),
//            modifier = Modifier.weight(1f)
//        )
//    }
//}

@Composable
fun CalendarView(completedDays: List<Int>, modifier: Modifier = Modifier) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.lengthOfMonth()
    val today = LocalDate.now().dayOfMonth
    val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row {
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "Previous Month",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "Next Month",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
        val totalCells = firstDayOfMonth + daysInMonth

        Column(modifier = Modifier.fillMaxHeight()) {
            for (i in 0 until totalCells step 7) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (j in 0 until 7) {
                        val day = i + j - firstDayOfMonth + 1
                        if (day in 1..daysInMonth) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (day in completedDays) MaterialTheme.colorScheme.secondary
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (day == today) Color.Red else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(40.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StreakItem(weeks: Int, description: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_streak),
            contentDescription = stringResource(id = R.string.streak_image_description),
            modifier = Modifier.size(32.dp)
        )
        Column(modifier = Modifier.padding(start = 4.dp)) {
Text(
    text = "$weeks ${stringResource(id = if (weeks > 1) R.string.weeks else R.string.week)}",
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


@Preview(showBackground = true, )
@Composable
fun ProgressPreview() {
    ITC_ONL2_SWD4_S3_1Theme(dynamicColor = false) {
CombinedScreen(
            currentStreak = 2,
            highestStreak = 5,
            progress = 0.50f,
            completedDays = listOf(2, 5, 10, 15, 20)
        )
    }
}