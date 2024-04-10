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
import com.novandi.core.domain.model.Applicant
import com.novandi.core.domain.model.ApplicantItemStatus
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.usecase.JobProviderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderApplicantDetailViewModel @Inject constructor(
    private val jobProviderUseCase: JobProviderUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _applicants = MutableLiveData<Resource<List<Applicant>>>(Resource.Loading())
    val applicants: LiveData<Resource<List<Applicant>>> get() = _applicants

    private val _response = MutableStateFlow<Resource<GeneralResult>?>(null)
    val response: StateFlow<Resource<GeneralResult>?> get() = _response

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var responseLoading by mutableStateOf<List<Pair<String, Boolean>>>(emptyList())
        private set

    var data by mutableStateOf<List<Applicant>?>(null)
        private set

    var done by mutableStateOf<List<ApplicantItemStatus>>(emptyList())
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnResponseLoading(applicantId: String, status: Boolean) {
        responseLoading = responseLoading + listOf(Pair(applicantId, status))
    }

    private fun setOnRemoveResponseLoading(applicantId: String) {
        responseLoading = responseLoading.filter { it.first != applicantId }
    }


    fun setOnData(values: List<Applicant>?) {
        data = values
    }

    private fun setOnDone(applicantItemStatus: ApplicantItemStatus) {
        done = done + listOf(applicantItemStatus)
    }

    fun getApplicants(token: String, companyId: String, vacancyId: String) {
        viewModelScope.launch {
            jobProviderUseCase.getApplicants(token, companyId, vacancyId)
                .catch { err ->
                    _applicants.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _applicants.value = result
                }
        }
    }

    fun acceptApplicant(token: String, companyId: String, vacancyId: String, applicantId: String) {
        viewModelScope.launch {
            jobProviderUseCase.postAcceptApplicants(token, companyId, vacancyId, applicantId)
                .catch { err ->
                    _response.value = Resource.Error(err.message.toString())
                    setOnRemoveResponseLoading(applicantId)
                }
                .collect { result ->
                    _response.value = result
                    setOnDone(
                        ApplicantItemStatus(applicantId, true)
                    )
                    setOnRemoveResponseLoading(applicantId)
                }
        }
    }

    fun rejectApplicant(token: String, companyId: String, vacancyId: String, applicantId: String) {
        viewModelScope.launch {
            jobProviderUseCase.postRejectApplicants(token, companyId, vacancyId, applicantId)
                .catch { err ->
                    _response.value = Resource.Error(err.message.toString())
                    setOnRemoveResponseLoading(applicantId)
                }
                .collect { result ->
                    _response.value = result
                    setOnDone(
                        ApplicantItemStatus(applicantId, false)
                    )
                    setOnRemoveResponseLoading(applicantId)
                }
        }
    }
}