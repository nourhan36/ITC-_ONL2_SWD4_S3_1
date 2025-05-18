package com.example.itc__onl2_swd4_s3_1.features.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.components.AppNavBar
import com.example.itc__onl2_swd4_s3_1.core.components.handleNavClick
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.core.utils.NotificationHelper
import com.example.itc__onl2_swd4_s3_1.data.entity.UserSettingsEntity
import com.example.itc__onl2_swd4_s3_1.data.local.database.HabitDatabase
import com.example.itc__onl2_swd4_s3_1.features.new_habit_setup.HabitViewModel
import com.example.itc__onl2_swd4_s3_1.features.new_habit_setup.NewHabitSetup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreen : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.scheduleHabitReset(applicationContext)
        NotificationHelper.requestNotificationPermissionIfNeeded(this)

        NotificationHelper.testNotificationAfterOneMinute(applicationContext)
        NotificationHelper.scheduleDailyNotification(applicationContext)

        setContent {
            val viewModel: HabitViewModel = hiltViewModel()
            val context = LocalContext.current
            val db = HabitDatabase.getDatabase(context)
            val settingsDao = db.userSettingsDao()
            val coroutineScope = rememberCoroutineScope()
            val isDarkTheme = remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    val storedSetting = settingsDao.getSettings()
                    isDarkTheme.value = storedSetting?.isDarkMode ?: false
                }
            }

            ITC_ONL2_SWD4_S3_1Theme(darkTheme = isDarkTheme.value) {
                AppNavBar(
                    selectedIndex = 0,
                    drawerThemeState = isDarkTheme,
                    onIndexChanged = { index -> handleNavClick(this, index) },
                    onFabClick = { openHabitSelector() },
                    onThemeToggle = { enabled ->
                        coroutineScope.launch {
                            settingsDao.saveSettings(UserSettingsEntity(id = 0, isDarkMode = enabled))
                        }
                        isDarkTheme.value = enabled
                    }
                ) { innerPadding ->
                    Content(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun openHabitSelector() {
        val intent = Intent(this, com.example.itc__onl2_swd4_s3_1.features.habit_selector.HabitSelector::class.java)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Content(viewModel: HabitViewModel, modifier: Modifier = Modifier) {
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val habits by viewModel.filteredHabits.collectAsState(initial = emptyList())
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ramadan_img),
            contentDescription = "Ramadan Kareem",
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("All", "Complete", "Incomplete").forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier
                        .clickable { viewModel.setFilter(label) }
                        .border(
                            width = 2.dp,
                            color = if (selectedFilter == label) Color.Blue else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp),
                    fontSize = 20.sp,
                    color = if (selectedFilter == label) Color.Red else MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }

        LazyColumn {
            items(habits, key = { it.id }) { habit ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        when (it) {
                            DismissValue.DismissedToStart -> {
                                viewModel.deleteHabit(habit)
                                true
                            }
                            DismissValue.DismissedToEnd -> {
                                val intent = Intent(context, NewHabitSetup::class.java).apply {
                                    putExtra("habitId", habit.id)
                                    putExtra("name", habit.name)
                                    putExtra("startTime", habit.startTime)
                                    putExtra("repeatType", habit.repeatType)
                                    putExtra("duration", habit.duration)
                                    putExtra("reminderTime", habit.reminderTime)
                                    putExtra("startDate", habit.startDate)
                                }
                                context.startActivity(intent)
                                false
                            }
                            else -> false
                        }
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(
                        DismissDirection.EndToStart,
                        DismissDirection.StartToEnd
                    ),
                    background = {
                        val color = when (dismissState.dismissDirection) {
                            DismissDirection.EndToStart -> Color.Red
                            else -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    },
                    dismissContent = {
                        HabitCard(
                            habit = habit,
                            onCheck = { viewModel.toggleHabitCompletion(habit) }
                        )
                    }
                )
            }
        }
    }
}
