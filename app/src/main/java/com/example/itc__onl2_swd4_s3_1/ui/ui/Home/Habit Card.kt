package com.example.itc__onl2_swd4_s3_1.ui.ui.Home

import com.example.itc__onl2_swd4_s3_1.R


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HabitCard(habit: Habit, onCheckClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Habit Name
            Text(
                text = habit.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            // Streak Days
            Text(
                text = "${habit.streakDays} 🔥",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Check Icon
            IconButton(
                onClick = onCheckClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = if (habit.isChecked) R.drawable.ic_checked else R.drawable.ic_unchecked),
                    contentDescription = if (habit.isChecked) "Checked" else "Unchecked",
                    tint = if (habit.isChecked) Color.Blue else Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
private fun previewHabitCard() {

        HabitCard(
            habit = Habit(name = "Subhan Allah Dhikr", streakDays = 5, isChecked = false),
            onCheckClick = { /* Handle check click */ }
        )

}