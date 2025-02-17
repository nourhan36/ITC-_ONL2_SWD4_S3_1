package com.example.itc__onl2_swd4_s3_1.ui.dhikr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.ui.theme.ITC_ONL2_SWD4_S3_1Theme

class DhikrListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                DhikrScreen { selectedDhikr ->
                    val intent = Intent(this, DhikrCounterActivity::class.java)
                    intent.putExtra("dhikr_text", selectedDhikr.content)
                    intent.putExtra("dhikr_count", selectedDhikr.count)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun DhikrScreen(onDhikrClick: (Dhikr) -> Unit) {
    val dhikrList = getDhikrList().filter { it.category == "تسابيح" }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8)).padding(16.dp)
    ) {
        Text(
            text = "Dhikr List",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn {
            items(dhikrList) { dhikr ->
                DhikrItem(dhikr, onDhikrClick)
            }
        }
    }
}

@Composable
fun DhikrItem(dhikr: Dhikr, onClick: (Dhikr) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(dhikr) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = dhikr.content, fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.End)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Count: ${dhikr.count}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

// Dummy function to simulate API response
fun getDhikrList(): List<Dhikr> {
    return listOf(
        Dhikr("تسابيح", "100", "Virtue of saying this dhikr", "", "سُبْحَانَ اللهِ وَبِحَمْدِهِ"),
        Dhikr("تسابيح", "100", "Forgiveness of sins", "", "لَا إِلَهَ إِلَّا اللَّهُ"),
        Dhikr("أذكار الصباح", "33", "Morning dhikr", "", "اللّهُـمَّ أَنْتَ رَبِّـي"),
    )
}


@Preview
@Composable
fun DhikrListPreview() {
    ITC_ONL2_SWD4_S3_1Theme {
        DhikrScreen { }
    }
}