package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.core.domain.model.UpdateProfilePhotoResult
import com.novandi.core.domain.usecase.JobSeekerUseCase
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

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var profileData by mutableStateOf<ProfileJobSeeker?>(null)
        private set

    var uploadLoading by mutableStateOf(false)
        private set

    var openDialogImagePreview by mutableStateOf(false)
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

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.setToken("")
            dataStoreManager.setAccountId("")
            dataStoreManager.setRoleId(0)
        }
    }
}