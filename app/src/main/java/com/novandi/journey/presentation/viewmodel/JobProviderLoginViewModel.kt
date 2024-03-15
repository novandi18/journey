package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.LoginRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.LoginResult
import com.novandi.core.domain.usecase.JobProviderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderLoginViewModel @Inject constructor(
    private val jobProviderUseCase: JobProviderUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _auth = MutableStateFlow<Resource<LoginResult>?>(null)
    val auth: StateFlow<Resource<LoginResult>?> get() = _auth

    var isLoading by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun setIsLoading(loading: Boolean) {
        isLoading = loading
    }

    fun setOnEmail(value: String) {
        email = value
    }

    fun setOnPassword(value: String) {
        password = value
    }

    fun setRoleId() {
        viewModelScope.launch {
            dataStoreManager.setRoleId(2)
        }
    }

    fun setAccountId(accountId: String) {
        viewModelScope.launch {
            dataStoreManager.setAccountId(accountId)
        }
    }

    fun setToken(token: String) {
        viewModelScope.launch {
            dataStoreManager.setToken(token)
        }
    }

    fun login() {
        viewModelScope.launch {
            jobProviderUseCase.loginJobProvider(LoginRequest(email, password))
                .catch { err ->
                    _auth.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _auth.value = result
                }
        }
    }

    fun clearErrorMessage() {
        _auth.value = null
    }
}