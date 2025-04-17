@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.itc__onl2_swd4_s3_1.ui.ui.dhikr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                DhikrScreen(
                    onDhikrClick = { selectedDhikr ->
                        startActivity(Intent(this, DhikrCounterActivity::class.java).apply {
                            putExtra(Constants.DHIKR_TEXT, selectedDhikr.content)
                            putExtra(Constants.DHIKR_COUNT, selectedDhikr.count)
                        })
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}

@Composable
fun DhikrScreen(onDhikrClick: (Dhikr) -> Unit, onBack: () -> Unit) {
    val dhikrList = getDhikrList().filter { it.category == "تسابيح" }

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
            items(dhikrList) { dhikr ->
                DhikrItem(dhikr, onDhikrClick)
            }
        }
    }
}

@Composable
fun DhikrItem(dhikr: Dhikr, onDhikrClick: (Dhikr) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { onDhikrClick(dhikr) }
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
    Text(text = dhikr.translation, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Text(text = dhikr.description, fontSize = 14.sp, color = Color.Gray)
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = "${dhikr.count} Times", fontSize = 14.sp, fontWeight = FontWeight.Bold)
}

fun getDhikrList(): List<Dhikr> = listOf(
    Dhikr("تسابيح", "33", "Virtue of saying this dhikr", "", "سُبْحَانَ اللهِ وَبِحَمْدِهِ", "x"),
    Dhikr("تسابيح", "33", "Forgiveness of sins", "", "لَا إِلَهَ إِلَّا اللَّهُ", "<")
)

@Preview
@Composable
fun DhikrListPreview() {
    ITC_ONL2_SWD4_S3_1Theme {
     DhikrScreen(onDhikrClick = {}, onBack = {})
    }
}