package com.novandi.journey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.Search
import com.novandi.core.domain.usecase.SearchUseCase
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class JobSeekerHomeViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    private val searchUseCase: SearchUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    val searches = searchUseCase.getSearch().asLiveData()
    val token = dataStoreManager.token.asLiveData()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isDoSearching = MutableStateFlow(false)
    val isDoSearching = _isDoSearching.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    fun onToggleDoSearch(isDoSearch: Boolean) {
        _isDoSearching.value = isDoSearch
    }

    fun saveSearch() = searchUseCase.saveSearch(Search(keyword = searchText.value))

    fun deleteSearch(id: Int) = searchUseCase.deleteSearch(id)

    fun vacancies(token: String) = vacancyUseCase.getVacancies(token).cachedIn(viewModelScope)

    fun latestVacancies() = vacancyUseCase.getLatestVacancies().cachedIn(viewModelScope)

    fun popularVacancies() = vacancyUseCase.getPopularVacancies().cachedIn(viewModelScope)

    fun searchVacancies(query: String) = vacancyUseCase.searchVacancy(query).cachedIn(viewModelScope)
}