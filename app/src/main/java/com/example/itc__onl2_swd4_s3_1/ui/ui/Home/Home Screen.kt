package com.example.itc__onl2_swd4_s3_1.ui.Home


import android.app.Activity
import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.ui.ui.Home.NavItem
import com.example.itc__onl2_swd4_s3_1.ui.ui.Home.getCurrentDate
import com.example.itc__onl2_swd4_s3_1.ui.ui.ManageSalah.SalahContainerActivity
import com.example.itc__onl2_swd4_s3_1.ui.ui.ProgressPage.ProgressTrackerPage
import com.example.itc__onl2_swd4_s3_1.ui.ui.dhikr.DhikrCounterActivity
import com.example.itc__onl2_swd4_s3_1.ui.ui.habitSelector.HabitSelector
import com.example.itc__onl2_swd4_s3_1.ui.ui.newHabitSetup.HabitViewModel
import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.ui.ui.utils.ResetHabitsWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleHabitReset(applicationContext)
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {

                navBar(
                    onFabClick = { openHomeActivity() },
                    onNavItemClick = { index -> handleNavClick(index) }
                )
            }
        }
    }


    private fun openHomeActivity() {
        val intent = Intent(this, HabitSelector::class.java)
        startActivity(intent)
    }

    private fun handleNavClick(index: Int) {
        when (index) {
            0 -> openHomeActivity()
           1-> startActivity(Intent(this, SalahContainerActivity::class.java))
            2 -> startActivity(Intent(this, DhikrCounterActivity::class.java))
            3-> startActivity(Intent(this, ProgressTrackerPage::class.java))

        }
    }
}

@Composable
fun navBar(
    onFabClick: () -> Unit,
    onNavItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: HabitViewModel = viewModel()
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
            Content(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
        }

    }
}

fun scheduleHabitReset(context: Context) {
    val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()

    // تعيين 12:00 ظهرًا
    dueDate.set(Calendar.HOUR_OF_DAY, 0)
    dueDate.set(Calendar.MINUTE, 0)
    dueDate.set(Calendar.SECOND, 0)

    if (dueDate.before(currentDate)) {
        dueDate.add(Calendar.DAY_OF_MONTH, 1)
    }

    val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

    val dailyWorkRequest = PeriodicWorkRequestBuilder<ResetHabitsWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "resetHabits",
        ExistingPeriodicWorkPolicy.UPDATE,
        dailyWorkRequest
    )
}
@Composable
fun Content(viewModel: HabitViewModel, modifier: Modifier = Modifier) {
    val selectedFilter = viewModel.selectedFilter
    val habits by viewModel.filteredHabits.collectAsState(initial = emptyList())

    val context = LocalContext.current
    val activity = context as? Activity

    // 🔁 المراقبة التلقائية للساعة 12
    LaunchedEffect(Unit) {
        while (true) {
            val now = Calendar.getInstance()
            if (now.get(Calendar.HOUR_OF_DAY) == 0 && now.get(Calendar.MINUTE) == 0) {
                viewModel.deleteAllHabits()
                Toast.makeText(context, "Habits reset for the new day!", Toast.LENGTH_SHORT).show()
                activity?.recreate()
                delay(60 * 1000L)
            } else {
                delay(30 * 1000L)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ramadan_img),
                contentDescription = "Ramadan Kareem",
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = "Ramadan Habit Tracker",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = getCurrentDate(),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Cairo", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        Text(text = "Egypt", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("All", "Complete", "Incomplete").forEach { label ->
                        Text(
                            text = label,
                            modifier = Modifier
                                .clickable { viewModel.setFilter(label) }
                                .border(
                                    border = if (selectedFilter == label)
                                        BorderStroke(2.dp, Color.Blue)
                                    else BorderStroke(0.dp, Color.Transparent),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(8.dp),
                            fontSize = 20.sp,
                            color = if (selectedFilter == label) Color.Red else MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(habits) { habit ->
                        HabitCard(habit = habit, onCheck = {
                            viewModel.toggleHabitCompletion(habit)
                        })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
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
