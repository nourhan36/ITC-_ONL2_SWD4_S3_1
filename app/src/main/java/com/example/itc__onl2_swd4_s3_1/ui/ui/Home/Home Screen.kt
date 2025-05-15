package com.example.itc__onl2_swd4_s3_1.ui.Home


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.ui.ui.Home.HabitCard
import com.example.itc__onl2_swd4_s3_1.ui.ui.Home.getCurrentDate
import com.example.itc__onl2_swd4_s3_1.ui.ui.components.AppNavBar
import com.example.itc__onl2_swd4_s3_1.ui.ui.components.handleNavClick
import com.example.itc__onl2_swd4_s3_1.ui.ui.habitSelector.HabitSelector
import com.example.itc__onl2_swd4_s3_1.ui.ui.newHabitSetup.HabitViewModel
import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.ui.ui.utils.ResetHabitsWorker
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

class HomeScreen : ComponentActivity() {

    private val viewModel: HabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleHabitReset(applicationContext)

        // ✅ تحقق من إذا العادات محتاجة Reset عند أول تشغيل
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val lastResetDate = prefs.getString("lastResetDate", null)
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)



        setContent {
            ITC_ONL2_SWD4_S3_1Theme {

                    AppNavBar(
                        selectedIndex = 0,
                        onIndexChanged = { index -> handleNavClick(this, index) },
                        onFabClick = { openHabitSelector() }
                    ) { innerPadding ->
                        Content(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                    }


            }
        }
    }

    private fun openHabitSelector() {
        val intent = Intent(this, HabitSelector::class.java)
        startActivity(intent)
    }



    private fun scheduleHabitReset(context: Context) {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(Calendar.HOUR_OF_DAY, 0)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) dueDate.add(Calendar.DAY_OF_MONTH, 1)
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
}


@Composable
fun Content(viewModel: HabitViewModel, modifier: Modifier = Modifier) {

    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val habits by viewModel.filteredHabits.collectAsState(initial = emptyList())
    // In HomeScreen Content

    val today = LocalDate.now().toString()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ✅ SharedPreferences لتفادي التكرار
    val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshDate()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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

                LazyColumn {
                    items(habits) { habit ->
                        HabitCard(
                            habit = habit,
                            onCheck = { viewModel.toggleHabitCompletion(habit) }
                        )
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

//@Preview
//@Composable
//fun previewMain() {
//    navBar(onFabClick = {}, onNavItemClick = {})
//}
