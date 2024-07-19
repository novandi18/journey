package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.AcceptApplicantRequest
import com.novandi.core.data.source.remote.request.CloseVacancyRequest
import com.novandi.core.data.source.remote.request.MessagingRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.Applicant
import com.novandi.core.domain.model.ApplicantItemStatus
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.VacancyDetailCompany
import com.novandi.core.domain.model.WhatsappResult
import com.novandi.core.domain.usecase.JobProviderUseCase
import com.novandi.core.domain.usecase.MessagingUseCase
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderApplicantDetailViewModel @Inject constructor(
    private val jobProviderUseCase: JobProviderUseCase,
    private val vacancyUseCase: VacancyUseCase,
    private val messagingUseCase: MessagingUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _applicants = MutableLiveData<Resource<List<Applicant>>>(Resource.Loading())
    val applicants: LiveData<Resource<List<Applicant>>> get() = _applicants

    private val _response = MutableStateFlow<Resource<GeneralResult>?>(null)
    val response: StateFlow<Resource<GeneralResult>?> get() = _response

    private val _whatsappResponse = MutableStateFlow<Resource<WhatsappResult>?>(null)
    val whatsappResponse: StateFlow<Resource<WhatsappResult>?> get() = _whatsappResponse

    private val _vacancy = MutableLiveData<Resource<VacancyDetailCompany>>(Resource.Loading())
    val vacancy: LiveData<Resource<VacancyDetailCompany>> get() = _vacancy

    private val _close = MutableStateFlow<Resource<GeneralResult>?>(null)
    val close: StateFlow<Resource<GeneralResult>?> get() = _close

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(true)
        private set

    var responseLoading by mutableStateOf<List<Pair<String, Boolean>>>(emptyList())
        private set

    var data by mutableStateOf<List<Applicant>?>(null)
        private set

    var done by mutableStateOf<List<ApplicantItemStatus>>(emptyList())
        private set

    var vacancyData by mutableStateOf<VacancyDetailCompany?>(null)
        private set

    var applicantWhatsappNumber by mutableStateOf<Pair<String, Boolean>?>(null)
        private set

    var closeVacancyStatus by mutableIntStateOf(2) // 0 = default, 1 = loading, 2 = success
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

    fun setOnVacancyData(value: VacancyDetailCompany?) {
        vacancyData = value
    }

    fun setOnApplicantWhatsappNumber(value: String, isAccept: Boolean) {
        applicantWhatsappNumber = Pair(value, isAccept)
    }

    fun setOnRemoveApplicantWhatsappNumber() {
        applicantWhatsappNumber = null
    }

    private fun setUpdateNotesOnData(applicant: Applicant, note: String) {
        val applicantIndex = data?.indexOf(applicant)
        if (applicantIndex != null) data?.get(applicantIndex)?.notes = note
    }

    fun resetCloseState() {
        _close.value = null
    }

    fun setOnCloseVacancyStatus(status: Int) {
        closeVacancyStatus = status
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

    fun acceptApplicant(
        token: String, companyId: String, vacancyId: String, applicant: Applicant,
        request: AcceptApplicantRequest
    ) {
        viewModelScope.launch {
            jobProviderUseCase.postAcceptApplicants(
                token, companyId, vacancyId, applicant.id, request
            )
                .catch { err ->
                    _response.value = Resource.Error(err.message.toString())
                    setOnRemoveResponseLoading(applicant.id)
                }
                .collect { result ->
                    _response.value = result
                    setOnDone(
                        ApplicantItemStatus(applicant.id, true)
                    )
                    setOnRemoveResponseLoading(applicant.id)
                    setUpdateNotesOnData(applicant, request.notes)
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

    fun getVacancyById(vacancyId: String) {
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

    fun sendWhatsappMessage(phoneNumber: String, message: String) {
        viewModelScope.launch {
            vacancyUseCase.sendWhatsappMessage(phoneNumber, message)
                .catch { err ->
                    _whatsappResponse.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _whatsappResponse.value = result
                }
        }
    }

    fun closeVacancy(token: String, request: CloseVacancyRequest) {
        viewModelScope.launch {
            vacancyUseCase.closeVacancy(token, request)
                .catch { err ->
                    _close.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _close.value = result
                }
        }
    }

    fun sendNotification(request: MessagingRequest) {
        viewModelScope.launch {
            messagingUseCase.sendNotification(request)
        }
    }
}