package com.novandi.journey.presentation.ui.component.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light

@Composable
fun NetworkError(
    retry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(id = R.drawable.network_error),
                contentDescription = stringResource(id = R.string.network_error)
            )
            Text(
                text = stringResource(id = R.string.network_error),
                color = Dark,
                fontSize = 20.sp
            )
            ElevatedButton(
                onClick = { retry() },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Blue40,
                    contentColor = Light
                )
            ) {
                Text(
                    text = stringResource(id = R.string.try_again)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        NetworkError({})
    }
}