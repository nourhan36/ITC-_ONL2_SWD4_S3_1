package com.example.itc__onl2_swd4_s3_1.ui.Home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.HabitEntity

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
                Text(text = habit.name, fontSize = 18.sp, color = Color.Black)
                Text(text = "0 days streak", fontSize = 14.sp, color = Color.Gray)
            }
            Checkbox(
                checked = habit.isCompleted,
                onCheckedChange = { onCheck() }
            )
        }
    }
}
@Preview
@Composable
fun HabitCardPreview() {
    HabitCard(
        habit = HabitEntity(
            id = 1,
            name = "Salah",
            startTime = "08:00 AM",
            repeatType = "Daily",
            duration = 30,
            reminderTime = "07:50 AM",
            isCompleted = false
        ),
        onCheck = {}
    )
}