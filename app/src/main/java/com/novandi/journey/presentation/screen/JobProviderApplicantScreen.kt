package com.novandi.journey.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.data.response.Resource
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.card.JCardVacancy
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.component.state.PullToRefreshLazyColumn
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobProviderApplicantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProviderApplicantScreen(
    viewModel: JobProviderApplicantViewModel = hiltViewModel(),
    navigateToApplicant: (String) -> Unit
) {
    val vacancies by viewModel.vacancies.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val accountId by viewModel.accountId.collectAsState()
    val token by viewModel.token.collectAsState()

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
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Light)
        ) {
            when (vacancies) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        JCardSkeleton()
                    }
                }

                is Resource.Error -> {
                    NetworkError {
                        viewModel.vacancies(token.toString(), accountId.toString())
                    }
                }

                else -> {
                    PullToRefreshLazyColumn(
                        items = if (vacancies is Resource.Success) vacancies.data ?: emptyList() else emptyList(),
                        content = { vacancy ->
                            JCardVacancy(
                                vacancy = vacancy,
                                navigateToDetail = navigateToApplicant,
                                simple = true,
                                totalApplicants = vacancy.totalApplicants ?: 0
                            )
                        },
                        isRefreshing = isRefreshing,
                        onRefresh = viewModel::onRefresh
                    )
                }
            }
        }
    }
}