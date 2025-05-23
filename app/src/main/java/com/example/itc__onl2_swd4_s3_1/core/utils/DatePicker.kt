package com.example.itc__onl2_swd4_s3_1.core.utils

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.itc__onl2_swd4_s3_1.core.theme.ITC_ONL2_SWD4_S3_1Theme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun DatePicker(
    onDateSelected: (LocalDate) -> Unit,
    initialDate: LocalDate = LocalDate.now(),
    allowedDateValidator: (LocalDate) -> Boolean = { true }
) {
    val dateDialogState = rememberMaterialDialogState()

    Button(onClick = { dateDialogState.show() }) {
        Text(text = "Pick date")
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
            ) {
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
            )
        ) { selectedDate ->
            onDateSelected(selectedDate)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDatePicker() {
    ITC_ONL2_SWD4_S3_1Theme(dynamicColor = false) {
        DatePicker(
            onDateSelected = {},
            initialDate = LocalDate.now(),
            allowedDateValidator = { true }
        )
    }
}


