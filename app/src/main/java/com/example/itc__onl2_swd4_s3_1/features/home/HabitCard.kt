package com.example.itc__onl2_swd4_s3_1.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.data.entity.HabitEntity
import androidx.compose.ui.res.stringResource
import com.example.itc__onl2_swd4_s3_1.R

@Composable
fun HabitCard(habit: HabitEntity, onCheck: () -> Unit) {
    val backgroundColor = if (habit.isCompleted) Color(0xFFD1FAD7) else Color(0xFFFFF1D1)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val habitNameId = when (habit.name) {
                    "habit_prayer" -> R.string.habit_prayer
                    "habit_quran" -> R.string.habit_quran
                    "habit_fasting" -> R.string.habit_fasting
                    else -> null
                }

                Text(
                    text = when (habit.name) {
                        "habit_prayer" -> stringResource(R.string.habit_prayer)
                        "habit_fasting" -> stringResource(R.string.habit_fasting)
                        "habit_quran" -> stringResource(R.string.habit_quran)
                        else -> habit.name
                    },
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            Checkbox(
                checked = habit.isCompleted,
                onCheckedChange = { onCheck() }
            )
        }
    }
}
