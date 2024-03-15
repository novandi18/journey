package com.novandi.journey.presentation.ui.component.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.field.JPasswordField
import com.novandi.journey.presentation.ui.component.field.JTextField
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.Light

@Composable
fun AuthLoginForm(
    email: String,
    password: String,
    setEmail: (String) -> Unit,
    setPassword: (String) -> Unit,
    doLogin: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        JTextField(
            leadingIcon = Icons.Filled.Email,
            label = stringResource(id = R.string.email),
            keyboardType = KeyboardType.Email,
            placeholder = stringResource(id = R.string.email_placeholder),
            onKeyUp = setEmail,
            textValue = email
        )
        JPasswordField(
            onKeyUp = setPassword,
            textValue = password
        )
        Column(
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = {
                    doLogin()
                },
                enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Blue40,
                    contentColor = Light,
                    disabledContainerColor = Blue80,
                    disabledContentColor = Light
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedVisibility(visible = isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = Light
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.btn_login),
                        color = Light,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}