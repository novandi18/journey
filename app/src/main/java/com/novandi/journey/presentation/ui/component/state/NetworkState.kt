package com.novandi.journey.presentation.ui.component.state

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.CloudQueue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.journey.R
import com.novandi.journey.presentation.common.connectivityState
import com.novandi.journey.presentation.ui.theme.Green40
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.utility.network.ConnectionState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NetworkState() {
    var showing by rememberSaveable { mutableStateOf(false) }
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    if (isConnected) {
        Handler(Looper.getMainLooper()).postDelayed({
            showing = false
        }, 3000)
    } else showing = true

    AnimatedVisibility(
        visible = showing,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isConnected) Green40 else Red
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = if (isConnected) Icons.Rounded.CloudQueue else
                        Icons.Rounded.CloudOff,
                    contentDescription = null,
                    tint = Light
                )
                Text(
                    text = stringResource(
                        id = if (isConnected) R.string.connected else R.string.disconnected
                    ),
                    fontSize = 14.sp,
                    color = Light
                )
            }
        }
    }
}