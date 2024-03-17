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
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.model.UpdateProfilePhotoResult
import com.novandi.core.domain.usecase.JobProviderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class JobProviderProfileViewModel @Inject constructor(
    private val jobProviderUseCase: JobProviderUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _profile = MutableStateFlow<Resource<ProfileJobProvider>?>(null)
    val profile: StateFlow<Resource<ProfileJobProvider>?> get() = _profile

    private var _profileData = MutableLiveData<ProfileJobProvider?>(null)
    var profileData: LiveData<ProfileJobProvider?> = _profileData

    private val _logoResponse = MutableStateFlow<Resource<UpdateProfilePhotoResult>?>(null)
    val logoResponse: StateFlow<Resource<UpdateProfilePhotoResult>?> get() = _logoResponse

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var uploadLoading by mutableStateOf(false)
        private set

    var openDialogImagePreview by mutableStateOf(false)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnProfileData(value: ProfileJobProvider) {
        _profileData.value = value
    }

    fun setOnUploadLoading(isLoading: Boolean) {
        uploadLoading = isLoading
    }

    fun setOnOpenDialogImagePreview(open: Boolean) {
        openDialogImagePreview = open
    }

    fun getProfile(token: String, userId: String) {
        viewModelScope.launch {
            jobProviderUseCase.getJobProvider(token, userId)
                .catch { err ->
                    _profile.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _profile.value = result
                }
        }
    }

    fun updateLogo(userId: String, logo: MultipartBody.Part) {
        viewModelScope.launch {
            jobProviderUseCase.updateJobProviderLogo(userId, logo)
                .catch { err ->
                    _logoResponse.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _logoResponse.value = result
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