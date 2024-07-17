package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.novandi.journey.presentation.ui.component.card.JCardApply
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.component.state.PullToRefreshLazyColumn
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobSeekerApplyViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSeekerApplyScreen(
    viewModel: JobSeekerApplyViewModel = hiltViewModel(),
    navigateToDetail: (vacancyId: String) -> Unit
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val vacancies by viewModel.vacancies.collectAsState()

    LaunchedEffect(token, accountId) {
        if (token != null && accountId != null) {
            viewModel.getVacancies(token.toString(), accountId.toString())
        }
    }

    LaunchedEffect(vacancies) {
        when (vacancies) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                if (vacancies?.data != null) viewModel.setOnVacanciesData(vacancies?.data)
                viewModel.setOnLoading(false)
                viewModel.resetState()
            }
            is Resource.Error -> {
                Toast.makeText(context, vacancies?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnLoading(false)
                viewModel.resetState()
            }
            else -> {
                viewModel.setOnLoading(false)
                viewModel.resetState()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.job_apply),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Light)
        ) {
            if (viewModel.loading) {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    JCardSkeleton(total = 3)
                }
            } else if (viewModel.vacanciesData == null) {
                NetworkError {
                    viewModel.getVacancies(token.toString(), accountId.toString())
                }
            } else if (viewModel.vacanciesData!!.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
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
                    items = viewModel.vacanciesData!!,
                    content = { jobApplyStatus ->
                        JCardApply(
                            data = jobApplyStatus,
                            onClick = navigateToDetail
                        )
                    },
                    isRefreshing = viewModel.loading,
                    onRefresh = {
                        viewModel.getVacancies(token.toString(), accountId.toString())
                    }
                )
            }
        }
    }
}