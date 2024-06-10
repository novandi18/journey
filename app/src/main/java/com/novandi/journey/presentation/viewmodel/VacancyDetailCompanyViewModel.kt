package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.CloseVacancyRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.VacancyDetailCompany
import com.novandi.core.domain.usecase.VacancyUseCase
import com.novandi.utility.data.ConvertUtil
import com.novandi.utility.data.dateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacancyDetailCompanyViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _vacancy = MutableStateFlow<Resource<VacancyDetailCompany>?>(null)
    val vacancy: StateFlow<Resource<VacancyDetailCompany>?> get() = _vacancy

    private val _closeVacancy = MutableStateFlow<Resource<GeneralResult>?>(null)
    val closeVacancy: StateFlow<Resource<GeneralResult>?> get() = _closeVacancy

    val accountId = dataStoreManager.accountId.asLiveData()
    val token = dataStoreManager.token.asLiveData()

    var loading by mutableStateOf(true)
        private set

    var vacancyData by mutableStateOf<VacancyDetailCompany?>(null)
        private set

    var closeLoading by mutableStateOf(true)
        private set

    fun setOnLoading(value: Boolean) {
        loading = value
    }

    fun setOnVacancyData(value: VacancyDetailCompany?) {
        vacancyData = value
    }

    fun setOnDeadlineTime() {
        vacancyData = vacancyData?.copy(
            deadlineTime = dateFormatter(ConvertUtil.getCurrentTimestamp())
        )
    }

    fun setOnCloseLoading(value: Boolean) {
        closeLoading = value
    }

    fun getVacancy(vacancyId: String) {
        viewModelScope.launch {
            vacancyUseCase.getVacancyInCompany(vacancyId)
                .catch { err ->
                    _vacancy.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _vacancy.value = result
                }
        }
    }

    fun closeVacancy(token: String, request: CloseVacancyRequest) {
        viewModelScope.launch {
            vacancyUseCase.closeVacancy(token, request)
                .catch { err ->
                    _closeVacancy.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _closeVacancy.value = result
                }
        }
    }
}