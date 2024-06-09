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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AssistWalker
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.novandi.core.consts.FilterMenu
import com.novandi.core.data.source.remote.request.VacanciesSearchRequest
import com.novandi.core.domain.model.Search
import com.novandi.core.domain.model.Vacancy
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.card.JCard
import com.novandi.journey.presentation.ui.component.sheet.SearchFilterSheet
import com.novandi.journey.presentation.ui.component.state.LazyColumnPaging
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobSeekerSearchViewModel
import kotlinx.coroutines.launch

@Composable
fun JobSeekerSearch(
    viewModel: JobSeekerSearchViewModel = hiltViewModel(),
    navigateToVacancy: (String) -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchHistories by viewModel.searches.observeAsState(listOf())
    val searchHistorySuggestion = searchHistories.filter { search ->
        search.keyword.contains(viewModel.search)
    }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val result = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result != null && result[0].isNotEmpty()) {
                viewModel.setOnSearch(result[0])
                val isSearchQueryExist = searchHistorySuggestion.filter { search ->
                    search.keyword == viewModel.search
                }

                if (isSearchQueryExist.isEmpty()) viewModel.saveSearch()
                viewModel.resetFilter()
                viewModel.searchVacancies(
                    query = viewModel.search,
                    filter = VacanciesSearchRequest(
                        jobType = viewModel.jobTypeFilter,
                        disability = viewModel.disabilityFilter,
                        province = viewModel.provinceFilter
                    )
                )
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Light)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Light)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(
                    modifier = Modifier.weight(.15f),
                    onClick = back
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                        tint = Dark
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .weight(.85f)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            viewModel.setOnFocused(it.isFocused)
                            if (it.isFocused) {
                                keyboardController?.show()
                            }
                        },
                    value = viewModel.search,
                    onValueChange = viewModel::setOnSearch,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.search_placeholder),
                            color = DarkGray40,
                            fontSize = 14.sp
                        )
                    },
                    textStyle = TextStyle(
                        color = Dark,
                        fontSize = 14.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = DarkGray,
                        focusedContainerColor = DarkGray,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = if (viewModel.focused) Dark else Color.Transparent
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (viewModel.search.isNotEmpty()) {
                                viewModel.setOnFocused(false)
                                val isSearchQueryExist = searchHistories.filter { search ->
                                    search.keyword == viewModel.search
                                }

                                if (isSearchQueryExist.isEmpty()) viewModel.saveSearch()
                                viewModel.resetFilter()
                                viewModel.searchVacancies(
                                    query = viewModel.search,
                                    filter = VacanciesSearchRequest(
                                        jobType = viewModel.jobTypeFilter,
                                        disability = viewModel.disabilityFilter,
                                        province = viewModel.provinceFilter
                                    )
                                )
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        }
                    ),
                    shape = CircleShape,
                    trailingIcon = {
                        if (viewModel.search.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    viewModel.clearSearchVacancies()
                                    viewModel.setOnSearch("")
                                    viewModel.setOnFocused(true)
                                    focusRequester.requestFocus()
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
                    }
                )
                IconButton(
                    modifier = Modifier.weight(.15f),
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
                        contentDescription = stringResource(id = R.string.voice_search),
                        tint = Dark
                    )
                }
            }
            if (viewModel.focused) {
                HorizontalDivider(
                    color = DarkGray,
                    thickness = 1.dp
                )
            } else {
                ScrollableTabRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    selectedTabIndex = viewModel.filterTabSelected,
                    edgePadding = 16.dp,
                    containerColor = Light,
                    contentColor = Dark,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[viewModel.filterTabSelected])
                                .fillMaxWidth(),
                            color = Color.Transparent
                        )
                    },
                    divider = {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = DarkGray
                        )
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterMenu.menu().forEachIndexed { index, tab ->
                            TextButton(
                                modifier = Modifier
                                    .padding(bottom = 12.dp)
                                    .height(48.dp),
                                onClick = {
                                    viewModel.setOnFilter(Pair(tab, index))
                                    viewModel.setOnShowFilterSheet(true)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkGray,
                                    contentColor = DarkGray80
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .padding(end = 2.dp),
                                        imageVector = when (index) {
                                            0 -> Icons.Rounded.Work
                                            1 -> Icons.Rounded.AssistWalker
                                            else -> Icons.Rounded.LocationOn
                                        },
                                        contentDescription = null,
                                        tint = DarkGray80
                                    )
                                    Text(
                                        text = when (index) {
                                            0 -> viewModel.jobTypeFilter
                                            1 -> viewModel.disabilityFilter
                                            else -> viewModel.provinceFilter
                                        },
                                        fontSize = 14.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Rounded.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = DarkGray80
                                    )
                                }
                            }
                        }
                    }
                }

                if (viewModel.showFilterSheet) {
                    SearchFilterSheet(
                        data = viewModel.filter.first,
                        selectedItem = when (viewModel.filter.second) {
                            0 -> viewModel.jobTypeFilter
                            1 -> viewModel.disabilityFilter
                            else -> viewModel.provinceFilter
                        },
                        onSelected = { value ->
                            when (viewModel.filter.second) {
                                0 -> viewModel.setOnJobTypeFilter(value)
                                1 -> viewModel.setOnDisabilityFilter(value)
                                else -> viewModel.setOnProvinceFilter(value)
                            }
                            viewModel.clearSearchVacancies()
                            coroutineScope.launch {
                                lazyListState.scrollToItem(0)
                            }
                            viewModel.searchVacancies(
                                query = viewModel.search,
                                filter = VacanciesSearchRequest(
                                    jobType = viewModel.jobTypeFilter,
                                    disability = viewModel.disabilityFilter,
                                    province = viewModel.provinceFilter
                                )
                            )
                            viewModel.setOnShowFilterSheet(false)
                        },
                        onDismissed = {
                            viewModel.setOnShowFilterSheet(false)
                            viewModel.setOnFilter(Pair(listOf(), 0))
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (viewModel.focused) {
                    SearchHistoryContent(
                        data = searchHistorySuggestion,
                        onSearch = { keyword ->
                            viewModel.setOnSearch(keyword)
                            viewModel.resetFilter()
                            viewModel.setOnFocused(false)
                            val isSearchQueryExist = searchHistories.filter { search ->
                                search.keyword == viewModel.search
                            }

                            if (isSearchQueryExist.isEmpty()) viewModel.saveSearch()
                            viewModel.searchVacancies(
                                query = viewModel.search,
                                filter = VacanciesSearchRequest(
                                    jobType = viewModel.jobTypeFilter,
                                    disability = viewModel.disabilityFilter,
                                    province = viewModel.provinceFilter
                                )
                            )
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        },
                        onDeleteSearch = {
                            viewModel.deleteSearch(it)
                        }
                    )
                } else {
                    val vacancies = viewModel.vacancies.collectAsLazyPagingItems()

                    SearchContent(
                        data = vacancies,
                        navigateToVacancy = {
                            viewModel.setOnFocused(false)
                            navigateToVacancy(it)
                        },
                        lazyListState = lazyListState
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchHistoryContent(
    data: List<Search>,
    onSearch: (String) -> Unit,
    onDeleteSearch: (Int) -> Unit
) {
    LazyColumn {
        items(data.size) { index ->
            TextButton(
                onClick = {
                    onSearch(data[index].keyword)
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
                        text = data[index].keyword,
                        overflow = TextOverflow.Ellipsis,
                        color = Dark,
                        maxLines = 1
                    )
                    IconButton(
                        onClick = {
                            onDeleteSearch(data[index].id)
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

@Composable
private fun SearchContent(
    data: LazyPagingItems<Vacancy>,
    navigateToVacancy: (String) -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumnPaging(
        items = data,
        content = { vacancy ->
            JCard(
                vacancy = vacancy,
                setClick = navigateToVacancy
            )
        },
        lazyListState = lazyListState
    )
}