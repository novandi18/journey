package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderApplicantViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _vacancies = MutableLiveData<Resource<List<Vacancy>>>(null)
    val vacancies: LiveData<Resource<List<Vacancy>>> get() = _vacancies

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var data by mutableStateOf<List<Vacancy>?>(null)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnData(values: List<Vacancy>?) {
        data = values
    }

    fun vacancies(token: String, companyId: String) {
        viewModelScope.launch {
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