package com.novandi.journey.presentation.screen

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.request.RecommendationVacanciesRequest
import com.novandi.core.domain.model.Vacancy
import com.novandi.journey.R
import com.novandi.journey.presentation.notification.NotificationWorker
import com.novandi.journey.presentation.ui.component.card.JCard
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton
import com.novandi.journey.presentation.ui.component.state.LazyColumnPaging
import com.novandi.journey.presentation.ui.component.state.PullToRefreshPaging
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobSeekerHomeViewModel
import com.novandi.utility.data.IntentExtra

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSeekerHomeScreen(
    viewModel: JobSeekerHomeViewModel = hiltViewModel(),
    navigateToVacancy: (String) -> Unit,
    navigateToJobApplyDetail: (vacancyId: String) -> Unit
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val isDoSearching by viewModel.isDoSearching.collectAsState()
    var tabSelected by remember { mutableIntStateOf(0) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val jobApplyExtra = IntentExtra.getExtra(context, NotificationWorker.JOB_APPLY)
    val recommendation by viewModel.recommendations.collectAsState()

    val searches = viewModel.searches.observeAsState(listOf())
    val searchByQuery = searches.value.filter { search ->
        search.keyword.contains(searchText)
    }

    val tabs = listOf(
        stringResource(id = R.string.recommended_for_you),
        stringResource(id = R.string.newest),
        stringResource(id = R.string.all),
        stringResource(id = R.string.most_popular),
    )

    if (jobApplyExtra != null) {
        navigateToJobApplyDetail(jobApplyExtra)
        IntentExtra.deleteExtra(context, NotificationWorker.JOB_APPLY)
    }

    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val result = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result != null && result[0].isNotEmpty()) {
                viewModel.onSearchTextChange(result[0])
                val isSearchQueryExist = searchByQuery.filter { search ->
                    search.keyword == searchText
                }

                if (isSearchQueryExist.isEmpty()) viewModel.saveSearch()
                keyboardController?.hide()
                viewModel.onToggleDoSearch(true)
            }
        }
    }

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blue40)
                    .padding(
                        if (isSearching) PaddingValues(0.dp)
                        else PaddingValues(top = 4.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
                    ),
                query = searchText,
                onQueryChange = { q ->
                    viewModel.onToggleDoSearch(false)
                    viewModel.onSearchTextChange(q)
                },
                onSearch = {
                    val isSearchQueryExist = searchByQuery.filter { search ->
                        search.keyword == searchText
                    }

                    if (isSearchQueryExist.isEmpty()) viewModel.saveSearch()
                    keyboardController?.hide()
                    viewModel.onToggleDoSearch(true)
                },
                active = isSearching,
                onActiveChange = { viewModel.onToogleSearch() },
                colors = SearchBarDefaults.colors(
                    containerColor = Light,
                    dividerColor = DarkGray40,
                    inputFieldColors = TextFieldDefaults.colors(
                        cursorColor = DarkGray80,
                        focusedContainerColor = Light,
                        unfocusedContainerColor = Light,
                        focusedTextColor = Dark,
                        unfocusedTextColor = DarkGray80
                    )
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_placeholder),
                        fontSize = 14.sp,
                        color = DarkGray40
                    )
                },
                leadingIcon = {
                    if (isSearching) {
                        IconButton(
                            onClick = {
                                viewModel.onToogleSearch()
                                viewModel.onToggleDoSearch(false)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = stringResource(id = R.string.back),
                                tint = DarkGray80
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = stringResource(id = R.string.search),
                            tint = Blue40
                        )
                    }
                },
                trailingIcon = {
                    Row {
                        if (searchText.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    viewModel.onSearchTextChange("")
                                    viewModel.onToggleDoSearch(false)
                                    keyboardController?.show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = stringResource(id = R.string.reset_search),
                                    tint = Dark
                                )
                            }
                        }
                        if (isSearching) {
                            IconButton(
                                onClick = {
                                    if (!SpeechRecognizer.isRecognitionAvailable(context)) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.voice_search_not_available),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

                                        intent.putExtra(
                                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                            RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
                                        )
                                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id")
                                        intent.putExtra(
                                            RecognizerIntent.EXTRA_PROMPT,
                                            context.getString(R.string.speak_something)
                                        )
                                        voiceLauncher.launch(intent)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Mic,
                                    contentDescription = stringResource(id = R.string.reset_search),
                                    tint = Dark
                                )
                            }
                        }
                    }
                }
            ) {
                if (isDoSearching) {
                    val vacancies = viewModel.searchVacancies(searchText).collectAsLazyPagingItems()

                    LazyColumnPaging(
                        items = vacancies,
                        content = { vacancy ->
                            JCard(
                                vacancy = vacancy,
                                setClick = navigateToVacancy
                            )
                        }
                    )
                } else {
                    LazyColumn {
                        items(searchByQuery.size) { index ->
                            TextButton(
                                onClick = {
                                    viewModel.onSearchTextChange(searchByQuery[index].keyword)
                                    viewModel.onToggleDoSearch(true)
                                    keyboardController?.hide()
                                },
                                shape = RectangleShape
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .padding(end = 8.dp),
                                        text = searchByQuery[index].keyword,
                                        overflow = TextOverflow.Ellipsis,
                                        color = Dark,
                                        maxLines = 1
                                    )
                                    IconButton(
                                        onClick = {
                                            viewModel.deleteSearch(searchByQuery[index].id)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = null,
                                            tint = Dark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
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

                        LaunchedEffect(skillOne, skillTwo, disability) {
                            if (disability != null && skillOne != null && skillTwo != null) {
                                viewModel.getRecommendations(
                                    RecommendationRequest(disability!!, skillOne!!, skillTwo!!)
                                )
                            }
                        }

                        LaunchedEffect(recommendation is Resource.Loading) {
                            when (recommendation) {
                                is Resource.Loading -> viewModel.setOnRecommendationLoading(true)
                                is Resource.Success -> {
                                    if (recommendation?.data != null)
                                        viewModel.setOnRecommendations(recommendation?.data!!)
                                    viewModel.setOnRecommendationLoading(false)
                                    viewModel.resetRecommendationState()
                                }
                                is Resource.Error -> {
                                    viewModel.setOnRecommendationLoading(false)
                                    viewModel.resetRecommendationState()
                                }
                                else -> viewModel.setOnRecommendationLoading(false)
                            }
                        }

                        if (viewModel.recommendationLoading) {
                            Box(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                JCardSkeleton(total = 3)
                            }
                        } else if (viewModel.recommendation != null) {
                            val request = RecommendationVacanciesRequest(viewModel.recommendation!!)
                            val vacancies = viewModel
                                .recommendationVacancies(request)
                                .collectAsLazyPagingItems()

                            JobSeekerHomeContent(
                                vacancies = vacancies,
                                navigateToVacancy = { id ->
                                    navigateToVacancy(id)
                                }
                            )
                        }
                    }
                    1 -> {
                        val vacancies = viewModel.latestVacancies().collectAsLazyPagingItems()
                        JobSeekerHomeContent(
                            vacancies = vacancies,
                            navigateToVacancy = { id ->
                                navigateToVacancy(id)
                            }
                        )
                    }
                    2 -> {
                        val vacancies = viewModel.popularVacancies().collectAsLazyPagingItems()
                        JobSeekerHomeContent(
                            vacancies = vacancies,
                            navigateToVacancy = { id ->
                                navigateToVacancy(id)
                            }
                        )
                    }
                    3 -> {
                        val vacancies = viewModel.vacancies(token.toString()).collectAsLazyPagingItems()
                        JobSeekerHomeContent(
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
                                end = if (index < tabs.lastIndex) 4.dp else 0.dp
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
fun JobSeekerHomeContent(
    vacancies: LazyPagingItems<Vacancy>,
    navigateToVacancy: (String) -> Unit,
    isSearch: Boolean = false
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    PullToRefreshPaging(
        items = vacancies,
        content = { vacancy ->
            JCard(
                vacancy = vacancy,
                setClick = navigateToVacancy
            )
        },
        isRefreshing = if (!isSearch) isRefreshing else false,
        setIsRefreshing = {
            if (!isSearch) isRefreshing = it
        },
        onRefresh = {
            vacancies.refresh()
        }
    )
}