package com.novandi.journey.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.novandi.journey.presentation.viewmodel.JobProviderHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProviderHomeScreen(
    viewModel: JobProviderHomeViewModel = hiltViewModel(),
    navigateToVacancy: (String) -> Unit,
    navigateToAdd: () -> Unit
) {
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val vacancies by viewModel.vacancies.observeAsState(Resource.Loading())

    LaunchedEffect(token != null, accountId != null) {
        if (token != null && accountId != null) {
            viewModel.vacancies(token.toString(), accountId.toString())
        }
    }

    LaunchedEffect(vacancies is Resource.Loading) {
        when (vacancies) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                viewModel.setOnData(vacancies.data)
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
                        text = stringResource(id = R.string.job_vacancy),
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue40,
                    titleContentColor = Light
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAdd() },
                containerColor = Blue40,
                contentColor = Light
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.add_vacancy)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Light)
        ) {
            if (viewModel.loading) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    JCardSkeleton()
                }
            } else if (viewModel.data != null) {
                PullToRefreshLazyColumn(
                    items = viewModel.data!!,
                    content = { vacancy ->
                        JCardVacancy(
                            vacancy = vacancy,
                            navigateToDetail = navigateToVacancy
                        )
                    },
                    isRefreshing = viewModel.loading,
                    onRefresh = {
                        viewModel.vacancies(token.toString(), accountId.toString())
                    }
                )
            } else {
                NetworkError {
                    viewModel.vacancies(token.toString(), accountId.toString())
                }
            }
        }
    }
}