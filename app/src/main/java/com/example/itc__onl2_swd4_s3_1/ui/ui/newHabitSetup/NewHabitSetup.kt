@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.itc__onl2_swd4_s3_1.ui.ui.newHabitSetup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itc__onl2_swd4_s3_1.ui.Home.HomeScreen
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.HabitEntity
import com.example.itc__onl2_swd4_s3_1.ui.ui.theme.ITC_ONL2_SWD4_S3_1Theme

class NewHabitSetup : ComponentActivity() {
    enum class CustomDaySelectionType {
        SINGLE, MULTIPLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("title") ?: ""
        val context = this

        setContent {
            ITC_ONL2_SWD4_S3_1Theme(dynamicColor = false) {
                var startCustomDays by remember { mutableStateOf<List<String>>(emptyList()) }
                var repeatCustomDays by remember { mutableStateOf<List<String>>(emptyList()) }

                NewHabitSetupScreen(
                    title = title,
                    viewModel = HabitViewModel(application = application),
                    onHabitSaved = {
                        val intent = Intent(context, HomeScreen::class.java)
                        context.startActivity(intent)
                    },
                    onStartCustomDaySelected = { startCustomDays = it },
                    onRepeatCustomDaysSelected = { repeatCustomDays = it }
                )
            }
        }
    }
}

@Composable
fun NewHabitSetupScreen(
    title: String,
    viewModel: HabitViewModel,
    onHabitSaved: () -> Unit,
    onStartCustomDaySelected: (List<String>) -> Unit,
    onRepeatCustomDaysSelected: (List<String>) -> Unit
) {
    var titleText by remember { mutableStateOf("") }
    var durationValue by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("minutes") }
    val repeatType by remember { mutableStateOf("Every Day") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) //
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                color = colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val dividerColor = colorScheme.onSurface.copy(alpha = 0.2f) // لازم يكون برا الـ Canvas (داخل composable)

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            ) {
                drawLine(
                    color = dividerColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 2f
                )


            }

            HabitTitleInput(value = titleText, onValueChange = { titleText = it })
            DurationSelector(
                durationValue,
                selectedUnit,
                onDurationChange = { durationValue = it },
                onUnitChange = { selectedUnit = it }
            )
            StartTimeSelector(onCustomDaySelected = onStartCustomDaySelected)
            RepeatingSelector(onCustomDaysSelected = onRepeatCustomDaysSelected)
            ReminderText()
        }

        Button(
            onClick = {
                if (titleText.isNotBlank() && durationValue.isNotBlank()) {
                    viewModel.insertHabit(
                        HabitEntity(
                            name = titleText,
                            startTime = "08:00 AM",
                            repeatType = repeatType,
                            duration = durationValue.toInt(),
                            reminderTime = "07:50 AM",
                            isCompleted = false
                        )
                    )
                    onHabitSaved()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
fun StartTimeSelector(onCustomDaySelected: (List<String>) -> Unit) {
    val options = listOf("Today", "Tomorrow", "Custom")
    var showDialog by remember { mutableStateOf(false) }

    SegmentButtonsSelector(
        question = "When would you like to start it?",
        listOptions = options,
        isMultiSelect = false,
        onCustomSelect = { showDialog = true }
    )

    if (showDialog) {
        CustomDaysDialog(
            selectionType = NewHabitSetup.CustomDaySelectionType.SINGLE,
            onDismiss = { showDialog = false },
            onDaysSelected = onCustomDaySelected
        )
    }
}

@Composable
fun RepeatingSelector(onCustomDaysSelected: (List<String>) -> Unit) {
    val options = listOf("Every Day", "Weekly", "Custom")
    var showDialog by remember { mutableStateOf(false) }

    SegmentButtonsSelector(
        question = "How often do you want to do it?",
        listOptions = options,
        isMultiSelect = true,
        onCustomSelect = { showDialog = true }
    )

    if (showDialog) {
        CustomDaysDialog(
            selectionType = NewHabitSetup.CustomDaySelectionType.MULTIPLE,
            onDismiss = { showDialog = false },
            onDaysSelected = onCustomDaysSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentButtonsSelector(
    question: String,
    listOptions: List<String>,
    isMultiSelect: Boolean,
    onCustomSelect: ((NewHabitSetup.CustomDaySelectionType) -> Unit)? = null
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column {
        Text(
            text = question,
            color = colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        )
        Row(modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                listOptions.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                            if (option == "Custom") {
                                onCustomSelect?.invoke(
                                    if (isMultiSelect) NewHabitSetup.CustomDaySelectionType.MULTIPLE
                                    else NewHabitSetup.CustomDaySelectionType.SINGLE
                                )
                            }
                        },
                        shape = SegmentedButtonDefaults.itemShape(index, listOptions.size),
                        modifier = Modifier.weight(1f),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = colorScheme.primaryContainer,
                            activeContentColor = colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(option)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDaysDialog(
    selectionType: NewHabitSetup.CustomDaySelectionType,
    onDismiss: () -> Unit,
    onDaysSelected: (List<String>) -> Unit
) {
    val daysOfWeek = listOf("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    val selectedDays = remember { mutableStateListOf<String>() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Day${if (selectionType == NewHabitSetup.CustomDaySelectionType.MULTIPLE) "s" else ""}") },
        text = {
            Column {
                daysOfWeek.forEach { day ->
                    val isSelected = selectedDays.contains(day)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectionType == NewHabitSetup.CustomDaySelectionType.SINGLE) {
                                    selectedDays.clear()
                                    selectedDays.add(day)
                                } else {
                                    if (isSelected) selectedDays.remove(day)
                                    else selectedDays.add(day)
                                }
                            }
                            .padding(8.dp)
                    ) {
// In CustomDaysDialog
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = colorScheme.primary,
                                checkmarkColor = colorScheme.onPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(day)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onDaysSelected(selectedDays.toList())
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DurationSelector(
    durationValue: String,
    selectedUnit: String,
    onDurationChange: (String) -> Unit,
    onUnitChange: (String) -> Unit
) {
    val timeUnits = listOf("minutes", "hours")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Duration",
            color = colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.width(32.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .border(
                    1.dp,
                    colorScheme.onSurface.copy(alpha = 0.5f),
                    RoundedCornerShape(8.dp)
                )
                .background(colorScheme.surface)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = durationValue,
                onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 2) onDurationChange(it) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(color = colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.width(2.dp))
            TimeUnitDropdown(
                items = timeUnits,
                selectedItem = selectedUnit,
                onItemSelected = onUnitChange
            )
        }
    }
}

@Composable
fun TimeUnitDropdown(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.wrapContentSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedItem,
                color = colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Icon",
                tint = colorScheme.onSurface
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colorScheme.surface)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            color = colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun HabitTitleInput(value: String, onValueChange: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Title",
            color = colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.width(32.dp))
        TextField(
            value = value,
            onValueChange = { if (it.length <= 20) onValueChange(it) },
            placeholder = { Text("Enter title") },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .border(
                    1.dp,
                    colorScheme.onSurface.copy(alpha = 0.5f),
                    RoundedCornerShape(8.dp)
                ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(color = colorScheme.onSurface)
        )
    }
}

@Composable
fun ReminderText() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Reminder Icon",
                tint = colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Set reminder so you don't forget to do it",
                color = colorScheme.onSurface, // Updated from primary
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = "Create Reminder",
            color = colorScheme.onSurface, // Updated from primary
            modifier = Modifier.clickable { }
        )
    }
}
