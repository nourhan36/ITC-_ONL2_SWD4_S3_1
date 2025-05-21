package com.example.itc__onl2_swd4_s3_1.features.new_habit_setup

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.R
import com.example.itc__onl2_swd4_s3_1.core.components.BaseActivity
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.example.itc__onl2_swd4_s3_1.data.entity.HabitEntity
import com.example.itc__onl2_swd4_s3_1.features.home.HomeScreen
import com.example.itc__onl2_swd4_s3_1.features.new_habit_setup.presentation.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class NewHabitSetup : BaseActivity() {

    private val viewModel: HabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val habitId = intent.getIntExtra("habitId", -1)
        val habitTitleFromSelector = if (habitId != -1) {
            intent.getStringExtra("type") ?: ""
        } else {
            intent.getStringExtra("title") ?: ""
        }

        val habit = if (habitId != -1) HabitEntity(
            id = habitId,
            type = habitTitleFromSelector,
            name = intent.getStringExtra("name") ?: "",
            duration = intent.getIntExtra("duration", 0),
            repeatType = intent.getStringExtra("repeatType") ?: "",
            reminderTime = intent.getStringExtra("reminderTime") ?: "",
            startTime = intent.getStringExtra("startTime") ?: "",
            startDate = intent.getStringExtra("startDate") ?: "",
            durationUnit= intent.getStringExtra("durationUnit") ?: "",
            isCompleted = false
        ) else null

        setContent {
            ITC_ONL2_SWD4_S3_1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewHabitSetupScreen(
                        title = habitTitleFromSelector,
                        viewModel = viewModel,
                        onHabitSaved = {
                            startActivity(Intent(this, HomeScreen::class.java))
                            finish()
                        },
                        existingHabit = habit
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
    var titleText by remember { mutableStateOf(existingHabit?.name ?: "") }
    var durationValue by remember { mutableStateOf(existingHabit?.duration?.toString() ?: "") }
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

        OutlinedTextField(
            value = titleText,
            onValueChange = { titleText = it },
            label = { Text(stringResource(R.string.habit_title)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = durationValue,
                onValueChange = { if (it.all(Char::isDigit)) durationValue = it },
                label = { Text(stringResource(R.string.duration_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            UnitDropdown(selectedUnit = selectedUnit, onUnitSelected = { selectedUnit = it })
        }

        DateSelector(
            selectedDate = startDate,
            onDateSelected = { startDate = it },
            showDatePicker = { showDatePicker = true }
        )

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
                        reminderTime = existingHabit?.reminderTime ?: "07:50 AM",
                        durationUnit = selectedUnit,
                        type = existingHabit?.type ?: title
                    )

                    if (existingHabit != null) viewModel.updateHabit(habit)
                    else viewModel.insertHabit(habit)

                    onHabitSaved()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                if (existingHabit != null) stringResource(R.string.update_habit)
                else stringResource(R.string.save_habit),
                fontSize = 16.sp
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    startDate = LocalDate.of(year, month + 1, day)
                    showDatePicker = false
                },
                startDate.year,
                startDate.monthValue - 1,
                startDate.dayOfMonth
            ).show()
        }
    }
}

@Composable
private fun UnitDropdown(selectedUnit: String, onUnitSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val units = listOf(
        stringResource(R.string.minutes),
        stringResource(R.string.hours)
    )

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
                Icon(Icons.Default.ArrowDropDown, contentDescription = stringResource(R.string.habit_duration_unit_label))
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
                Icon(Icons.Default.ArrowDropDown, contentDescription = stringResource(R.string.habit_date_label))
            }

            val options = listOf(
                "today" to stringResource(R.string.today),
                "tomorrow" to stringResource(R.string.tomorrow),
                "custom" to stringResource(R.string.custom)
            )

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { (key, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            when (key) {
                                "today" -> onDateSelected(LocalDate.now())
                                "tomorrow" -> onDateSelected(LocalDate.now().plusDays(1))
                                "custom" -> showDatePicker()
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
