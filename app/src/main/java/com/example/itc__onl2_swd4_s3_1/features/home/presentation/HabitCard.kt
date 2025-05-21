package com.example.itc__onl2_swd4_s3_1.features.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 👆 العنوان الرئيسي
            Text(
                text = habit.name,
                fontSize = 14.sp,
                color = colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // 👇 الرقم (مثلاً عدد التكرارات)
                    Text(
                        text = habit.duration.toString(),
                        fontSize = 20.sp,
                        color = colorScheme.onSurface
                    )

                    // 👇 عرض المدة أسفل الرقم
                    Text(
                        text = "${habit.duration} ${habit.durationUnit}",
                        fontSize = 12.sp,
                        color = colorScheme.onSurfaceVariant
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
}

