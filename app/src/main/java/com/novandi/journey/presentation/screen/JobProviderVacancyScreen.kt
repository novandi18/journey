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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.rounded.AssistWalker
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.domain.model.Vacancy
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.field.JDatePickerField
import com.novandi.journey.presentation.ui.component.field.JDropdownDialog
import com.novandi.journey.presentation.ui.component.field.JTextAreaField
import com.novandi.journey.presentation.ui.component.field.JTextField
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobProviderVacancyViewModel
import com.novandi.utility.data.ConvertUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProviderVacancyScreen(
    viewModel: JobProviderVacancyViewModel = hiltViewModel(),
    vacancy: Vacancy? = null,
    back: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val response by viewModel.response.collectAsState()

    LaunchedEffect(Unit) {
        if (vacancy != null) {
            viewModel.setOnDisability(viewModel.disabilities.filter { it.disability == vacancy.disabilityName }[0].id - 1)
            viewModel.setOnSkillOne(viewModel.skills.filter { it.skill == vacancy.skillOne }[0].id - 1)
            viewModel.setOnSkillTwo(viewModel.skills.filter { it.skill == vacancy.skillTwo }[0].id - 1)
            viewModel.setOnJobType(vacancy.jobType - 1)
            viewModel.setOnPosition(vacancy.placementAddress)
            viewModel.setOnDescription(vacancy.description)
            viewModel.setOnDeadline(ConvertUtil.convertDateValueToActualFormat(vacancy.deadlineTime))
        }
    }

    LaunchedEffect(response is Resource.Loading) {
        when (response) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                Toast.makeText(context, response?.data?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnLoading(false)
                back(true)
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
                    Text(text = stringResource(id = R.string.add_vacancy))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue40,
                    titleContentColor = Light
                ),
                navigationIcon = {
                    IconButton(onClick = { back(false) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Light
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Light)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                JTextField(
                    leadingIcon = Icons.Filled.Work,
                    label = stringResource(id = R.string.placement_address_placeholder),
                    placeholder = stringResource(id = R.string.placement_address_placeholder),
                    onKeyUp = viewModel::setOnPosition,
                    textValue = viewModel.position,
                    isReadOnly = viewModel.loading
                )
                JTextAreaField(
                    title = stringResource(id = R.string.description_placeholder),
                    onKeyUp = viewModel::setOnDescription,
                    value = viewModel.description,
                    isReadOnly = viewModel.loading
                )
                JDropdownDialog(
                    icon = Icons.Filled.Work,
                    label = stringResource(id = R.string.job_type_placeholder),
                    items = viewModel.jobTypes,
                    selectedIndex = viewModel.jobType,
                    setSelectedItem = viewModel::setOnJobType
                )
                JDropdownDialog(
                    icon = Icons.Rounded.AssistWalker,
                    label = stringResource(id = R.string.disability_job_placeholder),
                    items = viewModel.disabilities.map { it.disability },
                    selectedIndex = viewModel.disability,
                    setSelectedItem = viewModel::setOnDisability
                )
                JDropdownDialog(
                    icon = Icons.AutoMirrored.Filled.StarHalf,
                    label = stringResource(id = R.string.skill_one_placeholder),
                    items = viewModel.skills.map { it.skill },
                    selectedIndex = viewModel.skillOne,
                    setSelectedItem = viewModel::setOnSkillOne
                )
                JDropdownDialog(
                    icon = Icons.AutoMirrored.Filled.StarHalf,
                    label = stringResource(id = R.string.skill_two_placeholder),
                    items = viewModel.skills.map { it.skill },
                    selectedIndex = viewModel.skillTwo,
                    setSelectedItem = viewModel::setOnSkillTwo
                )
                JDatePickerField(
                    setDate = viewModel::setOnDeadline,
                    value = viewModel.deadline,
                    isEnabled = !viewModel.loading
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
                onClick = {
                    if (viewModel.validateFields()) {
                        val request = VacancyRequest(
                            placementAddress = viewModel.position,
                            description = viewModel.description,
                            disabilityId = viewModel.disabilities[viewModel.disability].id,
                            skillOne = viewModel.skills[viewModel.skillOne].id.toString(),
                            skillTwo = viewModel.skills[viewModel.skillTwo].id.toString(),
                            jobType = viewModel.jobType + 1,
                            deadlineTime = viewModel.deadline
                        )

                        if (vacancy == null) {
                            viewModel.addVacancy(
                                token = token.toString(),
                                companyId = accountId.toString(),
                                request = request
                            )
                        } else {
                            viewModel.updateVacancy(
                                companyId = accountId.toString(),
                                vacancyId = vacancy.id,
                                request = request
                            )
                        }
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.fields_empty),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue40,
                    contentColor = Light,
                    disabledContainerColor = Blue80,
                    disabledContentColor = Light
                ),
                enabled = !viewModel.loading
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