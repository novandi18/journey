package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.novandi.core.data.response.Resource
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.auth.AuthFooter
import com.novandi.journey.presentation.ui.component.auth.AuthLoginForm
import com.novandi.journey.presentation.ui.component.auth.AuthTitle
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobProviderLoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProviderLoginScreen(
    viewModel: JobProviderLoginViewModel = hiltViewModel(),
    backToStarted: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToHome: () -> Unit
) {
    val auth by viewModel.auth.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(auth is Resource.Loading) {
        when (auth) {
            is Resource.Loading -> viewModel.setIsLoading(true)
            is Resource.Success -> {
                viewModel.setRoleId()
                viewModel.setAccountId(auth!!.data!!.id)
                viewModel.setToken(auth!!.data!!.token)
                viewModel.setIsLoading(false)
                navigateToHome()
            }
            is Resource.Error -> {
                viewModel.setIsLoading(false)
                Toast.makeText(
                    context,
                    context.getString(R.string.login_failed),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearErrorMessage()
            }
            else -> viewModel.setIsLoading(false)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.jobprovider),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Light,
                    titleContentColor = Dark,
                    navigationIconContentColor = Dark
                ),
                navigationIcon = {
                    IconButton(onClick = { backToStarted() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .height(28.dp)
                            .width(28.dp)
                            .background(color = Blue40, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        JobProviderLoginContent(
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues),
            navigateToRegister = navigateToRegister,
            doLogin = {
                viewModel.login()
            }
        )
    }
}

@Composable
private fun JobProviderLoginContent(
    viewModel: JobProviderLoginViewModel,
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit,
    doLogin: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Light)
            .verticalScroll(rememberScrollState())
    ) {
        AuthTitle(
            title = stringResource(id = R.string.login_title),
            description = stringResource(id = R.string.login_description)
        )
        AuthLoginForm(
            doLogin = doLogin,
            isLoading = viewModel.isLoading,
            email = viewModel.email,
            password = viewModel.password,
            setEmail = viewModel::setOnEmail,
            setPassword = viewModel::setOnPassword
        )
        AuthFooter(
            navigateToRegister = navigateToRegister
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        JobProviderLoginScreen(
            backToStarted = {},
            navigateToRegister = {},
            navigateToHome = {}
        )
    }
}