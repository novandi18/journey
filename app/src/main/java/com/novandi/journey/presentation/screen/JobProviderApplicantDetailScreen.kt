package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.data.response.Resource
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.card.JCardApplicant
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobProviderApplicantDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProviderApplicantDetailScreen(
    viewModel: JobProviderApplicantDetailViewModel = hiltViewModel(),
    vacancyId: String,
    back: () -> Unit
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val applicants by viewModel.applicants.observeAsState(Resource.Loading())
    val response by viewModel.response.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getApplicants(token.toString(), accountId.toString(), vacancyId)
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
            is Resource.Loading -> viewModel.setOnResponseLoading(true)
            is Resource.Success -> {
                Toast.makeText(context, response!!.data?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnResponseLoading(false)
            }
            is Resource.Error -> {
                Toast.makeText(context, response!!.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnResponseLoading(false)
            }
            else -> viewModel.setOnResponseLoading(false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.job_vacancy_user)
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
        Column(
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.data!!.size) { index ->
                        JCardApplicant(
                            applicant = viewModel.data!![index],
                            onAccept = { isAccept ->
                                if (isAccept) {
                                    viewModel.acceptApplicant(
                                        token.toString(), accountId.toString(),
                                        vacancyId, viewModel.data!![index].id
                                    )
                                } else {
                                    viewModel.rejectApplicant(
                                        token.toString(), accountId.toString(),
                                        vacancyId, viewModel.data!![index].id
                                    )
                                }
                            },
                            isLoading = viewModel.responseLoading
                        )
                    }
                }
            }
        }
    }
}