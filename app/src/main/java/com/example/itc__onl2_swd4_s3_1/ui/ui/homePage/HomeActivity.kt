@file:OptIn(ExperimentalMaterial3Api::class)

        package com.example.itc__onl2_swd4_s3_1.ui.ui.homePage

        import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
        import com.example.itc__onl2_swd4_s3_1.ui.ui.ManageSalah.SalahTrackerScreen

        import com.example.itc__onl2_swd4_s3_1.ui.ui.dhikr.DhikrCounterActivity
        import com.example.itc__onl2_swd4_s3_1.ui.ui.prayertimes.MainActivity

        import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    HomeScreen { habitTitle ->
                        if (habitTitle == "Dhikr") {
                            startActivity(Intent(this, DhikrCounterActivity::class.java))
                        } else if (habitTitle == "Salah") {
                            startActivity(Intent(this, SalahTrackerScreen::class.java))
                        } else if (habitTitle == "Quran") {
                            // Handle Quran activity
                        } else if (habitTitle == "Fasting") {
                            // Handle Fasting activity
                        } else if (habitTitle == "Dhul Hijah") {
                            // Handle Dhul Hijah activity
                        } else if (habitTitle == "Prayers") {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
            }
        }
    }
}

        @Preview
        @Composable
        fun HomeScreenPreview() {
            ITC_ONL2_SWD4_S3_1Theme {
                HomeScreen(onClick = {})
            }
        }

        @Composable
        fun HomeScreen(onClick: (String) -> Unit) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Select Habit") }
                    )
                },
                containerColor = Color(0xFFF5F5F5)
            ) { paddingValues ->
                HabitGrid(Modifier.padding(paddingValues), onClick)
            }
        }

        @Composable
        fun HabitGrid(modifier: Modifier = Modifier, onClick: (String) -> Unit) {
            val habits = listOf(
                Habit("Salah", R.drawable.salah),
                Habit("Quran", R.drawable.quraan),
                Habit("Dhikr", R.drawable.dhikr),
                Habit("Dhul Hijah", R.drawable.kaaba),
                Habit("Fasting", R.drawable.fasting),
                Habit("Prayers", R.drawable.duaa)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(habits) { habit ->
                    HabitCard(habit) { onClick(habit.title) }
                }
            }
        }

        @Composable
        fun HabitCard(habit: Habit, onClick: () -> Unit) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .clickable { onClick() }
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = habit.iconRes),
                    contentDescription = habit.title,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = habit.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

