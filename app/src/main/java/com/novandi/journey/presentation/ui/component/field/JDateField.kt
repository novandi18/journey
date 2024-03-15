package com.novandi.journey.presentation.ui.component.field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.Light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JDateField(
    openDialog: () -> Unit,
    value: String,
    isEnabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        interactionSource = interactionSource,
        textStyle = TextStyle(textAlign = TextAlign.Start),
        readOnly = true
    ) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = { it() },
            enabled = isEnabled,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.deadline_time_placeholder)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Timer,
                    contentDescription = stringResource(id = R.string.deadline_time_placeholder)
                )
            },
            trailingIcon = {
                IconButton(onClick = { openDialog() }) {
                    Icon(
                        imageVector = Icons.Filled.EditCalendar,
                        contentDescription = stringResource(id = R.string.deadline_time_placeholder),
                        tint = Dark
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Dark,
                disabledLeadingIconColor = Dark
            ),
            contentPadding = OutlinedTextFieldDefaults.contentPadding(),
            container = {
                OutlinedTextFieldDefaults.ContainerBox(
                    enabled = isEnabled,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blue40,
                        focusedContainerColor = Light,
                        focusedTextColor = Dark,
                        cursorColor = Dark,
                        unfocusedContainerColor = Light
                    )
                )
            },
        )
    }
}