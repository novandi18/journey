package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.skeleton.VacancySkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Green
import com.novandi.journey.presentation.ui.theme.Green80
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.journey.presentation.ui.theme.Red80
import com.novandi.journey.presentation.viewmodel.JobSeekerApplyDetailViewModel
import com.novandi.utility.field.ApplicantStatus
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSeekerApplyDetailScreen(
    viewModel: JobSeekerApplyDetailViewModel = hiltViewModel(),
    vacancyId: String,
    back: () -> Unit
) {
    val context = LocalContext.current

    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()

    val vacancy by viewModel.vacancy.collectAsState()
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
                viewModel.resetVacancyState()
            }
            is Resource.Error -> {
                viewModel.setOnLoading(false)
                Toast.makeText(context, vacancy?.message, Toast.LENGTH_SHORT).show()
                viewModel.resetVacancyState()
            }
            else -> viewModel.setOnLoading(false)
        }
    }

    LaunchedEffect(applies is Resource.Loading) {
        when (applies) {
            is Resource.Loading -> viewModel.setOnStatusLoading(true)
            is Resource.Success -> {
                viewModel.setOnVacancyStatusData(applies?.data)
                viewModel.setOnStatusLoading(false)
                viewModel.resetVacancyStatusState()
            }
            is Resource.Error -> {
                viewModel.setOnStatusLoading(false)
                Toast.makeText(context, applies?.message, Toast.LENGTH_SHORT).show()
                viewModel.resetVacancyStatusState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.your_job_apply),
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
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Light)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (viewModel.statusLoading && viewModel.loading && viewModel.vacancyStatusData != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shimmer(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(124.dp)
                                .background(DarkGray40, RoundedCornerShape(24.dp))
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth(.4f)
                                    .height(16.dp)
                                    .background(DarkGray40)
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(20.dp)
                                        .background(DarkGray40)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth(.6f)
                                        .height(20.dp)
                                        .background(DarkGray40)
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth(.4f)
                                .height(16.dp)
                                .background(DarkGray40)
                        )
                    }

                    VacancySkeleton(
                        paddingValues = PaddingValues(0.dp)
                    )
                } else if (viewModel.vacancyStatusData != null && viewModel.vacancyData != null) {
                    val statusData = viewModel.vacancyStatusData!!.filter {
                        it.vacancyId == vacancyId
                    }[0]
                    val status = statusData.status.lowercase()
                    val notes = statusData.notes

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = when (status) {
                                ApplicantStatus.ACCEPTED.value -> Green.copy(.2f)
                                ApplicantStatus.REJECTED.value -> Red.copy(.2f)
                                else -> DarkGray
                            }
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = when (status) {
                                ApplicantStatus.ACCEPTED.value -> Green
                                ApplicantStatus.REJECTED.value -> Red
                                else -> DarkGray80
                            }
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = when (status) {
                                    ApplicantStatus.ACCEPTED.value -> Green80
                                    ApplicantStatus.REJECTED.value -> Red80
                                    else -> DarkGray80
                                }
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(
                                    id = when (status) {
                                        ApplicantStatus.ACCEPTED.value -> R.string.job_accepted_desc
                                        ApplicantStatus.REJECTED.value -> R.string.job_rejected_desc
                                        else -> R.string.job_pending_desc
                                    }
                                ),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = when (status) {
                                    ApplicantStatus.ACCEPTED.value -> Green80
                                    ApplicantStatus.REJECTED.value -> Red80
                                    else -> DarkGray80
                                }
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.notes_from_job_provider),
                            fontSize = 12.sp,
                            color = DarkGray80
                        )
                        SelectionContainer {
                            Text(
                                text = notes ?: stringResource(id = R.string.no_notes),
                                fontSize = 14.sp,
                                color = Dark
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.your_vacancy_applied),
                            fontSize = 12.sp,
                            color = DarkGray80
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = viewModel.vacancyData!!.placementAddress,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 32.sp
                                )
                                Text(
                                    modifier = Modifier
                                        .background(
                                            color = Dark.copy(alpha = .1f),
                                            shape = CircleShape
                                        )
                                        .padding(horizontal = 16.dp, vertical = 4.dp),
                                    text = JobTypes.types()[viewModel.vacancyData!!.jobType - 1],
                                    fontSize = 14.sp
                                )
                            }
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = viewModel.vacancyData!!.description,
                                fontSize = 14.sp
                            )
                        }
                        HorizontalDivider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                                model = viewModel.vacancyData!!.companyLogo,
                                contentDescription = null
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = viewModel.vacancyData!!.companyName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = viewModel.vacancyData!!.sectorName,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        HorizontalDivider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
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
                                    text = viewModel.vacancyData!!.disabilityName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        HorizontalDivider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
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
                                    text = viewModel.vacancyData!!.skillOne,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    modifier = Modifier
                                        .background(color = Dark.copy(.1f), shape = CircleShape)
                                        .padding(horizontal = 16.dp, vertical = 4.dp),
                                    text = viewModel.vacancyData!!.skillTwo,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                } else {
                    NetworkError {
                        if (viewModel.vacancyData == null) viewModel.getVacancy(vacancyId)
                        if (viewModel.vacancyStatusData == null)
                            viewModel.getVacancyStatus(token.toString(), accountId.toString())
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JobSeekerApplyDetailScreenPreview() {
    JourneyTheme {
        JobSeekerApplyDetailScreen(
            vacancyId = "",
            back = {}
        )
    }
}