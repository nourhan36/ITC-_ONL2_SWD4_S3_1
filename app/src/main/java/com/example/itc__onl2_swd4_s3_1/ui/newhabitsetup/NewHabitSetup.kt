package com.example.itc__onl2_swd4_s3_1.ui.newhabitsetup

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.ui.home.HomeScreen
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.HabitEntity
import com.example.itc__onl2_swd4_s3_1.ui.ui.newHabitSetup.HabitViewModel
import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewHabitSetup : ComponentActivity() {
    private val viewModel: HabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val habitId = intent.getIntExtra("habitId", -1)
        val name = intent.getStringExtra("name") ?: ""
        val duration = intent.getIntExtra("duration", 0)
        val repeatType = intent.getStringExtra("repeatType") ?: ""
        val reminderTime = intent.getStringExtra("reminderTime") ?: ""
        val startTime = intent.getStringExtra("startTime") ?: ""
        val startDate = intent.getStringExtra("startDate") ?: ""

        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewHabitSetupScreen(
                        title = if (habitId != -1) "Edit Habit" else "New Habit",
                        viewModel = viewModel,
                        onHabitSaved = {
                            startActivity(Intent(this, HomeScreen::class.java))
                            finish()
                        },
                        existingHabit = if (habitId != -1) {
                            HabitEntity(
                                id = habitId,
                                name = name,
                                duration = duration,
                                repeatType = repeatType,
                                reminderTime = reminderTime,
                                startTime = startTime,
                                startDate = startDate,
                                isCompleted = false // optional
                            )
                        } else null
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHabitSetupScreen(
    title: String,
    viewModel: HabitViewModel,
    onHabitSaved: () -> Unit,
    existingHabit: HabitEntity? = null
) {
    var titleText by remember { mutableStateOf("") }
    var durationValue by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("minutes") }
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // Title Input
        OutlinedTextField(
            value = titleText,
            onValueChange = { titleText = it },
            label = { Text("Habit Title") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // Duration Input Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = durationValue,
                onValueChange = { if (it.all { c -> c.isDigit() }) durationValue = it },
                label = { Text("Duration") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            UnitDropdown(
                selectedUnit = selectedUnit,
                onUnitSelected = { selectedUnit = it }
            )
        }

        // Date Selector
        DateSelector(
            selectedDate = startDate,
            onDateSelected = { startDate = it },
            showDatePicker = { showDatePicker = true }
        )

        // Save Button
        Button(
            onClick = {
                if (validateInput(titleText, durationValue)) {
                    val habit = HabitEntity(
                        id = existingHabit?.id ?: 0,
                        name = titleText,
                        duration = durationValue.toInt(),
                        startDate = startDate.toString(),
                        isCompleted = existingHabit?.isCompleted ?: false,
                        repeatType = existingHabit?.repeatType ?: "Daily",
                        startTime = existingHabit?.startTime ?: "08:00 AM",
                        reminderTime = existingHabit?.reminderTime ?: "07:50 AM"
                    )

                    if (existingHabit != null) {
                        viewModel.updateHabit(habit)
                    } else {
                        viewModel.insertHabit(habit)
                    }

                    onHabitSaved()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(if (existingHabit != null) "Update Habit" else "Save Habit", fontSize = 16.sp)

        }

        if (showDatePicker) {
            val datePicker = DatePickerDialog(
                context,
                { _, year, month, day ->
                    startDate = LocalDate.of(year, month + 1, day)
                    showDatePicker = false
                },
                startDate.year,
                startDate.monthValue - 1,
                startDate.dayOfMonth
            )
            datePicker.show()
        }
    }
}

@Composable
private fun UnitDropdown(
    selectedUnit: String,
    onUnitSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val units = listOf("minutes", "hours")

    Surface(
        modifier = Modifier
            .width(120.dp)
            .clickable { expanded = true },
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selectedUnit)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select unit"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            onUnitSelected(unit)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    showDatePicker: () -> Unit
) {
    val options = listOf("Today", "Tomorrow", "Custom")
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true },
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select date"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            when (option) {
                                "Today" -> onDateSelected(LocalDate.now())
                                "Tomorrow" -> onDateSelected(LocalDate.now().plusDays(1))
                                "Custom" -> showDatePicker()
                            }
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

private fun validateInput(title: String, duration: String): Boolean {
    return title.isNotBlank() && duration.isNotBlank() && duration.all { it.isDigit() }
}