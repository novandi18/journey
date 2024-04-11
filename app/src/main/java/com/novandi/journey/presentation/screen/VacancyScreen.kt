package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.novandi.core.consts.JobTypes
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.JobApplyStatus
import com.novandi.core.domain.model.Vacancy
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.bar.VacancyBar
import com.novandi.journey.presentation.ui.component.skeleton.VacancyBarSkeleton
import com.novandi.journey.presentation.ui.component.skeleton.VacancySkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.VacancyViewModel
import com.novandi.utility.data.currentDateTime
import com.novandi.utility.field.ApplicantStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyScreen(
    viewModel: VacancyViewModel = hiltViewModel(),
    vacancyId: String,
    navigateBack: () -> Unit,
    navigateToEditVacancy: (Vacancy) -> Unit
) {
    val context = LocalContext.current

    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val roleId by viewModel.roleId.observeAsState()

    val vacancy by viewModel.vacancy.collectAsState()
    val applyStatus by viewModel.applyResult.collectAsState()
    val applies by viewModel.applies.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getVacancy(vacancyId)
        viewModel.getVacancyStatus(token.toString(), accountId.toString())
    }

    LaunchedEffect(vacancy is Resource.Loading) {
        when (vacancy) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                viewModel.setOnVacancyData(vacancy?.data)
                viewModel.setOnLoading(false)
            }
            is Resource.Error -> viewModel.setOnLoading(false)
            else -> viewModel.setOnLoading(false)
        }
    }

    LaunchedEffect(applies is Resource.Loading) {
        when (applies) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.setOnVacancyStatusData(applies?.data)
            }
            is Resource.Error -> {}
            else -> {}
        }
    }

    LaunchedEffect(applyStatus is Resource.Loading) {
        when (applyStatus) {
            is Resource.Loading -> viewModel.setOnApplyLoading(true)
            is Resource.Success -> {
                val newApplies = listOf(
                    JobApplyStatus(
                        vacancyId = vacancyId,
                        status = ApplicantStatus.PENDING.value,
                        appliedAt = currentDateTime()
                    )
                )
                viewModel.setOnVacancyStatusData(newApplies)
                viewModel.setOnApplyLoading(false)
            }
            is Resource.Error -> {
                Toast.makeText(context, applyStatus?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnApplyLoading(false)
            }
            else -> viewModel.setOnApplyLoading(false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.detail_vacancy),
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue40,
                    titleContentColor = Light
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Light
                        )
                    }
                },
                actions = {
                    if (roleId == 2) {
                        if (viewModel.vacancyData != null) {
                            IconButton(
                                onClick = {
                                    navigateToEditVacancy(viewModel.vacancyData!!)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = stringResource(id = R.string.edit_vacancy),
                                    tint = Light
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (roleId == 1) {
                BottomAppBar(
                    modifier = Modifier.shadow(16.dp),
                    containerColor = Light,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    if (viewModel.loading) {
                        VacancyBarSkeleton()
                    } else if (viewModel.vacancyData != null && viewModel.vacancyStatusData != null) {
                        val status = viewModel.vacancyStatusData!!.filter {
                            it.vacancyId == vacancyId
                        }

                        VacancyBar(
                            vacancy = viewModel.vacancyData!!,
                            vacancyStatus = if (status.isEmpty()) null else status[0],
                            loading = viewModel.applyLoading,
                            doApply = {
                                viewModel.setOnApplyLoading(true)
                                viewModel.applyVacancy(token.toString(), accountId.toString(), vacancyId)
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Light)
                .verticalScroll(rememberScrollState())
        ) {
            if (viewModel.loading) {
                VacancySkeleton()
            } else if (viewModel.vacancyData != null) {
                VacancyContent(viewModel.vacancyData!!)
            } else {
                NetworkError {
                    viewModel.getVacancy(vacancyId)
                }
            }
        }
    }
}

@Composable
fun VacancyContent(
    vacancy: Vacancy
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = vacancy.placementAddress,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
                Text(
                    modifier = Modifier
                        .background(color = Dark.copy(alpha = .1f), shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = JobTypes.types()[vacancy.jobType - 1],
                    fontSize = 14.sp
                )
            }
            Text(
                text = vacancy.description,
                fontSize = 14.sp
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(48.dp).clip(CircleShape),
                model = vacancy.companyLogo,
                contentDescription = null
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = vacancy.companyName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = vacancy.sectorName,
                    fontSize = 14.sp
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Filled.AssistWalker,
                contentDescription = stringResource(id = R.string.disability),
                tint = Blue40
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.vacancy_apply),
                    fontSize = 12.sp,
                    color = Dark.copy(.8f)
                )
                Text(
                    text = vacancy.disabilityName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = stringResource(id = R.string.disability),
                tint = Blue40
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.skill_requiremnt),
                    fontSize = 12.sp,
                    color = Dark.copy(.8f)
                )
                Text(
                    modifier = Modifier
                        .background(color = Dark.copy(.1f), shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = vacancy.skillOne,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    modifier = Modifier
                        .background(color = Dark.copy(.1f), shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = vacancy.skillTwo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        VacancyScreen(vacancyId = "1", navigateBack = {}, navigateToEditVacancy = {})
    }
}