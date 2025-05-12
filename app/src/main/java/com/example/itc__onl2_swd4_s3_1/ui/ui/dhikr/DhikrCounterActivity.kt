@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.itc__onl2_swd4_s3_1.ui.ui.dhikr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.ui.ui.utils.Constants
import kotlinx.coroutines.launch

class DhikrCounterActivity : ComponentActivity() {

    companion object {
        const val DHIKR_COMPLETED_TEXT = "dhikr_completed_text"
        const val DHIKR_COMPLETED = "dhikr_completed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dhikrText = intent.getStringExtra(Constants.DHIKR_TEXT) ?: getDhikrList().firstOrNull()?.content ?: "سبحان الله"
        val dhikrCount = intent.getStringExtra(Constants.DHIKR_COUNT)?.toIntOrNull() ?: getDhikrList().firstOrNull()?.count?.toIntOrNull() ?: 33

        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                DhikrCounter(dhikrText, dhikrCount, onDhikrCompleted = { completedDhikrText ->
                    val intent = Intent(this, DhikrListActivity::class.java).apply {
                        putExtra(DHIKR_COMPLETED_TEXT, completedDhikrText)
                        putExtra(DHIKR_COMPLETED, true)
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    finish()
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhikrCounter(dhikrText: String, total: Int, onDhikrCompleted: (String) -> Unit) {
    var count by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = { Text("Dhikr") },
            actions = {
                IconButton(onClick = { count = 0 }) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Reset Counter")
                }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "$count/$total", fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Text(text = "Total: $count", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(32.dp))

        DhikrCard(dhikrText, total, context)
        Spacer(modifier = Modifier.weight(1f))

        AnimatedCounterButton {
            if (count < total) {
                count++
                if (count == total) {
                    onDhikrCompleted(dhikrText)
                }
            }
        }
        Text(
            text = "Tap to count your daily Dhikr",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DhikrCard(dhikrText: String, total: Int, context: android.content.Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                context.startActivity(Intent(context, DhikrListActivity::class.java))
            },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dhikrText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "$total Times : May Allah be Praised",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AnimatedCounterButton(onCountIncrease: () -> Unit) {
    val scope = rememberCoroutineScope()
    val animationProgress = remember { Animatable(0f) }
    val animatedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

    fun triggerAnimation() {
        scope.launch {
            animationProgress.snapTo(0f)
            animationProgress.animateTo(1f, animationSpec = tween(400))
            animationProgress.animateTo(0f, animationSpec = tween(400))
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(180.dp)) {
            val animatedRadius = (size.minDimension / 2) * animationProgress.value
            drawCircle(color = animatedColor, radius = animatedRadius)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onCountIncrease()
                    triggerAnimation()
                }
        ) {
            Text(text = "Count", color = MaterialTheme.colorScheme.onPrimary, fontSize = 24.sp)
        }
    }
}

@Preview
@Composable
fun DhikrCounterPreview() {
    ITC_ONL2_SWD4_S3_1Theme {
        DhikrCounter("سبحان الله", 33, onDhikrCompleted = {})
    }
}
