package com.example.itc__onl2_swd4_s3_1.ui.SalahTrackerScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SalahTrackerScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SalahTracker()
        }
    }
}

@Composable
fun SalahTracker() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val todayFormatted = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    var selectedPrayersMap by remember { mutableStateOf(mutableMapOf<LocalDate, Set<String>>()) }
    val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
    val selectedPrayers = selectedPrayersMap[selectedDate] ?: emptySet()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Salah Tracker",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
            )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Which Salah Did you offer Today?", fontSize = 16.sp)
        Text(todayFormatted, fontSize = 14.sp, color = Color.Gray)


        Spacer(modifier = Modifier.height(16.dp))
        prayers.forEach { prayer ->
            SalahCheckBox(prayer, selectedPrayers) { checked, name ->
                selectedPrayersMap = selectedPrayersMap.toMutableMap().apply {
                    put(selectedDate, if (checked) selectedPrayers + name else selectedPrayers - name)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        CalendarGrid(
            selectedDate = selectedDate,
            onDateSelected = { newDate ->
                selectedDate = newDate
            }
        )
    }
}


@Composable
fun SalahCheckBox(prayer: String, selectedPrayers: Set<String>, onCheckedChange: (Boolean, String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!selectedPrayers.contains(prayer), prayer) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(prayer, fontSize = 18.sp, modifier = Modifier.weight(1f))
        Checkbox(
            checked = selectedPrayers.contains(prayer),
            onCheckedChange = { onCheckedChange(it, prayer) }
        )
    }
}

@Composable
fun CalendarGrid(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val startOfMonth = selectedDate.withDayOfMonth(1)
    val daysInMonth = startOfMonth.lengthOfMonth()
    val days = (1..daysInMonth).map { startOfMonth.withDayOfMonth(it) }
    var pickedDate by remember { mutableStateOf(selectedDate) }
    val dateDialogState = rememberMaterialDialogState()


    Column(modifier = Modifier.fillMaxWidth()) {
        // Date Picker Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {  dateDialogState.show()}) {
                Text(text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
            }
        }
        // Date Picker Dialog
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    onDateSelected(pickedDate) // Update the selected date when "Ok" is clicked
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = selectedDate, // Use the selectedDate as the initial date
                title = "Pick a date",
                allowedDateValidator = { it.dayOfMonth % 2 == 1 }
            ) { pickedDate = it }
        }
    }


    // Calendar Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(days.size) { index ->
                val date = days[index]
                val isSelected = date == selectedDate
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            if (isSelected) Color.Blue else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onDateSelected(date) }
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
}
@Preview(showBackground = true)
@Composable
fun SalahTrackerScreenPreview() {
    SalahTracker()
}