package com.example.itc__onl2_swd4_s3_1.ui.ui.ManageSalah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.SalahEntity
import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SalahTrackerScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                SalahTracker()
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalahTracker() {
    val context = LocalContext.current
    val db = remember { SalahDatabase.getDatabase(context) }
    val dao = db.salahDao()
    val scope = rememberCoroutineScope()

    val colorScheme = MaterialTheme.colorScheme
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val selectedDateStr = selectedDate.toString()
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
    var selectedPrayers by remember { mutableStateOf(setOf<String>()) }

    // Load saved salahs for the selected date
    LaunchedEffect(selectedDateStr) {
        val entry = dao.getPrayersForDate(selectedDateStr)
        selectedPrayers = entry?.prayers?.split(",")?.toSet() ?: emptySet()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SalahTrackerHeader()

        Text(
            text = "Which Salah did you pray today?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground
        )
        Text(
            text = selectedDate.format(dateFormatter),
            fontSize = 14.sp,
            color = colorScheme.onBackground.copy(alpha = 0.6f)
        )

        prayers.forEach { prayer ->
            SalahCheckBox(
                prayer = prayer,
                selectedPrayers = selectedPrayers,
                containerColor = colorScheme.surface,
                textColor = colorScheme.onSurface
            ) { checked, name ->
                val updated = selectedPrayers.toMutableSet().apply {
                    if (checked) add(name) else remove(name)
                }
                selectedPrayers = updated

                // Save update to Room
                scope.launch(Dispatchers.IO) {
                    dao.insertOrUpdate(
                        SalahEntity(
                            date = selectedDateStr,
                            prayers = updated.joinToString(",")
                        )
                    )
                }
            }
        }

        CalendarGrid(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            selectedColor = colorScheme.secondary,
            defaultColor = colorScheme.surface,
            textColor = colorScheme.onSurface,
            selectedTextColor = colorScheme.onSecondary
        )
    }
}

@Composable
fun SalahCheckBox(
    prayer: String,
    selectedPrayers: Set<String>,
    containerColor: Color,
    textColor: Color,
    onCheckedChange: (Boolean, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            text = "Salah Tracker",
            color = colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CalendarGrid(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    selectedColor: Color,
    defaultColor: Color,
    textColor: Color,
    selectedTextColor: Color
) {
    val daysInMonth = selectedDate.withDayOfMonth(1).lengthOfMonth()
    val days = (1..daysInMonth).map { selectedDate.withDayOfMonth(it) }
    val dialogState = rememberMaterialDialogState()

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { dialogState.show() }) {
                Text(text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
            }
        }

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton(text = "OK")
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(initialDate = selectedDate, title = "Pick a date") {
                onDateSelected(it)
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
            items(days.size) { index ->
                val date = days[index]
                val isSelected = date == selectedDate
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            color = if (isSelected) selectedColor else defaultColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onDateSelected(date) }
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isSelected) selectedTextColor else textColor,
                        fontWeight = FontWeight.Bold
                    )
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