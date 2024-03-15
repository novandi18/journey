package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.usecase.JobProviderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderProfileViewModel @Inject constructor(
    private val jobProviderUseCase: JobProviderUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _profile = MutableStateFlow<Resource<ProfileJobProvider>?>(null)
    val profile: StateFlow<Resource<ProfileJobProvider>?> get() = _profile

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var profileData by mutableStateOf<ProfileJobProvider?>(null)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnProfileData(value: ProfileJobProvider?) {
        profileData = value
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

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.setToken("")
            dataStoreManager.setAccountId("")
            dataStoreManager.setRoleId(0)
        }
    }
}