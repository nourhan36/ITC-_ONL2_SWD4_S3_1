package com.example.itc__onl2_swd4_s3_1.features.manage_salah

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.components.AppNavBar
import com.example.itc__onl2_swd4_s3_1.core.components.BaseActivity
import com.example.itc__onl2_swd4_s3_1.core.components.handleNavClick
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.core.utils.ThemeManager
import com.example.itc__onl2_swd4_s3_1.features.prayer_times.PrayerApp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SalahContainerActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            val isDarkTheme = ThemeManager.isDarkMode

            ITC_ONL2_SWD4_S3_1Theme(darkTheme = isDarkTheme.value) {

                val primaryColor = MaterialTheme.colorScheme.primary

                val view = LocalView.current
                SideEffect {
                    val window = (view.context as Activity).window
                    window.statusBarColor = primaryColor.toArgb()
                }

                AppNavBar(
                    selectedIndex = 1,
                    drawerThemeState = isDarkTheme,
                    onIndexChanged = { index -> handleNavClick(this, index) },
                    onThemeToggle = { enabled -> ThemeManager.toggleDarkMode(enabled) },
                    onFabClick = null
                ) {
                    SalahTabsScreen()
                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SalahTabsScreen() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf(
        stringResource(R.string.praying_times),
        stringResource(R.string.salah_tracker)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
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
