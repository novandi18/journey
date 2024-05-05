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
class JobProviderSearchViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _vacancies = MutableLiveData<Resource<List<Vacancy>>>(null)
    val vacancies: LiveData<Resource<List<Vacancy>>> get() = _vacancies

    val token = dataStoreManager.token.asLiveData()

    var loading by mutableStateOf(true)
        private set

    var data by mutableStateOf<List<Vacancy>?>(null)
        private set

    var filteredData by mutableStateOf<List<Vacancy>>(listOf())
        private set

    var query by mutableStateOf("")
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnData(values: List<Vacancy>?) {
        data = values
    }

    fun setOnQuery(value: String) {
        query = value
    }

    fun setOnFilteredData(values: List<Vacancy>) {
        filteredData = values
    }

    fun getVacancies(token: String, companyId: String) {
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