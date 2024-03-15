package com.novandi.journey.presentation.ui.component.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.utility.data.ConvertUtil
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JDatePickerField(
    setDate: (String) -> Unit,
    value: String,
    isEnabled: Boolean = true
) {
    val scope = rememberCoroutineScope()
    var openDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

    if (value.isNotEmpty() && datePickerState.selectedDateMillis == null)
        datePickerState.selectedDateMillis = ConvertUtil.convertDateToMillis(value)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = stringResource(id = R.string.deadline_time_placeholder))
        JDateField(
            openDialog = {
                openDialog = true
            },
            value = if (value.isNotEmpty()) ConvertUtil.convertDateTimeToDate(value) else value,
            isEnabled = isEnabled
        )
    }

    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        scope.launch {
                            setDate(
                                ConvertUtil.convertMillisToDate(datePickerState.selectedDateMillis!!)
                            )
                        }
                    },
                    enabled = confirmEnabled
                ) {
                    Text(stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Light,
                selectedDayContainerColor = Blue40,
                selectedDayContentColor = Light,
                dividerColor = DarkGray40,
                dayContentColor = Dark
            )
        ) {
            DatePicker(state = datePickerState)
        }
    }
}