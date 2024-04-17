package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.consts.JobTypes
import com.novandi.core.data.response.Resource
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.card.JCardApplicant
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.component.state.PullToRefreshLazyColumn
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.journey.presentation.viewmodel.JobProviderApplicantDetailViewModel
import com.novandi.utility.field.ApplicantStatus
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProviderApplicantDetailScreen(
    viewModel: JobProviderApplicantDetailViewModel = hiltViewModel(),
    vacancyId: String,
    back: () -> Unit,
    navigateToApplicantProfile: (applicantId: String) -> Unit
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val applicants by viewModel.applicants.observeAsState(Resource.Loading())
    val vacancy by viewModel.vacancy.observeAsState(Resource.Loading())
    val response by viewModel.response.collectAsState()
    var vacancyToggle by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.getApplicants(token.toString(), accountId.toString(), vacancyId)
        viewModel.getVacancyById(vacancyId)
    }

    LaunchedEffect(applicants is Resource.Loading) {
        when (applicants) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                viewModel.setOnData(applicants.data)
                viewModel.setOnLoading(false)
            }
            is Resource.Error -> viewModel.setOnLoading(false)
        }
    }

    LaunchedEffect(response is Resource.Loading) {
        when (response) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                Toast.makeText(context, response!!.data?.message, Toast.LENGTH_SHORT).show()
            }
            is Resource.Error -> {
                Toast.makeText(context, response!!.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    LaunchedEffect(vacancy is Resource.Loading) {
        when (vacancy) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.setOnVacancyData(vacancy.data)
            }
            is Resource.Error -> {
                Toast.makeText(context, vacancy.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.job_vacancy_user),
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
                            back()
                        }
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
        ) {
            if (viewModel.loading) {
                JCardSkeleton()
            } else if (viewModel.data == null) {
                NetworkError {
                    viewModel.getApplicants(token.toString(), accountId.toString(), vacancyId)
                }
            } else if (viewModel.data!!.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(256.dp),
                            painter = painterResource(id = R.drawable.job_apply_empty),
                            contentDescription = stringResource(id = R.string.job_apply_empty)
                        )
                        Text(
                            text = stringResource(id = R.string.job_apply_empty),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkGray80
                        )
                    }
                }
            } else {
                PullToRefreshLazyColumn(
                    paddingValues = PaddingValues(
                        bottom = if (vacancyToggle) 224.dp else 104.dp,
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp
                    ),
                    items = viewModel.data!!,
                    content = { applicant ->
                        JCardApplicant(
                            applicant = applicant,
                            onAccept = { isAccept ->
                                if (isAccept) {
                                    viewModel.acceptApplicant(
                                        token.toString(), accountId.toString(),
                                        vacancyId, applicant.id
                                    )
                                } else {
                                    viewModel.rejectApplicant(
                                        token.toString(), accountId.toString(),
                                        vacancyId, applicant.id
                                    )
                                }
                                viewModel.setOnResponseLoading(applicant.id, isAccept)
                            },
                            loading = viewModel.responseLoading,
                            status = when (applicant.status.lowercase()) {
                                ApplicantStatus.ACCEPTED.value -> true
                                ApplicantStatus.REJECTED.value -> false
                                else -> viewModel.done.find {
                                    it.applicantId == applicant.id
                                }?.isAccepted
                            },
                            navigateToApplicantProfile = navigateToApplicantProfile
                        )
                    },
                    isRefreshing = viewModel.loading,
                    onRefresh = {
                        viewModel.getApplicants(token.toString(), accountId.toString(), vacancyId)
                    }
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Light
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 12.dp
                    ),
                    border = BorderStroke(1.dp, DarkGray40),
                    onClick = {
                        vacancyToggle = !vacancyToggle
                    }
                ) {
                    if (viewModel.vacancyData != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = viewModel.vacancyData!!.placementAddress,
                                    fontSize = 12.sp,
                                    color = Dark,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = if (vacancyToggle) Icons.Rounded.KeyboardArrowDown else
                                        Icons.Rounded.KeyboardArrowUp,
                                    contentDescription = null,
                                    tint = Dark
                                )
                            }
                            Text(
                                text = JobTypes.types()[viewModel.vacancyData!!.jobType - 1],
                                fontSize = 12.sp,
                                color = Dark
                            )
                            AnimatedVisibility(visible = vacancyToggle) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .background(
                                                    color = Blue40,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(vertical = 2.dp, horizontal = 8.dp),
                                            text = viewModel.vacancyData!!.skillOne,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Light,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        Text(
                                            modifier = Modifier
                                                .background(
                                                    color = Blue40,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(vertical = 2.dp, horizontal = 8.dp),
                                            text = viewModel.vacancyData!!.skillTwo,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Light,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.disability),
                                            fontSize = 12.sp,
                                            color = DarkGray80
                                        )
                                        Text(
                                            text = viewModel.vacancyData!!.disabilityName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.End),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.closed_at),
                                            fontSize = 10.sp,
                                            color = DarkGray80
                                        )
                                        Text(
                                            text = viewModel.vacancyData!!.deadlineTime,
                                            fontWeight = FontWeight.Medium,
                                            color = Red,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(16.dp).shimmer(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth(.5f).height(16.dp)
                                    .background(DarkGray40)
                            )
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth(.3f)
                                    .height(12.dp)
                                    .background(DarkGray40)
                            )
                        }
                    }
                }
            }
        }
    }
}