package com.novandi.journey.presentation.ui.component.field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.Light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JPasswordField(
    label: String = stringResource(id = R.string.password),
    placeholder: String = stringResource(id = R.string.password_placeholder),
    onKeyUp: (String) -> Unit,
    textValue: String,
    isReadOnly: Boolean = false
) {
    var isVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = textValue,
        onValueChange = { newText -> onKeyUp(newText) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        singleLine = true,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        readOnly = isReadOnly
    ) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = textValue,
            innerTextField = { it() },
            enabled = true,
            singleLine = true,
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            interactionSource = interactionSource,
            placeholder = {
                Text(text = placeholder)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Password,
                    contentDescription = stringResource(id = R.string.password)
                )
            },
            trailingIcon = {
                if (!isReadOnly) {
                    IconButton(onClick = {
                        isVisible = !isVisible
                    }) {
                        Icon(
                            imageVector = if (isVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                }
            },
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
            },
            label = {
                Text(text = label)
            }
        )
    }
}