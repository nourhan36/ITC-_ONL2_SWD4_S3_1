package com.example.itc__onl2_swd4_s3_1.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.data.entity.HabitEntity

@Composable
fun HabitCard(habit: HabitEntity, onCheck: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor = if (habit.isCompleted)
        colorScheme.secondaryContainer
    else
        colorScheme.surfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val label = when (habit.name) {
                    "habit_prayer" -> stringResource(R.string.habit_prayer)
                    "habit_quran" -> stringResource(R.string.habit_quran)
                    "habit_fasting" -> stringResource(R.string.habit_fasting)
                    else -> habit.name
                }

                Text(
                    text = label,
                    fontSize = 18.sp,
                    color = colorScheme.onSurface
                )
            }

            Checkbox(
                checked = habit.isCompleted,
                onCheckedChange = { onCheck() },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorScheme.primary,
                    uncheckedColor = colorScheme.onSurfaceVariant,
                    checkmarkColor = colorScheme.onPrimary
                )
            )
        }
    }
}
