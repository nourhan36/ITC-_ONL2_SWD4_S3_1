package com.example.itc__onl2_swd4_s3_1.core.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.utils.Constants
import com.example.itc__onl2_swd4_s3_1.features.dhikr.DhikrCounterActivity
import com.example.itc__onl2_swd4_s3_1.features.home.HomeScreen
import com.example.itc__onl2_swd4_s3_1.features.home.NavItem
import com.example.itc__onl2_swd4_s3_1.features.manage_salah.SalahContainerActivity
import com.example.itc__onl2_swd4_s3_1.features.progress_page.ProgressTrackerPage
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource

fun handleNavClick(context: Context, index: Int) {
    when (index) {
        0 -> if (context !is HomeScreen) context.startActivity(Intent(context, HomeScreen::class.java))
        1 -> if (context !is SalahContainerActivity) context.startActivity(Intent(context, SalahContainerActivity::class.java))
        2 -> if (context !is DhikrCounterActivity) context.startActivity(Intent(context, DhikrCounterActivity::class.java))
        3 -> if (context !is ProgressTrackerPage) context.startActivity(Intent(context, ProgressTrackerPage::class.java))
    }
}

@Composable
fun AppNavBar(
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit,
    drawerThemeState: MutableState<Boolean>,
    onThemeToggle: (Boolean) -> Unit,
    onFabClick: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var selectedLanguageCode by remember { mutableStateOf(LocaleHelper.getSavedLanguage(context)) }

    val navItemList = listOf(
        NavItem(stringResource(R.string.nav_home), painterResource(R.drawable.home)),
        NavItem(stringResource(R.string.nav_salah), painterResource(R.drawable.salah_icon)),
        NavItem(stringResource(R.string.nav_dhikr), painterResource(R.drawable.zikr)),
        NavItem(stringResource(R.string.nav_streak), painterResource(R.drawable.progress)),
        NavItem(stringResource(R.string.nav_more), painterResource(R.drawable.menu))
    )


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerOptions(
                    isDarkTheme = drawerThemeState.value,
                    onThemeToggle = onThemeToggle,
                    selectedLanguage = selectedLanguageCode,
                    onLanguageSelected = { newLang -> selectedLanguageCode = newLang }
                )
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                onFabClick?.let {
                    LargeFloatingActionButton(
                        onClick = it,
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                if (index == 4) {
                                    scope.launch { drawerState.open() }
                                } else {
                                    onIndexChanged(index)
                                }
                            },
                            icon = { Icon(navItem.icon, contentDescription = navItem.label) },
                            label = { Text(navItem.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

@Composable
fun DrawerOptions(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.lightdark),
                contentDescription = "Dark Mode",
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Dark Mode", fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isDarkTheme, onCheckedChange = onThemeToggle)
        }

        LanguageSelector(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = onLanguageSelected
        )
    }
}
