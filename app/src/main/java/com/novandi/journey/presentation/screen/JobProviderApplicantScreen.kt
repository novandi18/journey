package com.novandi.journey.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.data.response.Resource
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.card.JCardVacancy
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobProviderApplicantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProviderApplicantScreen(
    viewModel: JobProviderApplicantViewModel = hiltViewModel(),
    navigateToApplicant: (String) -> Unit
) {
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val vacancies by viewModel.vacancies.observeAsState(Resource.Loading())

    LaunchedEffect(Unit) {
        viewModel.vacancies(token.toString(), accountId.toString())
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
                        text = stringResource(id = R.string.job_vacancy_user)
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
            if (viewModel.loading) {
                JCardSkeleton()
            } else if (viewModel.data == null) {
                NetworkError {
                    viewModel.vacancies(token.toString(), accountId.toString())
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(viewModel.data!!.size) { index ->
                        JCardVacancy(
                            vacancy = viewModel.data!![index],
                            navigateToDetail = navigateToApplicant,
                            simple = true
                        )
                    }
                }
            }
        }
    }
}