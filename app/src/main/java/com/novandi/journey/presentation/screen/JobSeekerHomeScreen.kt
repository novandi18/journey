package com.novandi.journey.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.local.entity.LatestVacancyEntity
import com.novandi.core.data.source.local.entity.PopularVacancyEntity
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.mapper.VacancyMapper
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.card.JCard
import com.novandi.journey.presentation.ui.component.dialog.RequestNotificationPermissionDialog
import com.novandi.journey.presentation.ui.component.state.PullToRefreshPaging
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobSeekerHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSeekerHomeScreen(
    viewModel: JobSeekerHomeViewModel = hiltViewModel(),
    navigateToVacancy: (String) -> Unit,
    navigateToSearch: () -> Unit
) {
    var tabSelected by remember { mutableIntStateOf(0) }

    RequestNotificationPermissionDialog()

    val tabs = listOf(
        stringResource(id = R.string.recommended_for_you),
        stringResource(id = R.string.all),
        stringResource(id = R.string.newest),
        stringResource(id = R.string.most_popular),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        onClick = navigateToSearch,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Light
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = stringResource(id = R.string.search),
                                tint = Blue40
                            )
                            Text(
                                text = stringResource(id = R.string.search_placeholder),
                                color = DarkGray40,
                                fontSize = 14.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue40
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp)
            ) {
                when (tabSelected) {
                    0 -> {
                        val disability by viewModel.disability.observeAsState()
                        val skillOne by viewModel.skillOne.observeAsState()
                        val skillTwo by viewModel.skillTwo.observeAsState()

                        val vacancies = viewModel.recommendationVacancies.collectAsLazyPagingItems()
                        if (vacancies.itemCount == 0) {
                            if (disability != null && skillOne != null && skillTwo != null) {
                                viewModel.getRecommendations(
                                    RecommendationRequest(
                                        disabilityName = disability.toString(),
                                        skillOne = skillOne.toString(),
                                        skillTwo = skillTwo.toString()
                                    )
                                )
                            }
                        }

                        RecommendationVacanciesContent(
                            vacancies = vacancies,
                            navigateToVacancy = { id ->
                                navigateToVacancy(id)
                            }
                        )
                    }
                    1 -> {
                        val token by viewModel.token.observeAsState()
                        val vacancies = viewModel.allVacancies.collectAsLazyPagingItems()
                        if (vacancies.itemCount == 0) {
                            if (token != null) {
                                viewModel.vacancies(token.toString())
                            }
                        }

                        AllVacanciesContent(
                            vacancies = vacancies,
                            navigateToVacancy = { id ->
                                navigateToVacancy(id)
                            }
                        )
                    }
                    2 -> {
                        val vacancies = viewModel.latestVacancies.collectAsLazyPagingItems()
                        if (vacancies.itemCount == 0) viewModel.latestVacancies()

                        LatestVacanciesContent(
                            vacancies = vacancies,
                            navigateToVacancy = { id ->
                                navigateToVacancy(id)
                            }
                        )
                    }
                    3 -> {
                        val vacancies = viewModel.popularVacancies.collectAsLazyPagingItems()
                        if (vacancies.itemCount == 0) viewModel.popularVacancies()

                        PopularVacanciesContent(
                            vacancies = vacancies,
                            navigateToVacancy = { id ->
                                navigateToVacancy(id)
                            }
                        )
                    }
                }
            }

            ScrollableTabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                selectedTabIndex = tabSelected,
                edgePadding = 16.dp,
                containerColor = Blue40,
                contentColor = Light,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[tabSelected])
                            .fillMaxWidth(),
                        color = Color.Transparent
                    )
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    TextButton(
                        modifier = Modifier
                            .padding(
                                bottom = 12.dp,
                                end = if (index < tabs.lastIndex) 4.dp else 0.dp,
                                top = 4.dp
                            )
                            .height(48.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (tabSelected == index) Light else Light.copy(.2f)
                        ),
                        onClick = {
                            tabSelected = index
                        },
                    ) {
                        Text(
                            text = tab,
                            color = if (tabSelected == index) Blue40 else Light
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationVacanciesContent(
    vacancies: LazyPagingItems<RecommendationVacancyEntity>,
    navigateToVacancy: (String) -> Unit
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    PullToRefreshPaging(
        items = vacancies,
        content = { vacancy ->
            JCard(
                vacancy = VacancyMapper.mapRecommendationVacancyEntityToDomain(vacancy),
                setClick = navigateToVacancy
            )
        },
        isRefreshing = isRefreshing,
        setIsRefreshing = {
            isRefreshing = it
        },
        onRefresh = {
            vacancies.refresh()
        }
    )
}

@Composable
private fun AllVacanciesContent(
    vacancies: LazyPagingItems<AllVacancyEntity>,
    navigateToVacancy: (String) -> Unit
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    PullToRefreshPaging(
        items = vacancies,
        content = { vacancy ->
            JCard(
                vacancy = VacancyMapper.mapAllVacancyEntityToDomain(vacancy),
                setClick = navigateToVacancy
            )
        },
        isRefreshing = false,
        setIsRefreshing = {
            isRefreshing = it
        },
        onRefresh = {
            vacancies.refresh()
        }
    )
}

@Composable
private fun LatestVacanciesContent(
    vacancies: LazyPagingItems<LatestVacancyEntity>,
    navigateToVacancy: (String) -> Unit
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    PullToRefreshPaging(
        items = vacancies,
        content = { vacancy ->
            JCard(
                vacancy = VacancyMapper.mapLatestVacancyEntityToDomain(vacancy),
                setClick = navigateToVacancy
            )
        },
        isRefreshing = false,
        setIsRefreshing = {
            isRefreshing = it
        },
        onRefresh = {
            vacancies.refresh()
        }
    )
}

@Composable
private fun PopularVacanciesContent(
    vacancies: LazyPagingItems<PopularVacancyEntity>,
    navigateToVacancy: (String) -> Unit
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    PullToRefreshPaging(
        items = vacancies,
        content = { vacancy ->
            JCard(
                vacancy = VacancyMapper.mapPopularVacancyEntityToDomain(vacancy),
                setClick = navigateToVacancy
            )
        },
        isRefreshing = isRefreshing,
        setIsRefreshing = {
            isRefreshing = it
        },
        onRefresh = {
            vacancies.refresh()
        }
    )
}