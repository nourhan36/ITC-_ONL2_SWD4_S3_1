package com.example.itc__onl2_swd4_s3_1.features.manage_salah

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.components.AppNavBar
import com.example.itc__onl2_swd4_s3_1.core.components.BaseActivity
import com.example.itc__onl2_swd4_s3_1.core.components.handleNavClick
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.features.prayer_times.PrayerApp
import com.google.accompanist.pager.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SalahContainerActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val prefs = context.getSharedPreferences("app_settings", MODE_PRIVATE)
            val isDarkTheme = rememberSaveable { mutableStateOf(prefs.getBoolean("dark_mode", false)) }
            val coroutineScope = rememberCoroutineScope()

            ITC_ONL2_SWD4_S3_1Theme(darkTheme = isDarkTheme.value) {
                AppNavBar(
                    selectedIndex = 1,
                    onIndexChanged = { index -> handleNavClick(this, index) },
                    drawerThemeState = isDarkTheme,
                    onThemeToggle = { enabled ->
                        isDarkTheme.value = enabled
                        prefs.edit().putBoolean("dark_mode", enabled).apply()
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
    val tabTitles = listOf(
        stringResource(R.string.praying_times),
        stringResource(R.string.salah_tracker)
    )

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
                0 -> PrayerApp()
                1 -> SalahTracker()
            }
        }
    }
}
