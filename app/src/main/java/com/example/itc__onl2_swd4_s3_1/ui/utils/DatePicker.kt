package com.example.itc__onl2_swd4_s3_1.ui.utils

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.itc__onl2_swd4_s3_1.ui.theme.ITC_ONL2_SWD4_S3_1Theme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun DatePicker(
    onDateSelected: (LocalDate) -> Unit, // Callback for when a date is selected
    initialDate: LocalDate = LocalDate.now(), // Initial date to show in the picker
    allowedDateValidator: (LocalDate) -> Boolean = { true } // Optional validator for allowed dates
) {
    val dateDialogState = rememberMaterialDialogState()

    Button(onClick = { dateDialogState.show() }) {
        Text(text = "Pick date")
    }

    // Date picker dialog
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
                ) {
                // Handle OK button click (optional)
            }
            negativeButton(
                text = "Cancel",
                textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.secondary)
                )
        }
    ) {
        datepicker(
            initialDate = initialDate,
            title = "Pick a date",
            allowedDateValidator = allowedDateValidator,
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onSurface,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                // Customize other colors as needed
            )
        ) { selectedDate ->
            onDateSelected(selectedDate) // Notify the parent about the selected date
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDatePicker() {
    ITC_ONL2_SWD4_S3_1Theme(dynamicColor = false) {
        DatePicker(
            onDateSelected = { /* Handle selected date */ },
            initialDate = LocalDate.now(),
            allowedDateValidator = { true }
        )
    }
}