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
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.usecase.JobSeekerUseCase
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobSeekerApplyDetailViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    private val jobSeekerUseCase: JobSeekerUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _vacancy = MutableStateFlow<Resource<Vacancy>?>(null)
    val vacancy: StateFlow<Resource<Vacancy>?> get() = _vacancy

    private val _applies = MutableStateFlow<Resource<List<JobApplyStatus>>?>(null)
    val applies: StateFlow<Resource<List<JobApplyStatus>>?> get() = _applies

    val accountId = dataStoreManager.accountId.asLiveData()
    val token = dataStoreManager.token.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var statusLoading by mutableStateOf(false)
        private set

    var vacancyData by mutableStateOf<Vacancy?>(null)
        private set

    var vacancyStatusData by mutableStateOf<List<JobApplyStatus>?>(null)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnStatusLoading(isLoading: Boolean) {
        statusLoading = isLoading
    }

    fun setOnVacancyData(data: Vacancy?) {
        vacancyData = data
    }

    fun setOnVacancyStatusData(data: List<JobApplyStatus>?) {
        vacancyStatusData = data
    }

    fun resetVacancyState() {
        _vacancy.value = null
    }

    fun resetVacancyStatusState() {
        _applies.value = null
    }

    fun getVacancy(id: String) {
        viewModelScope.launch {
            vacancyUseCase.getVacancy(id)
                .catch { err ->
                    _vacancy.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _vacancy.value = result
                }
        }
    }

    fun getVacancyStatus(token: String, userId: String) {
        viewModelScope.launch {
            jobSeekerUseCase.getApplyStatus(token, userId)
                .catch { err ->
                    _applies.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _applies.value = result
                }
        }
    }
}