package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.request.RecommendationVacanciesRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.Search
import com.novandi.core.domain.usecase.SearchUseCase
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobSeekerHomeViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    private val searchUseCase: SearchUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    val searches = searchUseCase.getSearch().asLiveData()
    val token = dataStoreManager.token.asLiveData()
    val disability = dataStoreManager.disability.asLiveData()
    val skillOne = dataStoreManager.skillOne.asLiveData()
    val skillTwo = dataStoreManager.skillTwo.asLiveData()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isDoSearching = MutableStateFlow(false)
    val isDoSearching = _isDoSearching.asStateFlow()

    private val _recommendations = MutableStateFlow<Resource<List<String>>?>(null)
    val recommendations: StateFlow<Resource<List<String>>?> get() = _recommendations

    var recommendation by mutableStateOf<List<String>?>(null)
        private set

    var recommendationLoading by mutableStateOf(true)
        private set

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

    fun setOnRecommendations(recommendations: List<String>) {
        recommendation = recommendations
    }

    fun resetRecommendationState() {
        _recommendations.value = null
    }

    fun setOnRecommendationLoading(isLoading: Boolean) {
        recommendationLoading = isLoading
    }

    fun saveSearch() = searchUseCase.saveSearch(Search(keyword = searchText.value))

    fun deleteSearch(id: Int) = searchUseCase.deleteSearch(id)

    fun vacancies(token: String) = vacancyUseCase.getVacancies(token).cachedIn(viewModelScope)

    fun latestVacancies() = vacancyUseCase.getLatestVacancies().cachedIn(viewModelScope)

    fun popularVacancies() = vacancyUseCase.getPopularVacancies().cachedIn(viewModelScope)

    fun searchVacancies(query: String) = vacancyUseCase.searchVacancy(query).cachedIn(viewModelScope)

    fun recommendationVacancies(recommendations: RecommendationVacanciesRequest) =
        vacancyUseCase.getRecommendationVacancies(recommendations).cachedIn(viewModelScope)

    fun getRecommendations(request: RecommendationRequest) {
        viewModelScope.launch {
            vacancyUseCase.getRecommendation(request)
                .catch {
                    _recommendations.value = Resource.Error(it.message.toString())
                }
                .collect {
                    _recommendations.value = it
                }
        }
    }
}