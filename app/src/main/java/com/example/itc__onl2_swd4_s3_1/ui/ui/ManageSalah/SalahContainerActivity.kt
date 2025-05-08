package com.example.itc__onl2_swd4_s3_1.ui.ui.ManageSalah

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.itc__onl2_swd4_s3_1.ui.ui.prayertimes.PrayerApp
import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

class SalahContainerActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                SalahTabsScreen()
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
