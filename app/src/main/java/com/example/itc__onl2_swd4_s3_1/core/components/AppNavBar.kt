package com.example.itc__onl2_swd4_s3_1.core.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.features.home.HomeScreen
import com.example.itc__onl2_swd4_s3_1.features.manage_salah.SalahContainerActivity
import com.example.itc__onl2_swd4_s3_1.features.progress_page.ProgressTrackerPage
import com.example.itc__onl2_swd4_s3_1.features.home.NavItem
import com.example.itc__onl2_swd4_s3_1.features.dhikr.DhikrCounterActivity
import kotlinx.coroutines.launch

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
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDarkTheme = drawerThemeState


    var selectedLanguage by remember { mutableStateOf("English") }

    val navItemList = listOf(
        NavItem("home", painterResource(R.drawable.home)),
        NavItem("Salah", painterResource(R.drawable.salah_icon)),
        NavItem("Dhikr", painterResource(R.drawable.zikr)),
        NavItem("Streak", painterResource(R.drawable.progress)),
        NavItem("More", painterResource(R.drawable.menu))
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerOptions(
                    isDarkTheme = isDarkTheme.value,
                    onThemeToggle = onThemeToggle,

                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = { selectedLanguage = it }
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

        LanguageSelector(selectedLanguage, onLanguageSelected)
    }
}

@Composable
fun LanguageSelector(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.language),
            contentDescription = "Language Icon",
            modifier = Modifier.size(35.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = selectedLanguage, fontSize = 18.sp)
        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("English") }, onClick = {
                onLanguageSelected("English")
                expanded = false
            })
            DropdownMenuItem(text = { Text("العربية") }, onClick = {
                onLanguageSelected("العربية")
                expanded = false
            })
        }
    }
}
