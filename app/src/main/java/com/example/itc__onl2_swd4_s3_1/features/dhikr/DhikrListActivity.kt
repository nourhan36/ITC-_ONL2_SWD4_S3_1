@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.itc__onl2_swd4_s3_1.features.dhikr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.components.BaseActivity
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.core.utils.Constants
import com.example.itc__onl2_swd4_s3_1.core.utils.ResetDhikrWorker
import com.example.itc__onl2_swd4_s3_1.core.utils.ThemeManager
import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.work.*

class DhikrListActivity : BaseActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val dhikrListState = mutableStateListOf<Dhikr>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sharedPreferences = getSharedPreferences("DhikrPrefs", Context.MODE_PRIVATE)
        scheduleResetWorker()

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val resultDhikrText = result.data?.getStringExtra(DhikrCounterActivity.DHIKR_COMPLETED_TEXT)
                resultDhikrText?.let { updateDhikrCompletionStatus(it, true) }
            }
        }

        setContent {
            val isDark = ThemeManager.isDarkMode

            ITC_ONL2_SWD4_S3_1Theme(darkTheme = isDark.value) {
                LaunchedEffect(Unit) {
                    waitUntilMidnightAndRefresh()
                }

                DhikrScreen(
                    dhikrList = dhikrListState,
                    onDhikrClick = { selectedDhikr ->
                        val intent = Intent(this@DhikrListActivity, DhikrCounterActivity::class.java).apply {
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

    override fun onResume() {
        super.onResume()
        reloadDhikrList()
    }

    private fun reloadDhikrList() {
        val dhikrList = getDhikrList(this).filter { it.category == "تسابيح" }
        dhikrList.forEach { dhikr ->
            val isCompleted = sharedPreferences.getBoolean(dhikr.key, false)
            dhikr.isCompleted.value = isCompleted
        }
        dhikrListState.clear()
        dhikrListState.addAll(dhikrList)
    }

    private suspend fun waitUntilMidnightAndRefresh() {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val delayMillis = target.timeInMillis - now.timeInMillis
        delay(delayMillis)

        reloadDhikrList()
        waitUntilMidnightAndRefresh()
    }

    private fun scheduleResetWorker() {
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(currentTime)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis

        val workRequest = PeriodicWorkRequestBuilder<ResetDhikrWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ResetDhikrWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun updateDhikrCompletionStatus(dhikrText: String, completed: Boolean) {
        val index = dhikrListState.indexOfFirst { it.content == dhikrText }
        if (index != -1) {
            dhikrListState[index].isCompleted.value = completed
            sharedPreferences.edit().putBoolean(dhikrListState[index].key, completed).apply()
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun DhikrScreen(dhikrList: List<Dhikr>, onDhikrClick: (Dhikr) -> Unit, onBack: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorScheme.background)) {

        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.dhikr),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 48.dp),
                    textAlign = TextAlign.Center,
                    color = colorScheme.onBackground
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = colorScheme.onBackground
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorScheme.primaryContainer,
                titleContentColor = colorScheme.onPrimaryContainer
            )
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
    val colorScheme = MaterialTheme.colorScheme

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onDhikrClick(dhikr) },
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant,
            contentColor = colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DhikrHeader(dhikr)
            DhikrDetails(dhikr)
        }
    }
}

@Composable
fun DhikrHeader(dhikr: Dhikr) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.islamic_star),
                contentDescription = stringResource(R.string.streak_image_description),
                tint = colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = dhikr.count,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = dhikr.content,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        )
    }
}

@Composable
fun DhikrDetails(dhikr: Dhikr) {
    val colorScheme = MaterialTheme.colorScheme

    Spacer(modifier = Modifier.height(4.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dhikr.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
        }
        Checkbox(
            checked = dhikr.isCompleted.value,
            onCheckedChange = null,
            enabled = false,
            modifier = Modifier.padding(start = 8.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = colorScheme.primary,
                uncheckedColor = colorScheme.onSurfaceVariant,
                checkmarkColor = colorScheme.onPrimary
            )
        )
    }
}

fun getDhikrList(context: Context): List<Dhikr> = listOf(
    Dhikr("dhikr_1", "تسابيح", "33", context.getString(R.string.dhikr_virtue_subhan), context.getString(R.string.dhikr_1_text)),
    Dhikr("dhikr_2", "تسابيح", "33", context.getString(R.string.dhikr_virtue_alhamdulillah), context.getString(R.string.dhikr_2_text)),
    Dhikr("dhikr_3", "تسابيح", "33", context.getString(R.string.dhikr_virtue_allahuakbar), context.getString(R.string.dhikr_3_text)),
    Dhikr("dhikr_4", "تسابيح", "33", context.getString(R.string.dhikr_virtue_astaghfirullah), context.getString(R.string.dhikr_4_text))
)






@Preview
@Composable
fun DhikrListPreview() {
    ITC_ONL2_SWD4_S3_1Theme {
        DhikrScreen(
        dhikrList = remember { mutableStateListOf(Dhikr("id", "تسابيح", "33", "...", "...", mutableStateOf(false))) },
            onDhikrClick = {},
            onBack = {}
        )
    }
}