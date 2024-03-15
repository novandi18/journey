package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.UpdateEmailRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.usecase.JobProviderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderEmailViewModel @Inject constructor(
    private val jobProviderUseCase: JobProviderUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _response = MutableStateFlow<Resource<GeneralResult>?>(null)
    val response: StateFlow<Resource<GeneralResult>?> get() = _response

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var currentEmail by mutableStateOf("")
        private set

    var newEmail by mutableStateOf("")
        private set

    var currentPassword by mutableStateOf("")
        private set

    var currentPasswordConfirm by mutableStateOf("")
        private set

    fun setOnLoading(value: Boolean) {
        loading = value
    }

    fun setOnCurrentEmail(value: String) {
        currentEmail = value
    }

    fun setOnNewEmail(value: String) {
        newEmail = value
    }

    fun setOnCurrentPassword(value: String) {
        currentPassword = value
    }

    fun setOnCurrentPasswordConfirm(value: String) {
        currentPasswordConfirm = value
    }

    fun update(token: String, companyId: String, request: UpdateEmailRequest) {
        viewModelScope.launch {
            jobProviderUseCase.updateJobProviderEmail(token, companyId, request)
                .catch { err ->
                    _response.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _response.value = result
                }
        }
    }

    fun validateFields() = currentEmail.isNotEmpty() && newEmail.isNotEmpty()
            && currentPassword.isNotEmpty() && currentPasswordConfirm.isNotEmpty()

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.setToken("")
            dataStoreManager.setAccountId("")
            dataStoreManager.setRoleId(0)
        }
    }
}