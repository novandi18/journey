package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.local.entity.LatestVacancyEntity
import com.novandi.core.data.source.local.entity.PopularVacancyEntity
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.request.RecommendationVacanciesRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobSeekerHomeViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _recommendationVacancies = MutableStateFlow<PagingData<RecommendationVacancyEntity>>(PagingData.empty())
    val recommendationVacancies: StateFlow<PagingData<RecommendationVacancyEntity>> = _recommendationVacancies

    private val _allVacancies = MutableStateFlow<PagingData<AllVacancyEntity>>(PagingData.empty())
    val allVacancies: StateFlow<PagingData<AllVacancyEntity>> = _allVacancies

    private val _latestVacancies = MutableStateFlow<PagingData<LatestVacancyEntity>>(PagingData.empty())
    val latestVacancies: StateFlow<PagingData<LatestVacancyEntity>> = _latestVacancies

    private val _popularVacancies = MutableStateFlow<PagingData<PopularVacancyEntity>>(PagingData.empty())
    val popularVacancies: StateFlow<PagingData<PopularVacancyEntity>> = _popularVacancies

    val token = dataStoreManager.token.asLiveData()
    val disability = dataStoreManager.disability.asLiveData()
    val skillOne = dataStoreManager.skillOne.asLiveData()
    val skillTwo = dataStoreManager.skillTwo.asLiveData()

    private val _recommendations = MutableStateFlow<Resource<List<String>>?>(null)
    val recommendations: StateFlow<Resource<List<String>>?> get() = _recommendations

    var recommendation by mutableStateOf<List<String>?>(null)
        private set

    var recommendationLoading by mutableStateOf(true)
        private set

    fun setOnRecommendations(recommendations: List<String>) {
        recommendation = recommendations
    }

    fun resetRecommendationState() {
        _recommendations.value = null
    }

    fun setOnRecommendationLoading(isLoading: Boolean) {
        recommendationLoading = isLoading
    }

    fun vacancies(token: String) {
        viewModelScope.launch {
            vacancyUseCase.getVacancies(token)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _allVacancies.value = it
                }
        }
    }

    fun latestVacancies() {
        viewModelScope.launch {
            vacancyUseCase.getLatestVacancies()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _latestVacancies.value = it
                }
        }
    }

    fun popularVacancies() {
        viewModelScope.launch {
            vacancyUseCase.getPopularVacancies()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _popularVacancies.value = it
                }
        }
    }

    fun recommendationVacancies(recommendations: RecommendationVacanciesRequest) {
        viewModelScope.launch {
            vacancyUseCase.getRecommendationVacancies(recommendations)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _recommendationVacancies.value = it
                }
        }
    }

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