package com.novandi.journey.presentation.ui.component.field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.Light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JTextAreaField(
    title: String,
    onKeyUp: (String) -> Unit,
    value: String,
    isReadOnly: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = title)

        BasicTextField(
            value = value,
            onValueChange = { newText -> onKeyUp(newText) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            minLines = 7,
            maxLines = 7,
            readOnly = isReadOnly
        ) {
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = { it() },
                enabled = !isReadOnly,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                colors = OutlinedTextFieldDefaults.colors(),
                contentPadding = OutlinedTextFieldDefaults.contentPadding(),
                container = {
                    OutlinedTextFieldDefaults.ContainerBox(
                        enabled = true,
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
                }
            )
        }
    }
}