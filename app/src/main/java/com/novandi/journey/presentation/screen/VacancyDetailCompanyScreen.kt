package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WorkOff
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.consts.JobTypes
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.CloseVacancyRequest
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.model.VacancyDetailCompany
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.dialog.JDialog
import com.novandi.journey.presentation.ui.component.skeleton.VacancySkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.journey.presentation.ui.theme.Red80
import com.novandi.journey.presentation.viewmodel.VacancyDetailCompanyViewModel
import com.novandi.utility.data.ConvertUtil
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailCompanyScreen(
    viewModel: VacancyDetailCompanyViewModel = hiltViewModel(),
    back: () -> Unit,
    vacancyId: String,
    navigateToEditVacancy: (Vacancy) -> Unit
) {
    val context = LocalContext.current
    val vacancy by viewModel.vacancy.collectAsState()
    val closeVacancy by viewModel.closeVacancy.collectAsState()
    val accountId by viewModel.accountId.observeAsState(null)
    val token by viewModel.token.observeAsState(null)

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

    LaunchedEffect(closeVacancy) {
        when (closeVacancy) {
            is Resource.Loading -> viewModel.setOnCloseLoading(true)
            is Resource.Success -> {
                viewModel.setOnDeadlineTime()
                Toast.makeText(context, closeVacancy?.data?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnCloseLoading(false)
                viewModel.resetCloseState()
            }
            is Resource.Error -> {
                Toast.makeText(context, closeVacancy?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnCloseLoading(false)
                viewModel.resetCloseState()
            }
            else -> viewModel.setOnCloseLoading(false)
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
                            if (accountId != null && vacancy?.data != null) {
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
                                        companyId = accountId.toString()
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, end = 24.dp, start = 24.dp)
                            .shimmer()
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .background(DarkGray40)
                        )
                    }
                    VacancySkeleton()
                } else if (viewModel.vacancyData == null) {
                    NetworkError {
                        viewModel.getVacancy(vacancyId)
                    }
                } else {
                    Content(
                        vacancy = viewModel.vacancyData!!,
                        closeVacancy = {
                            if (viewModel.vacancyData != null && token != null && accountId != null) {
                                viewModel.closeVacancy(
                                    token = token.toString(),
                                    request = CloseVacancyRequest(
                                        vacancyId = vacancyId,
                                        companyId = accountId.toString()
                                    )
                                )
                            }
                        },
                        closeLoading = viewModel.closeLoading
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    vacancy: VacancyDetailCompany,
    closeVacancy: () -> Unit,
    closeLoading: Boolean = false
) {
    val openCloseVacancyDialog = remember { mutableStateOf(false) }

    when {
        openCloseVacancyDialog.value -> {
            JDialog(
                onDismissRequest = { openCloseVacancyDialog.value = false },
                onConfirmation = {
                    closeVacancy()
                    openCloseVacancyDialog.value = false
                },
                dialogTitle = stringResource(id = R.string.delete_vacancy_title),
                dialogText = stringResource(id = R.string.delete_vacancy_desc),
                confirmText = stringResource(id = R.string.delete_vacancy_confirm),
                icon = Icons.Default.WorkOff
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (ConvertUtil.isDeadlinePassed(vacancy.deadlineTime)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Red.copy(.1f),
                    contentColor = Red
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Red
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Rounded.Info,
                        contentDescription = stringResource(id = R.string.vacancy_closed),
                        tint = Red
                    )
                    Text(
                        text = stringResource(id = R.string.vacancy_closed),
                        fontSize = 14.sp,
                        color = Red
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Red.copy(.1f),
                    contentColor = Red
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Red
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.delete_vacancy_desc),
                            fontSize = 12.sp
                        )
                        Button(
                            onClick = { openCloseVacancyDialog.value = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Red,
                                disabledContainerColor = Red80,
                                contentColor = Light,
                                disabledContentColor = Red
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 0.dp
                            ),
                            enabled = !closeLoading
                        ) {
                            Text(
                                text = stringResource(id = R.string.delete_vacancy_title),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (closeLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 8.dp),
                            color = Red
                        )
                    }
                }
            }
        }

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
                    fontSize = 14.sp,
                    color = Dark
                )
            }
            Text(
                text = vacancy.description,
                fontSize = 14.sp,
                color = Dark
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
                    fontWeight = FontWeight.Medium,
                    color = Dark
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
                contentDescription = stringResource(id = R.string.skill_requiremnt),
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
                    fontWeight = FontWeight.Medium,
                    color = Dark
                )
                Text(
                    modifier = Modifier
                        .background(color = Dark.copy(.1f), shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = vacancy.skillTwo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Dark
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
                imageVector = Icons.Filled.Timer,
                contentDescription = stringResource(id = R.string.deadline_close),
                tint = Blue40
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.deadline_close),
                    fontSize = 12.sp,
                    color = Dark.copy(.8f)
                )
                Text(
                    text = vacancy.deadlineTime,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Red
                )
            }
        }
    }
}