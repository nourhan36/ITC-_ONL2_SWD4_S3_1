package com.example.itc__onl2_swd4_s3_1.ui.Home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.ui.ui.Home.NavItem
import com.example.itc__onl2_swd4_s3_1.ui.ui.Home.getCurrentDate
import com.example.itc__onl2_swd4_s3_1.ui.ui.ManageSalah.SalahTrackerScreen
import com.example.itc__onl2_swd4_s3_1.ui.ui.dhikr.DhikrCounterActivity
import com.example.itc__onl2_swd4_s3_1.ui.ui.homePage.HomeActivity
import kotlinx.coroutines.launch

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navBar(
                onFabClick = { openHomeActivity() },
                onNavItemClick = { index -> handleNavClick(index) }
            )
        }
    }

    private fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun handleNavClick(index: Int) {
        when (index) {
            0 -> openHomeActivity()
            1-> startActivity(Intent(this, SalahTrackerScreen::class.java))
            2 -> startActivity(Intent(this, DhikrCounterActivity::class.java))

        }
    }
}

@Composable
fun navBar(
    onFabClick: () -> Unit,
    onNavItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItemList = listOf(
        NavItem("Home", painterResource(R.drawable.home)),
        NavItem("Salah", painterResource(R.drawable.salah_icon)),
        NavItem("Dhikr", painterResource(R.drawable.zikr)),
        NavItem("Streak", painterResource(R.drawable.progress)),
        NavItem("More", painterResource(R.drawable.menu))
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isDarkTheme by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(vertical = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.qiblah),
                            contentDescription = "Qiblah",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Qiblah", fontSize = 18.sp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lightdark),
                            contentDescription = "light/dark mode",
                            modifier = Modifier.size(35.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Dark Mode", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(150.dp))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { isDarkTheme = it }
                        )
                    }

                    LanguageSelector(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { selectedLanguage = it }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(vertical = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = "logout",
                            modifier = Modifier.size(35.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Logout", fontSize = 18.sp)
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                LargeFloatingActionButton(
                    onClick = onFabClick,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "add")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                if (index == 4) {
                                    scope.launch { drawerState.open() }
                                } else {
                                    onNavItemClick(index)
                                }
                            },
                            icon = { Icon(navItem.icon, "icon") },
                            label = { Text(text = navItem.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Content(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ramadan_img),
        contentDescription = "Ramadan Kareem"
    )
    Column {
        Text(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .padding(top = 22.dp)
                .fillMaxWidth(),
            text = "Ramadan Habit Tracker",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 30.sp
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.padding(horizontal = 15.dp),
                text = getCurrentDate(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Column {
                Text(text = "Cairo", fontSize = 20.sp)
                Text(text = "Egypt", fontSize = 20.sp)
            }
        }
        Spacer(Modifier.padding(top = 150.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var selectedIndex2 by remember { mutableStateOf(-1) }

            listOf("ALL", "Complete", "Incomplete").forEachIndexed { idx, label ->
                Text(
                    modifier = Modifier
                        .clickable { selectedIndex2 = idx }
                        .border(
                            border = if (selectedIndex2 == idx) BorderStroke(2.dp, Color.Blue) else BorderStroke(0.dp, Color.Transparent),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp),
                    fontSize = 25.sp,
                    text = label,
                    color = if (selectedIndex2 == idx) Color.Red else Color.Black
                )
            }
        }
    }
}

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
        Text(
            text = "Welcome,",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "user name",
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodySmall
        )
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
            contentDescription = "language icon",
            modifier = Modifier.size(35.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = selectedLanguage, fontSize = 18.sp)
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown Icon",
            modifier = Modifier.size(24.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
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

@Preview
@Composable
fun previewMain() {
    navBar(onFabClick = {}, onNavItemClick = {})
}
