package com.example.itc__onl2_swd4_s3_1.ui.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.ui.home.HomeScreen
import com.example.itc__onl2_swd4_s3_1.ui.ui.Home.NavItem
import com.example.itc__onl2_swd4_s3_1.ui.ui.ManageSalah.SalahContainerActivity
import com.example.itc__onl2_swd4_s3_1.ui.ui.ProgressPage.ProgressTrackerPage
import com.example.itc__onl2_swd4_s3_1.ui.ui.dhikr.DhikrCounterActivity
import kotlinx.coroutines.launch

// ✅ Navigation handler
fun handleNavClick(context: Context, index: Int) {
    when (index) {
        0 -> if (context !is HomeScreen) context.startActivity(Intent(context, HomeScreen::class.java))
        1 -> context.startActivity(Intent(context, SalahContainerActivity::class.java))
        2 -> context.startActivity(Intent(context, DhikrCounterActivity::class.java))
        3 -> context.startActivity(Intent(context, ProgressTrackerPage::class.java))
    }
}

// ✅ NavBar Composable
@Composable
fun AppNavBar(
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit,
    onFabClick: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isDarkTheme by remember { mutableStateOf(false) }
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
                DrawerHeader()
                DrawerOptions(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = it },
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


// ✅ Drawer Header
@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile",
            tint = Color.White,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Welcome,", color = Color.White, style = MaterialTheme.typography.titleMedium)
        Text("User name", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
    }
}

// ✅ Drawer Options
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Logout */ }
                .padding(vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logout),
                contentDescription = "Logout",
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Logout", fontSize = 18.sp)
        }
    }
}

// ✅ Language Selector
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
