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
import com.novandi.core.domain.usecase.JobProviderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderApplicantProfileViewModel @Inject constructor(
    private val jobProviderUseCase: JobProviderUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _profile = MutableStateFlow<Resource<ProfileJobSeeker>?>(null)
    val profile: StateFlow<Resource<ProfileJobSeeker>?> get() = _profile

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var profileData by mutableStateOf<ProfileJobSeeker?>(null)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnProfileData(value: ProfileJobSeeker?) {
        profileData = value
    }

    fun resetState() {
        _profile.value = null
    }

    fun getApplicant(token: String, companyId: String, applicantId: String) {
        viewModelScope.launch {
            jobProviderUseCase.getApplicantById(token, companyId, applicantId)
                .catch { err ->
                    _profile.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _profile.value = result
                }
        }
    }
}