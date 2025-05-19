@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.itc__onl2_swd4_s3_1.features.habit_selector

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.features.new_habit_setup.NewHabitSetup
import androidx.compose.ui.res.stringResource
import com.example.itc__onl2_swd4_s3_1.core.components.BaseActivity
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme

class HabitSelector : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    HabitsScreen { habitTitle ->
                        val intent = Intent(this, NewHabitSetup::class.java).apply {
                            putExtra("title", habitTitle)
                        }
                        startActivity(intent)
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun HabitsScreenPreview() {
    ITC_ONL2_SWD4_S3_1Theme {
        HabitsScreen(onClick = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(onClick: (String) -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.select_habit),
                        color = colorScheme.onPrimaryContainer
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    titleContentColor = colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = colorScheme.background
    ) { paddingValues ->
        HabitGrid(Modifier.padding(paddingValues), onClick)
    }
}

@Composable
fun HabitGrid(modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    val habits = listOf(
        Habit(stringResource(R.string.salah_tracker), R.drawable.salah),
        Habit(stringResource(R.string.quran), R.drawable.quraan),
        Habit(stringResource(R.string.dhikr), R.drawable.dhikr),
        Habit(stringResource(R.string.dhul_hijah), R.drawable.kaaba),
        Habit(stringResource(R.string.fasting), R.drawable.fasting),
        Habit(stringResource(R.string.prayers), R.drawable.duaa)
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(habits) { habit ->
            HabitCard(habit) { onClick (habit.title) }
        }
    }
}

@Composable
fun HabitCard(habit: Habit, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = habit.iconRes),
            contentDescription = habit.title,
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = habit.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurfaceVariant
        )
    }
}