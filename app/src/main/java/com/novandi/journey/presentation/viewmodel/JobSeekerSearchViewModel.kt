package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.novandi.core.data.source.remote.request.VacanciesSearchRequest
import com.novandi.core.domain.model.Search
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.usecase.SearchUseCase
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobSeekerSearchViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    private val searchUseCase: SearchUseCase
): ViewModel() {
    private val _vacancies = MutableStateFlow<PagingData<Vacancy>>(PagingData.empty())
    val vacancies: StateFlow<PagingData<Vacancy>> = _vacancies

    val searches = searchUseCase.getSearch().asLiveData()

    var search by mutableStateOf("")
        private set

    var focused by mutableStateOf(true)
        private set

    var filter by mutableStateOf(Pair(listOf<String>(), 0))
        private set

    var showFilterSheet by mutableStateOf(false)
        private set

    var jobTypeFilter by mutableStateOf("Semua")
        private set

    var disabilityFilter by mutableStateOf("Semua")
        private set

    var provinceFilter by mutableStateOf("Semua")
        private set

    var filterTabSelected by mutableIntStateOf(0)
        private set

    fun setOnSearch(value: String) {
        search = value
    }

    fun setOnFocused(value: Boolean) {
        focused = value
    }

    fun setOnFilter(value: Pair<List<String>, Int>) {
        filter = value
    }

    fun setOnShowFilterSheet(value: Boolean) {
        showFilterSheet = value
    }

    fun setOnJobTypeFilter(filter: String) {
        jobTypeFilter = filter
    }

    fun setOnDisabilityFilter(filter: String) {
        disabilityFilter = filter
    }

    fun setOnProvinceFilter(filter: String) {
        provinceFilter = filter
    }

    fun resetFilter() {
        filter = Pair(listOf(), 0)
        jobTypeFilter = "Semua"
        disabilityFilter = "Semua"
        provinceFilter = "Semua"
    }

    fun saveSearch() = searchUseCase.saveSearch(Search(keyword = search))

    fun deleteSearch(id: Int) = searchUseCase.deleteSearch(id)

    fun searchVacancies(query: String, filter: VacanciesSearchRequest) {
        viewModelScope.launch {
            vacancyUseCase.searchVacancy(query, filter)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _vacancies.value = it
                }
        }
    }

    fun clearSearchVacancies() {
        _vacancies.value = PagingData.empty()
    }
}