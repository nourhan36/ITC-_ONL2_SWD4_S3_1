package com.example.itc__onl2_swd4_s3_1.features.manage_salah

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.itc__onl2_swd4_s3_1.data.entity.UserSettingsEntity
import com.example.itc__onl2_swd4_s3_1.data.local.database.HabitDatabase
import com.example.itc__onl2_swd4_s3_1.core.components.AppNavBar
import com.example.itc__onl2_swd4_s3_1.core.components.handleNavClick

import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.features.prayer_times.PrayerApp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SalahContainerActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val db = HabitDatabase.getDatabase(context)
            val settingsDao = db.userSettingsDao()
            val coroutineScope = rememberCoroutineScope()

            val isDarkTheme = rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    val storedSetting = settingsDao.getSettings()
                    isDarkTheme.value = storedSetting?.isDarkMode ?: false
                }
            }

            ITC_ONL2_SWD4_S3_1Theme(darkTheme = isDarkTheme.value) {
                AppNavBar(
                    selectedIndex = 1,
                    onIndexChanged = { index -> handleNavClick(this, index) },
                    drawerThemeState = isDarkTheme,
                    onThemeToggle = { enabled ->
                        coroutineScope.launch {
                            settingsDao.saveSettings(UserSettingsEntity(id = 0, isDarkMode = enabled))
                        }
                        isDarkTheme.value = enabled
                    },
                    onFabClick = null
                ) {
                    SalahTabsScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SalahTabsScreen() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val tabTitles = listOf("Prayer Times", "Salah Tracker")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, color = MaterialTheme.colorScheme.onPrimary) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            count = tabTitles.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> PrayerApp(context = context)
                1 -> SalahTracker()
            }
        }
    }
}
