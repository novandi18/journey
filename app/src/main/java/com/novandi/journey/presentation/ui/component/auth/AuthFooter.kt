package com.novandi.journey.presentation.ui.component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark

@Composable
fun AuthFooter(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
            Text(
                text = stringResource(id = R.string.login_ask),
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth(),
                fontSize = 14.sp,
                color = Dark.copy(alpha = 0.6f)
            )
            TextButton(
                onClick = { navigateToRegister() },
                modifier = modifier.align(alignment = Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.register_in_login),
                    color = Blue40
                )
            }
        }
    }
}