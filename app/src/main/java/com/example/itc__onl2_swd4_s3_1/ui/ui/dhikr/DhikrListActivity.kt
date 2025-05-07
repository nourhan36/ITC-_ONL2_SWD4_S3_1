@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.itc__onl2_swd4_s3_1.ui.ui.dhikr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.ui.ui.utils.Constants

class DhikrListActivity : ComponentActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val dhikrListState = mutableStateListOf<Dhikr>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize SharedPreferences for persisting checkbox states
        sharedPreferences = getSharedPreferences("DhikrPrefs", Context.MODE_PRIVATE)

        // Initialize dhikr list
        val dhikrList = getDhikrList().filter { it.category == "تسابيح" }

        // Load saved completion states
        dhikrList.forEach { dhikr ->
            val isCompleted = sharedPreferences.getBoolean("dhikr_${dhikr.content}", false)
            dhikr.isCompleted.value = isCompleted
        }

        dhikrListState.addAll(dhikrList)

        // Check if we're coming from DhikrCounterActivity with a completed dhikr
        val completedDhikrText = intent.getStringExtra(DhikrCounterActivity.DHIKR_COMPLETED_TEXT)
        val isCompleted = intent.getBooleanExtra(DhikrCounterActivity.DHIKR_COMPLETED, false)

        if (completedDhikrText != null && isCompleted) {
            updateDhikrCompletionStatus(completedDhikrText, true)
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultDhikrText = result.data?.getStringExtra(DhikrCounterActivity.DHIKR_COMPLETED_TEXT)
                resultDhikrText?.let { text ->
                    updateDhikrCompletionStatus(text, true)
                }
            }
        }

        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                DhikrScreen(
                    dhikrList = dhikrListState,
                    onDhikrClick = { selectedDhikr ->
                        val intent = Intent(this, DhikrCounterActivity::class.java).apply {
                            putExtra(Constants.DHIKR_TEXT, selectedDhikr.content)
                            putExtra(Constants.DHIKR_COUNT, selectedDhikr.count)
                        }
                        resultLauncher.launch(intent)
                    },
                    onBack = { finish() }
                )
            }
        }
    }

    private fun updateDhikrCompletionStatus(dhikrText: String, completed: Boolean) {
        val index = dhikrListState.indexOfFirst { it.content == dhikrText }
        if (index != -1) {
            dhikrListState[index].isCompleted.value = completed

            // Save to SharedPreferences
            sharedPreferences.edit().putBoolean("dhikr_${dhikrText}", completed).apply()
        }
    }
}

@Composable
fun DhikrScreen(dhikrList: List<Dhikr>, onDhikrClick: (Dhikr) -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8))) {
        TopAppBar(
            title = {
                Text(
                    "Dhikr",
                    modifier = Modifier.fillMaxWidth().padding(end = 48.dp),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn {
            items(dhikrList, key = { it.content }) { dhikr ->
                DhikrItem(dhikr, onDhikrClick)
            }
        }
    }
}

@Composable
fun DhikrItem(dhikr: Dhikr, onDhikrClick: (Dhikr) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onDhikrClick(dhikr) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DhikrHeader(dhikr)
            DhikrDetails(dhikr)
        }
    }
}

@Composable
fun DhikrHeader(dhikr: Dhikr) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.islamic_star),
                contentDescription = "Dhikr Icon",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = dhikr.count,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = dhikr.content,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF388E3C)
        )
    }
}

@Composable
fun DhikrDetails(dhikr: Dhikr) {
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = dhikr.description, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${dhikr.count} Times", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Checkbox(
            checked = dhikr.isCompleted.value,
            onCheckedChange = null, // Disable manual checking
            modifier = Modifier.padding(start = 8.dp),
            enabled = false // Visually indicate it's disabled
        )
    }
}

fun getDhikrList(): List<Dhikr> = listOf(
    Dhikr("تسابيح", "33", "Virtue of saying this dhikr", "سُبْحَانَ اللهِ وَبِحَمْدِهِ"),
    Dhikr("تسابيح", "33", "Forgiveness of sins", "لَا إِلَهَ إِلَّا اللَّهُ")
)

@Preview
@Composable
fun DhikrListPreview() {
    ITC_ONL2_SWD4_S3_1Theme {
        DhikrScreen(dhikrList = remember { mutableStateListOf(Dhikr("تسابيح", "33", "...", "...", mutableStateOf(false))) }, onDhikrClick = {}, onBack = {})
    }
}
