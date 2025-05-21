package com.example.itc__onl2_swd4_s3_1.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.components.AppNavBar
import com.example.itc__onl2_swd4_s3_1.core.components.BaseActivity
import com.example.itc__onl2_swd4_s3_1.core.components.LocaleHelper
import com.example.itc__onl2_swd4_s3_1.core.components.handleNavClick
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.core.utils.NotificationHelper
import com.example.itc__onl2_swd4_s3_1.core.utils.ThemeManager
import com.example.itc__onl2_swd4_s3_1.features.home.presentation.HabitCard
import com.example.itc__onl2_swd4_s3_1.features.home.presentation.HabitListItem
import com.example.itc__onl2_swd4_s3_1.features.new_habit_setup.NewHabitSetup
import com.example.itc__onl2_swd4_s3_1.features.new_habit_setup.presentation.HabitFilter
import com.example.itc__onl2_swd4_s3_1.features.new_habit_setup.presentation.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreen : BaseActivity() {

    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getSavedLanguage(newBase)
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setEdgeToEdgeStatusBar(ThemeManager.isDarkMode.value)

        NotificationHelper.scheduleHabitReset(applicationContext)
        NotificationHelper.requestNotificationPermissionIfNeeded(this)
//        NotificationHelper.testNotificationAfterOneMinute(applicationContext)
        NotificationHelper.scheduleDailyNotification(applicationContext)

        setContent {
            val viewModel: HabitViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val isDarkTheme = ThemeManager.isDarkMode

            ITC_ONL2_SWD4_S3_1Theme(darkTheme = isDarkTheme.value) {
                AppNavBar(
                    selectedIndex = 0,
                    drawerThemeState = isDarkTheme,
                    onIndexChanged = { index -> handleNavClick(this, index) },
                    onFabClick = { openHabitSelector() },
                    onThemeToggle = { enabled ->
                        ThemeManager.toggleDarkMode(enabled)
                    }
                ) { innerPadding ->
                    Content(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun openHabitSelector() {
        val intent = Intent(
            this,
            com.example.itc__onl2_swd4_s3_1.features.habit_selector.HabitSelector::class.java
        )
        startActivity(intent)
    }
}

@Composable
fun Content(viewModel: HabitViewModel, modifier: Modifier = Modifier) {
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val habits by viewModel.filteredHabits.collectAsState(initial = emptyList())
    val context = LocalContext.current

    val filters = listOf(
        HabitFilter.ALL to stringResource(R.string.all),
        HabitFilter.COMPLETE to stringResource(R.string.complete),
        HabitFilter.INCOMPLETE to stringResource(R.string.incomplete)
    )

    Column(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ramadan),
            contentDescription = stringResource(R.string.streak_image_description),
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
            filters.forEach { (filter, label) ->
                Text(
                    text = label,
                    modifier = Modifier
                        .clickable { viewModel.setFilter(filter) }
                        .border(
                            width = 2.dp,
                            color = if (selectedFilter == filter) Color.Blue else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp),
                    fontSize = 20.sp,
                    color = if (selectedFilter == filter) Color.Red else MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }

        LazyColumn {
            items(habits, key = { it.id }) { habit ->
                HabitListItem(
                    habit = habit,
                    onEdit = { habitToEdit ->
                        val intent = Intent(context, NewHabitSetup::class.java).apply {
                            putExtra("habitId", habitToEdit.id)
                            putExtra("name", habitToEdit.name)
                            putExtra("startTime", habitToEdit.startTime)
                            putExtra("repeatType", habitToEdit.repeatType)
                            putExtra("duration", habitToEdit.duration)
                            putExtra("reminderTime", habitToEdit.reminderTime)
                            putExtra("startDate", habitToEdit.startDate)
                            putExtra("type", habitToEdit.type)
                        }
                        context.startActivity(intent)
                    },
                    onDeleteConfirmed = { habitToDelete ->
                        viewModel.deleteHabit(habitToDelete)
                    },
                    onToggleComplete = {
                        viewModel.toggleHabitCompletion(habit)
                    }
                )
            }
        }
    }
}
