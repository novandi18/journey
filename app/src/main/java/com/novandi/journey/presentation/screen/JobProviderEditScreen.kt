package com.novandi.journey.presentation.screen

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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MapsHomeWork
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobProviderEditRequest
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.field.JDropdownDialog
import com.novandi.journey.presentation.ui.component.field.JTextField
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobProviderEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProviderEditScreen(
    viewModel: JobProviderEditViewModel = hiltViewModel(),
    profile: ProfileJobProvider?,
    back: () -> Unit
) {
    val context = LocalContext.current
    val regenciesData by viewModel.regenciesData.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val response by viewModel.response.collectAsState()

    LaunchedEffect(Unit) {
        if (profile != null) {
            viewModel.setOnName(profile.name)
            viewModel.setOnSectorId(viewModel.sectors.map { it.sector }.indexOf(profile.sectorName))
            viewModel.setOnAddress(profile.address)
            viewModel.setOnProvince(viewModel.provinces.indexOf(profile.province))
            viewModel.setOnEmployees(profile.employees)
        }

        viewModel.getRegencies()
    }

    LaunchedEffect(regenciesData is Resource.Loading) {
        when (regenciesData) {
            is Resource.Loading -> viewModel.setOnRegencies(listOf())
            is Resource.Success -> {
                if (regenciesData?.data != null) {
                    viewModel.setOnRegencies(regenciesData!!.data!!)
                    if (viewModel.regencies.isNotEmpty())
                        viewModel.setOnRegency(viewModel.regencies.indexOf(profile!!.city))
                }
            }
            is Resource.Error -> {}
            else -> {}
        }
    }

    LaunchedEffect(response is Resource.Loading) {
        when (response) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                Toast.makeText(context, response?.data?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnLoading(false)
                back()
            }
            is Resource.Error -> {
                Toast.makeText(context, response?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnLoading(false)
            }
            else -> viewModel.setOnLoading(false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_update_profile),
                        fontSize = 16.sp
                    )
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
                    leadingIcon = Icons.Filled.Apartment,
                    label = stringResource(id = R.string.company_name),
                    placeholder = stringResource(id = R.string.companyname_placeholder),
                    onKeyUp = viewModel::setOnName,
                    textValue = viewModel.name,
                    isReadOnly = viewModel.loading
                )
                JDropdownDialog(
                    icon = Icons.Filled.Factory,
                    label = stringResource(id = R.string.sector_placeholder),
                    items = viewModel.sectors.map { it.sector },
                    selectedIndex = viewModel.sectorId,
                    setSelectedItem = viewModel::setOnSectorId
                )
                JTextField(
                    leadingIcon = Icons.Filled.MapsHomeWork,
                    label = stringResource(id = R.string.address),
                    keyboardType = KeyboardType.Text,
                    placeholder = stringResource(id = R.string.address_placeholder),
                    onKeyUp = viewModel::setOnAddress,
                    textValue = viewModel.address,
                    isReadOnly = viewModel.loading
                )
                JDropdownDialog(
                    icon = Icons.Filled.LocationOn,
                    label = stringResource(id = R.string.province_placeholder),
                    items = viewModel.provinces,
                    selectedIndex = viewModel.province,
                    setSelectedItem = {
                        viewModel.setOnProvince(it)
                        viewModel.setOnRegency(-1)
                        viewModel.getRegencies()
                    }
                )
                JDropdownDialog(
                    icon = Icons.Filled.LocationCity,
                    label = stringResource(id = R.string.city_placeholder),
                    items = viewModel.regencies,
                    selectedIndex = viewModel.regency,
                    setSelectedItem = viewModel::setOnRegency
                )
                JTextField(
                    leadingIcon = Icons.Filled.Groups,
                    label = stringResource(id = R.string.total_employee),
                    keyboardType = KeyboardType.Number,
                    placeholder = stringResource(id = R.string.totalemployee_placeholder),
                    onKeyUp = { total -> viewModel.setOnEmployees(total.toInt()) },
                    textValue = viewModel.employees.toString(),
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
                            val request = JobProviderEditRequest(
                                name = viewModel.name,
                                address = viewModel.address,
                                city = viewModel.regencies[viewModel.regency],
                                province = viewModel.provinces[viewModel.province],
                                employees = viewModel.employees,
                                sectorId = viewModel.sectors[viewModel.sectorId].id
                            )
                            viewModel.update(accountId.toString(), request)
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
                        AnimatedVisibility(viewModel.loading) {
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

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        JobProviderEditScreen(
            profile = ProfileJobProvider(),
            back = {}
        )
    }
}