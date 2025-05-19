package com.example.itc__onl2_swd4_s3_1.features.progress_page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.itc__onl2_swd4_s3_1.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarView(completedDates: List<LocalDate>, modifier: Modifier = Modifier) {
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.value.lengthOfMonth()
    val today = LocalDate.now().dayOfMonth
    val currentLocale = Locale.getDefault()

    val weekDays = listOf(
        stringResource(R.string.sun),
        stringResource(R.string.mon),
        stringResource(R.string.tue),
        stringResource(R.string.wed),
        stringResource(R.string.thu),
        stringResource(R.string.fri),
        stringResource(R.string.sat)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header with month and navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentMonth.value.format(
                    DateTimeFormatter.ofPattern("MMMM yyyy", currentLocale)
                ),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row {
                Text(
                    text = stringResource(R.string.left_arrow),
                    modifier = Modifier.clickable {
                        currentMonth.value = currentMonth.value.minusMonths(1)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.right_arrow),
                    modifier = Modifier.clickable {
                        currentMonth.value = currentMonth.value.plusMonths(1)
                    }
                )
            }
        }

        // Day headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Days grid
        val firstDayOfMonth = currentMonth.value.atDay(1).dayOfWeek.value % 7
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
                            val dateForThisDay = currentMonth.value.atDay(day)
                            val isCompleted = completedDates.contains(dateForThisDay)
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isCompleted) MaterialTheme.colorScheme.secondary
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (day == today && currentMonth.value == YearMonth.now())
                                        Color.Red else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
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
