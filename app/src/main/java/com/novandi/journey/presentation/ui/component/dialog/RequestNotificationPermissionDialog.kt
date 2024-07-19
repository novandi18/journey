package com.novandi.journey.presentation.ui.component.dialog

import android.Manifest
import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.novandi.journey.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        var showPermissionDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (!permissionState.status.isGranted) {
                if (permissionState.status.shouldShowRationale) {
                    showPermissionDialog = true
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
        }

        when {
            showPermissionDialog -> {
                JDialog(
                    onDismissRequest = {},
                    onConfirmation = {
                        showPermissionDialog = false
                        permissionState.launchPermissionRequest()
                    },
                    dialogTitle = stringResource(id = R.string.notification_permission_title),
                    dialogText = stringResource(id = R.string.notification_permission_desc),
                    confirmText = stringResource(id = R.string.ok),
                    icon = Icons.Rounded.Notifications,
                    cancelShowing = false
                )
            }
        }
    }
}
