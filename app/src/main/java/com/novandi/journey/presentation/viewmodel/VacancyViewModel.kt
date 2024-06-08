package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.novandi.core.data.response.Resource
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.File
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.UpdateCvResult
import com.novandi.core.domain.model.VacancyDetailUser
import com.novandi.core.domain.usecase.JobSeekerUseCase
import com.novandi.core.domain.usecase.VacancyUseCase
import com.novandi.journey.presentation.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class VacancyViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    private val jobSeekerUseCase: JobSeekerUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _vacancy = MutableStateFlow<Resource<VacancyDetailUser>?>(null)
    val vacancy: StateFlow<Resource<VacancyDetailUser>?> get() = _vacancy

    private val _applyResult = MutableStateFlow<Resource<GeneralResult>?>(null)
    val applyResult: StateFlow<Resource<GeneralResult>?> get() = _applyResult

    private val _cv = MutableStateFlow<Resource<UpdateCvResult>?>(null)
    val cv: StateFlow<Resource<UpdateCvResult>?> get() = _cv

    private val _downloadedCv = MutableStateFlow<LiveData<WorkInfo>?>(null)
    val downloadedCv: StateFlow<LiveData<WorkInfo>?> get() = _downloadedCv

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(true)
        private set

    var applyLoading by mutableStateOf(false)
        private set

    var vacancyData by mutableStateOf<VacancyDetailUser?>(null)
        private set

    var uploadCvLoading by mutableStateOf(false)
        private set

    var cvDownloadShowing by mutableStateOf(false)
        private set

    var cvFile by mutableStateOf<File?>(null)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnApplyLoading(isLoading: Boolean) {
        applyLoading = isLoading
    }

    fun setOnVacancyData(data: VacancyDetailUser?) {
        vacancyData = data
    }

    fun resetVacancyState() {
        _vacancy.value = null
    }

    fun resetApplyResultState() {
        _applyResult.value = null
    }

    fun setOnUploadCvLoading(isLoading: Boolean) {
        uploadCvLoading = isLoading
    }

    fun updateCvOnVacancyData(cv: String?) {
        vacancyData = vacancyData?.copy(userCv = cv)
    }

    fun resetCvState() {
        _cv.value = null
    }

    fun setOnCvDownloadShowing(value: Boolean) {
        cvDownloadShowing = value
    }

    fun setOnUpdateStatusApply(status: String) {
        vacancyData = vacancyData?.copy(statusApply = status)
    }

    fun resetDownloadedCvState() {
        _downloadedCv.value = null
    }

    fun setOnCvFile(value: File) {
        cvFile = value
    }

    fun setOnUpdateFile(downloadedUri: String? = null, isDownloading: Boolean) {
        cvFile = cvFile?.copy(
            isDownloading = isDownloading,
            downloadedUri = downloadedUri
        )
    }

    fun getVacancy(vacancyId: String, userId: String) {
        viewModelScope.launch {
            vacancyUseCase.getVacancyInUser(vacancyId, userId)
                .catch { err ->
                    _vacancy.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _vacancy.value = result
                }
        }
    }

    fun applyVacancy(token: String, userId: String, vacancyId: String) {
        viewModelScope.launch {
            jobSeekerUseCase.postJobApply(token, userId, vacancyId)
                .catch { err ->
                    _applyResult.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _applyResult.value = result
                }
        }
    }

    fun updateCv(userId: String, cv: MultipartBody.Part) {
        viewModelScope.launch {
            jobSeekerUseCase.updateJobSeekerCv(userId, cv)
                .catch { err ->
                    _cv.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _cv.value = result
                }
        }
    }

    fun downloadCv(file: File) {
        val fileCopy = file.copy(
            id = file.id + "2"
        )
        MainActivity().fileDownloadStarter(
            file = fileCopy,
            running = { state ->
                _downloadedCv.value = state
            }
        )
    }
}