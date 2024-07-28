package com.novandi.journey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderHomeViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _vacancies = MutableStateFlow<Resource<List<Vacancy>>>(Resource.Loading())
    val vacancies: StateFlow<Resource<List<Vacancy>>> get() = _vacancies.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val accountId: StateFlow<String?> = dataStoreManager.accountId.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    init {
        viewModelScope.launch {
            combine(
                dataStoreManager.accountId,
                dataStoreManager.token
            ) { accountId, token -> accountId to token }
                .filter { (accountId, token) -> accountId != null && token != null }
                .collectLatest { (accountId, token) ->
                    vacancies(token, accountId)
                }
        }
    }

    fun vacancies(token: String?, companyId: String?) {
        viewModelScope.launch {
            if (token != null && companyId != null) {
                vacancyUseCase.getJobProviderVacancies(token, companyId)
                    .catch { err ->
                        _vacancies.value = Resource.Error(err.message.toString())
                    }
                    .collect { result ->
                        _vacancies.value = result
                    }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true

            val token = dataStoreManager.token.firstOrNull()
            val accountId = dataStoreManager.accountId.firstOrNull()
            if (token != null && accountId != null) {
                vacancies(token, accountId)
            }

            _isRefreshing.value = false
        }
    }
}