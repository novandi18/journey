package com.novandi.journey.presentation.screen

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.UpdatePasswordRequest
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.dialog.JDialog
import com.novandi.journey.presentation.ui.component.field.JPasswordField
import com.novandi.journey.presentation.ui.component.field.JTextField
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobSeekerPasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSeekerPasswordScreen(
    viewModel: JobSeekerPasswordViewModel = hiltViewModel(),
    navigateToStarted: () -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val response by viewModel.response.collectAsState()

    LaunchedEffect(response is Resource.Loading) {
        when (response) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                Toast.makeText(context, response?.data?.message, Toast.LENGTH_SHORT).show()
                viewModel.logout()
                viewModel.setOnLoading(false)
                Handler(Looper.getMainLooper()).postDelayed({
                    navigateToStarted()
                }, 500)
            }
            is Resource.Error -> {
                Toast.makeText(context, response?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnLoading(false)
            }
            else -> viewModel.setOnLoading(false)
        }
    }

    when {
        openDialog.value -> {
            JDialog(
                onDismissRequest = { openDialog.value = false },
                onConfirmation = {
                    openDialog.value = false
                    val request = UpdatePasswordRequest(
                        email = viewModel.email,
                        oldPassword = viewModel.currentPassword,
                        newPassword = viewModel.newPassword
                    )
                    viewModel.update(token.toString(), accountId.toString(), request)
                },
                dialogTitle = stringResource(id = R.string.title_update_password),
                dialogText = stringResource(id = R.string.update_password_desc),
                confirmText = stringResource(id = R.string.update_confirm),
                icon = Icons.Rounded.Password
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.title_update_password))
                },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue40,
                    navigationIconContentColor = Light,
                    titleContentColor = Light
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Light)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                JTextField(
                    keyboardType = KeyboardType.Email,
                    leadingIcon = Icons.Filled.Email,
                    label = stringResource(id = R.string.current_email),
                    placeholder = stringResource(id = R.string.current_email),
                    onKeyUp = viewModel::setOnEmail,
                    textValue = viewModel.email,
                    isReadOnly = viewModel.loading
                )
                JPasswordField(
                    label = stringResource(id = R.string.current_password),
                    placeholder = stringResource(id = R.string.current_password_placeholder),
                    onKeyUp = viewModel::setOnCurrentPassword,
                    textValue = viewModel.currentPassword,
                    isReadOnly = viewModel.loading
                )
                JPasswordField(
                    label = stringResource(id = R.string.new_password),
                    placeholder = stringResource(id = R.string.new_password_placeholder),
                    onKeyUp = viewModel::setOnNewPassword,
                    textValue = viewModel.newPassword,
                    isReadOnly = viewModel.loading
                )
                JPasswordField(
                    label = stringResource(id = R.string.new_password_confirm),
                    placeholder = stringResource(id = R.string.new_password_confirm_placeholder),
                    onKeyUp = viewModel::setOnNewPasswordConfirm,
                    textValue = viewModel.newPasswordConfirm,
                    isReadOnly = viewModel.loading
                )
                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Blue40,
                        contentColor = Light,
                        disabledContainerColor = Blue80,
                        disabledContentColor = Light
                    ),
                    enabled = !viewModel.loading,
                    onClick = {
                        if (viewModel.validateFields()) {
                            if (viewModel.newPasswordConfirm != viewModel.newPassword) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.error_confirm_current_password),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                openDialog.value = true
                            }
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.fields_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.submit),
                            color = Light,
                            fontSize = 16.sp
                        )
                        AnimatedVisibility(false) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = Light
                            )
                        }
                    }
                }
            }
        }
    }
}