package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MapsHomeWork
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobProviderRegisterRequest
import com.novandi.core.domain.model.JobProvider
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.auth.AuthTitle
import com.novandi.journey.presentation.ui.component.field.JDropdownField
import com.novandi.journey.presentation.ui.component.field.JPasswordField
import com.novandi.journey.presentation.ui.component.field.JTextField
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobProviderRegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProviderRegisterScreen(
    viewModel: JobProviderRegisterViewModel = hiltViewModel(),
    backToLogin: () -> Unit
) {
    val register by viewModel.register.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(register is Resource.Loading) {
        when (register) {
            is Resource.Loading -> viewModel.setOnIsLoading(true)
            is Resource.Success -> {
                viewModel.setOnIsLoading(false)
                Toast.makeText(context, register?.data?.message, Toast.LENGTH_SHORT).show()
                backToLogin()
            }
            is Resource.Error -> {
                viewModel.setOnIsLoading(false)
                Toast.makeText(context, register?.message, Toast.LENGTH_SHORT).show()
            }
            else -> viewModel.setOnIsLoading(false)
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
                    IconButton(onClick = { backToLogin() }) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Light)
                .padding(paddingValues)
        ) {
            JobProviderRegisterContent(
                viewModel = viewModel,
                doRegister = { jobProvider ->
                    val request = JobProviderRegisterRequest(
                        name = jobProvider.name,
                        address = jobProvider.address,
                        city = jobProvider.city,
                        province = jobProvider.province,
                        employees = jobProvider.employees,
                        email = jobProvider.email,
                        password = jobProvider.password,
                        sectorId = jobProvider.sectorId
                    )
                    viewModel.register(request)
                },
            )
        }
    }
}

@Composable
fun JobProviderRegisterContent(
    viewModel: JobProviderRegisterViewModel,
    doRegister: (JobProvider) -> Unit
) {
    val regenciesData by viewModel.regenciesData.observeAsState()

    LaunchedEffect(regenciesData is Resource.Loading) {
        when (regenciesData) {
            is Resource.Loading -> viewModel.setOnRegencies(listOf())
            is Resource.Success -> {
                if (regenciesData?.data != null) {
                    viewModel.setOnRegencies(regenciesData!!.data!!)
                }
            }
            is Resource.Error -> {}
            else -> {}
        }
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        AuthTitle(
            title = stringResource(id = R.string.register_title),
            description = stringResource(id = R.string.register_description)
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            JTextField(
                leadingIcon = Icons.Filled.Apartment,
                label = stringResource(id = R.string.company_name),
                keyboardType = KeyboardType.Text,
                placeholder = stringResource(id = R.string.companyname_placeholder),
                onKeyUp = viewModel::setOnCompanyName,
                textValue = viewModel.companyName,
                isReadOnly = viewModel.isLoading
            )
            JTextField(
                leadingIcon = Icons.Filled.Email,
                label = stringResource(id = R.string.email),
                keyboardType = KeyboardType.Email,
                placeholder = stringResource(id = R.string.email_placeholder),
                onKeyUp = viewModel::setOnEmail,
                textValue = viewModel.email,
                isReadOnly = viewModel.isLoading
            )
            JPasswordField(
                onKeyUp = viewModel::setOnPassword,
                textValue = viewModel.password,
                isReadOnly = viewModel.isLoading
            )
            JDropdownField(
                label = stringResource(id = R.string.sector_placeholder),
                icon = Icons.Filled.Factory,
                data = viewModel.sectors.map { it.sector },
                itemSelected = viewModel.sector,
                setItemSelected = viewModel::setOnSector,
                isReadOnly = viewModel.isLoading
            )
            JTextField(
                leadingIcon = Icons.Filled.MapsHomeWork,
                label = stringResource(id = R.string.address),
                keyboardType = KeyboardType.Text,
                placeholder = stringResource(id = R.string.address_placeholder),
                onKeyUp = viewModel::setOnAddress,
                textValue = viewModel.address,
                isReadOnly = viewModel.isLoading
            )
            JTextField(
                leadingIcon = Icons.Filled.Groups,
                label = stringResource(id = R.string.total_employee),
                keyboardType = KeyboardType.Number,
                placeholder = stringResource(id = R.string.totalemployee_placeholder),
                onKeyUp = viewModel::setOnEmployees,
                textValue = viewModel.employees,
                isReadOnly = viewModel.isLoading
            )
            JDropdownField(
                label = stringResource(id = R.string.province_placeholder),
                icon = Icons.Filled.LocationOn,
                data = viewModel.provinces,
                itemSelected = viewModel.province,
                setItemSelected = {
                    viewModel.setOnProvince(it)
                    viewModel.setOnCity(-1)
                    viewModel.getRegencies()
                },
                isReadOnly = viewModel.isLoading
            )
            JDropdownField(
                label = stringResource(id = R.string.city_placeholder),
                icon = Icons.Filled.LocationCity,
                data = viewModel.regencies,
                itemSelected = viewModel.city,
                setItemSelected = viewModel::setOnCity,
                isReadOnly = viewModel.isLoading
            )
            Box(
                modifier = Modifier.padding(vertical = 56.dp, horizontal = 24.dp)
            ) {
                Button(
                    onClick = {
                        doRegister(
                            JobProvider(
                                name = viewModel.companyName,
                                address = viewModel.address,
                                email = viewModel.email,
                                password = viewModel.password,
                                city = viewModel.regencies[viewModel.city],
                                province = viewModel.provinces[viewModel.province],
                                employees = viewModel.employees.toInt(),
                                sectorId = viewModel.sectors[viewModel.sector].id
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = viewModel.validateFields() || viewModel.isLoading,
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
                        AnimatedVisibility(visible = viewModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(28.dp),
                                color = Light
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.btn_register),
                            color = Light,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        JobProviderRegisterScreen(backToLogin = {})
    }
}