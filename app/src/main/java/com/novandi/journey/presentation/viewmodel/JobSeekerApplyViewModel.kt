package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.JobApplyStatus
import com.novandi.core.domain.usecase.JobSeekerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobSeekerApplyViewModel @Inject constructor(
    private val jsSeekerUseCase: JobSeekerUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _vacancies = MutableStateFlow<Resource<List<JobApplyStatus>>?>(null)
    val vacancies: StateFlow<Resource<List<JobApplyStatus>>?> get() = _vacancies

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var vacanciesData by mutableStateOf<List<JobApplyStatus>?>(null)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnVacanciesData(values: List<JobApplyStatus>?) {
        vacanciesData = values
    }

    fun getVacancies(token: String, userId: String) {
        viewModelScope.launch {
            jsSeekerUseCase.getApplyStatus(token, userId)
                .catch { err ->
                    _vacancies.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _vacancies.value = result
                }
        }
    }
}