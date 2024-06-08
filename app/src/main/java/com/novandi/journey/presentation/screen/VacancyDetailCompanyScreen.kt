package com.novandi.journey.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.consts.JobTypes
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.model.VacancyDetailCompany
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.skeleton.VacancySkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.VacancyDetailCompanyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailCompanyScreen(
    viewModel: VacancyDetailCompanyViewModel = hiltViewModel(),
    back: () -> Unit,
    vacancyId: String,
    navigateToEditVacancy: (Vacancy) -> Unit
) {
    val vacancy by viewModel.vacancy.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getVacancy(vacancyId)
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
                        onClick = back
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Light
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (vacancy?.data != null) {
                                navigateToEditVacancy(
                                    Vacancy(
                                        id = vacancy?.data?.id.toString(),
                                        placementAddress = vacancy?.data?.position.toString(),
                                        description = vacancy?.data?.description.toString(),
                                        createdAt = "",
                                        updatedAt = vacancy?.data?.updatedAt.toString(),
                                        deadlineTime = vacancy?.data?.deadlineTime.toString(),
                                        jobType = JobTypes.types().indexOf(vacancy?.data?.jobType.toString()),
                                        skillOne = vacancy?.data?.skillOne.toString(),
                                        skillTwo = vacancy?.data?.skillTwo.toString(),
                                        disabilityName = vacancy?.data?.disability.toString(),
                                        companyLogo = "",
                                        sectorName = "",
                                        companyName = "",
                                        totalApplicants = null,
                                        companyId = ""
                                    )
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = stringResource(id = R.string.edit_vacancy),
                            tint = Light
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Light)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                if (viewModel.loading && viewModel.vacancyData == null) {
                    VacancySkeleton()
                } else if (viewModel.vacancyData == null) {
                    NetworkError {
                        viewModel.getVacancy(vacancyId)
                    }
                } else {
                    Content(
                        vacancy = viewModel.vacancyData!!
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    vacancy: VacancyDetailCompany
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
                    text = vacancy.position,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
                Text(
                    modifier = Modifier
                        .background(color = Dark.copy(alpha = .1f), shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = vacancy.jobType,
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
                    text = vacancy.disability,
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