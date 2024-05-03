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
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.core.domain.model.UpdateCvResult
import com.novandi.core.domain.model.UpdateProfilePhotoResult
import com.novandi.core.domain.usecase.JobSeekerUseCase
import com.novandi.journey.presentation.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class JobSeekerProfileViewModel @Inject constructor(
    private val jobSeekerUseCase: JobSeekerUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _profile = MutableStateFlow<Resource<ProfileJobSeeker>?>(null)
    val profile: StateFlow<Resource<ProfileJobSeeker>?> get() = _profile

    private val _photoProfile = MutableStateFlow<Resource<UpdateProfilePhotoResult>?>(null)
    val photoProfile: StateFlow<Resource<UpdateProfilePhotoResult>?> get() = _photoProfile

    private val _cv = MutableStateFlow<Resource<UpdateCvResult>?>(null)
    val cv: StateFlow<Resource<UpdateCvResult>?> get() = _cv

    private val _downloadedCv = MutableStateFlow<LiveData<WorkInfo>?>(null)
    val downloadedCv: StateFlow<LiveData<WorkInfo>?> get() = _downloadedCv

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(true)
        private set

    var profileData by mutableStateOf<ProfileJobSeeker?>(null)
        private set

    var uploadLoading by mutableStateOf(false)
        private set

    var uploadCvLoading by mutableStateOf(false)
        private set

    var openDialogImagePreview by mutableStateOf(false)
        private set

    var cvDownloadShowing by mutableStateOf(false)
        private set

    var cvFile by mutableStateOf<File?>(null)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnProfileData(value: ProfileJobSeeker?) {
        profileData = value
    }

    fun setOnUploadLoading(isLoading: Boolean) {
        uploadLoading = isLoading
    }

    fun setOnOpenDialogImagePreview(open: Boolean) {
        openDialogImagePreview = open
    }

    fun setOnUploadCvLoading(isLoading: Boolean) {
        uploadCvLoading = isLoading
    }

    fun updateCvOnProfileData(cv: String?) {
        profileData = profileData?.copy(cv = cv)
    }

    fun resetPhotoProfileState() {
        _photoProfile.value = null
    }

    fun resetCvState() {
        _cv.value = null
    }

    fun setOnCvDownloadShowing(value: Boolean) {
        cvDownloadShowing = value
    }

    fun setOnCvFile(value: File) {
        cvFile = value
    }

    fun resetDownloadedCvState() {
        _downloadedCv.value = null
    }

    fun setOnUpdateFile(downloadedUri: String? = null, isDownloading: Boolean) {
        cvFile = cvFile?.copy(
            isDownloading = isDownloading,
            downloadedUri = downloadedUri
        )
    }

    fun getProfile(token: String, userId: String) {
        viewModelScope.launch {
            jobSeekerUseCase.getJobSeeker(token, userId)
                .catch { err ->
                    _profile.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _profile.value = result
                }
        }
    }

    fun updatePhotoProfile(userId: String, photo: MultipartBody.Part) {
        viewModelScope.launch {
            jobSeekerUseCase.updateJobSeekerPhoto(userId, photo)
                .catch { err ->
                    _photoProfile.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _photoProfile.value = result
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

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.setToken("")
            dataStoreManager.setAccountId("")
            dataStoreManager.setRoleId(0)
            dataStoreManager.setDisability("")
            dataStoreManager.setSkillOne("")
            dataStoreManager.setSkillTwo("")
        }
    }

    fun downloadCv(file: File) {
        MainActivity().fileDownloadStarter(
            file = file,
            running = { state ->
                _downloadedCv.value = state
            }
        )
    }
}