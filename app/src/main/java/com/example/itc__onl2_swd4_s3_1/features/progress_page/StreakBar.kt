package com.example.itc__onl2_swd4_s3_1.features.progress_page


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R

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

fun formatStreak(value: Int): String {
    return when {
        value >= 365 -> "${value / 365} year${if (value / 365 > 1) "s" else ""}"
        value >= 30 -> "${value / 30} month${if (value / 30 > 1) "s" else ""}"
        value >= 7 -> "${value / 7} week${if (value / 7 > 1) "s" else ""}"
        else -> "$value day${if (value > 1) "s" else ""}"
    }
}
