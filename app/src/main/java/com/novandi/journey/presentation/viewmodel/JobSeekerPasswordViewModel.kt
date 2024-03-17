package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.UpdatePasswordRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.usecase.JobSeekerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobSeekerPasswordViewModel @Inject constructor(
    private val jobSeekerUseCase: JobSeekerUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _response = MutableStateFlow<Resource<GeneralResult>?>(null)
    val response: StateFlow<Resource<GeneralResult>?> get() = _response

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var currentPassword by mutableStateOf("")
        private set

    var newPassword by mutableStateOf("")
        private set

    var newPasswordConfirm by mutableStateOf("")
        private set

    fun setOnLoading(value: Boolean) {
        loading = value
    }

    fun setOnEmail(value: String) {
        email = value
    }

    fun setOnCurrentPassword(value: String) {
        currentPassword = value
    }

    fun setOnNewPassword(value: String) {
        newPassword = value
    }

    fun setOnNewPasswordConfirm(value: String) {
        newPasswordConfirm = value
    }

    fun update(token: String, companyId: String, request: UpdatePasswordRequest) {
        viewModelScope.launch {
            jobSeekerUseCase.updateJobSeekerPassword(token, companyId, request)
                .catch { err ->
                    _response.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _response.value = result
                }
        }
    }

    fun validateFields() = email.isNotEmpty() && currentPassword.isNotEmpty()
            && newPassword.isNotEmpty() && newPasswordConfirm.isNotEmpty()

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.setToken("")
            dataStoreManager.setAccountId("")
            dataStoreManager.setRoleId(0)
        }
    }
}