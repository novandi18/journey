package com.novandi.journey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.local.entity.LatestVacancyEntity
import com.novandi.core.data.source.local.entity.PopularVacancyEntity
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobSeekerHomeViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _recommendationVacancies = MutableStateFlow<PagingData<RecommendationVacancyEntity>>(PagingData.empty())
    val recommendationVacancies: StateFlow<PagingData<RecommendationVacancyEntity>> = _recommendationVacancies

    private val _allVacancies = MutableStateFlow<PagingData<AllVacancyEntity>>(PagingData.empty())
    val allVacancies: StateFlow<PagingData<AllVacancyEntity>> = _allVacancies

    private val _latestVacancies = MutableStateFlow<PagingData<LatestVacancyEntity>>(PagingData.empty())
    val latestVacancies: StateFlow<PagingData<LatestVacancyEntity>> = _latestVacancies

    private val _popularVacancies = MutableStateFlow<PagingData<PopularVacancyEntity>>(PagingData.empty())
    val popularVacancies: StateFlow<PagingData<PopularVacancyEntity>> = _popularVacancies

    private val _hasRecommendationFetched = MutableStateFlow(false)
    val hasRecommendationFetched: StateFlow<Boolean> = _hasRecommendationFetched

    private val _hasAllVacanciesFetched = MutableStateFlow(false)
    val hasAllVacanciesFetched: StateFlow<Boolean> = _hasAllVacanciesFetched

    private val _hasLatestVacanciesFetched = MutableStateFlow(false)
    val hasLatestVacanciesFetched: StateFlow<Boolean> = _hasLatestVacanciesFetched

    private val _hasPopularVacanciesFetched = MutableStateFlow(false)
    val hasPopularVacanciesFetched: StateFlow<Boolean> = _hasPopularVacanciesFetched

    fun setHasRecommendationFetched(value: Boolean) {
        _hasRecommendationFetched.value = value
    }

    fun setHasAllVacanciesFetched(value: Boolean) {
        _hasAllVacanciesFetched.value = value
    }

    fun setHasLatestVacanciesFetched(value: Boolean) {
        _hasLatestVacanciesFetched.value = value
    }

    fun setHasPopularVacanciesFetched(value: Boolean) {
        _hasPopularVacanciesFetched.value = value
    }

    fun getRecommendations() {
        viewModelScope.launch {
            combine(
                dataStoreManager.disability,
                dataStoreManager.skillOne,
                dataStoreManager.skillTwo
            ) { disability, skillOne, skillTwo ->
                if (disability != null && skillOne != null && skillTwo != null) {
                    val request = RecommendationRequest(disability, skillOne, skillTwo)
                    vacancyUseCase.getRecommendation(request)
                        .cachedIn(viewModelScope)
                        .collect {
                            _recommendationVacancies.value = it
                        }
                }
            }.collect {}
        }
    }

    fun vacancies() {
        viewModelScope.launch {
            dataStoreManager.token.collect { token ->
                if (token != null) {
                    vacancyUseCase.getVacancies(token)
                        .cachedIn(viewModelScope)
                        .collect {
                            _allVacancies.value = it
                        }
                }
            }
        }
    }

    fun latestVacancies() {
        viewModelScope.launch {
            vacancyUseCase.getLatestVacancies()
                .cachedIn(viewModelScope)
                .collect {
                    _latestVacancies.value = it
                }
        }
    }

    fun popularVacancies() {
        viewModelScope.launch {
            vacancyUseCase.getPopularVacancies()
                .cachedIn(viewModelScope)
                .collect {
                    _popularVacancies.value = it
                }
        }
    }
}