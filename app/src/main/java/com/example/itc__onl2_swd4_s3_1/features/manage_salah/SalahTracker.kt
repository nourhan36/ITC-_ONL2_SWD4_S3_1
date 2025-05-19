package com.example.itc__onl2_swd4_s3_1.features.manage_salah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.features.manage_salah.presentation.SalahViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.res.stringResource
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.components.BaseActivity

@AndroidEntryPoint
class SalahTrackerScreen : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                SalahTracker()
            }
        }
    }
}

@Composable
fun SalahTracker(viewModel: SalahViewModel = hiltViewModel()) {
    val colorScheme = MaterialTheme.colorScheme
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val selectedDateStr = selectedDate.toString()
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    val prayers = listOf(
        stringResource(R.string.fajr),
        stringResource(R.string.dhuhr),
        stringResource(R.string.asr),
        stringResource(R.string.maghrib),
        stringResource(R.string.isha)
    )
    val selectedPrayers by viewModel.selectedPrayers.collectAsState()
    val completedDates by viewModel.completedDates.collectAsState()
    val incompleteDates by viewModel.incompleteDates.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadInitialData(prayers)
    }

    LaunchedEffect(selectedDateStr) {
        viewModel.loadPrayersForDate(selectedDateStr)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SalahTrackerHeader()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.which_salah_did_you_offer),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            Text(
                text = selectedDate.format(dateFormatter),
                fontSize = 14.sp,
                color = colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(prayers.size) { index ->
                val prayer = prayers[index]
                SalahCheckBox(
                    prayer = prayer,
                    selectedPrayers = selectedPrayers,
                    containerColor = colorScheme.surface,
                    textColor = colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                ) { _, name ->
                    viewModel.togglePrayer(name, prayers)
                }
            }
        }


        CalendarGrid(
            selectedDate = selectedDate,
            completedDates = completedDates,
            incompleteDates = incompleteDates,
            onDateSelected = { selectedDate = it },
            selectedColor = colorScheme.secondary,
            completedColor = colorScheme.primary,
            currentDayColor = Color.Red,
            defaultColor = colorScheme.surface,
            textColor = colorScheme.onSurface,
            selectedTextColor = colorScheme.onSecondary,
            completedTextColor = colorScheme.onPrimary,
            currentDayTextColor = Color.White,
            modifier = Modifier.weight(0.6f)
        )
    }
}


@Composable
fun SalahCheckBox(
    prayer: String,
    selectedPrayers: Set<String>,
    containerColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean, String) -> Unit
) {
    Row(
        modifier = modifier
            .background(containerColor, shape = RoundedCornerShape(12.dp))
            .clickable { onCheckedChange(!selectedPrayers.contains(prayer), prayer) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prayer,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f),
            color = textColor
        )
        Checkbox(
            checked = selectedPrayers.contains(prayer),
            onCheckedChange = { onCheckedChange(it, prayer) }
        )
    }
}

@Composable
fun SalahTrackerHeader() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.primary, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.salah_tracker),
            color = colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CalendarGrid(
    selectedDate: LocalDate,
    completedDates: List<LocalDate>,
    incompleteDates: List<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    selectedColor: Color,
    completedColor: Color,
    currentDayColor: Color,
    defaultColor: Color,
    textColor: Color,
    selectedTextColor: Color,
    completedTextColor: Color,
    currentDayTextColor: Color,
    modifier: Modifier = Modifier
) {
    val daysInMonth = selectedDate.withDayOfMonth(1).lengthOfMonth()
    val days = (1..daysInMonth).map { selectedDate.withDayOfMonth(it) }
    val dialogState = rememberMaterialDialogState()
    val today = LocalDate.now()

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { dialogState.show() },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
            }
        }

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton(text = stringResource(R.string.complete))
                negativeButton(text = stringResource(R.string.incomplete))

            }
        ) {
            datepicker(initialDate = selectedDate, title = stringResource(R.string.habit_date_label)) {
                onDateSelected(it)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(days.size) { index ->
                val date = days[index]
                val isSelected = date == selectedDate
                val isCompleted = completedDates.contains(date)
                val isIncomplete = incompleteDates.contains(date)
                val isCurrentDay = date == today

                val bgColor = when {
                    isCurrentDay -> currentDayColor
                    isSelected -> selectedColor
                    isCompleted -> completedColor
                    else -> defaultColor
                }

                val textCol = when {
                    isCurrentDay -> currentDayTextColor
                    isSelected -> selectedTextColor
                    isCompleted -> completedTextColor
                    else -> textColor
                }

                Column(
                    modifier = Modifier
                        .padding(2.dp)
                        .background(bgColor, shape = RoundedCornerShape(4.dp))
                        .clickable { onDateSelected(date) }
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = textCol,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    if (isIncomplete) {
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(6.dp)
                                .background(Color(0xFFFFA726), shape = CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SalahTrackerPreview() {
    ITC_ONL2_SWD4_S3_1Theme(dynamicColor = false) {
        SalahTracker()
    }
}
