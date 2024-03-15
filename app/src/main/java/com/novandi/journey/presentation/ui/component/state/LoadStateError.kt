package com.novandi.journey.presentation.ui.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.Light

@Composable
fun LoadStateError(errorMessage: String, retry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = errorMessage,
            fontSize = 16.sp,
            color = Dark
        )
        Button(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = { retry() },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Blue40,
                contentColor = Light
            )
        ) {
            Text(text = stringResource(id = R.string.try_again), color = Light)
        }
    }
}