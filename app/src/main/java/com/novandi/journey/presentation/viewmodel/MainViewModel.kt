package com.novandi.journey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.store.DataStoreManager
import com.novandi.journey.presentation.navigation.AuthNavigation
import com.novandi.journey.presentation.navigation.JobProviderNavigation
import com.novandi.journey.presentation.navigation.JobSeekerNavigation
import com.novandi.journey.presentation.navigation.WelcomeNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _startedDestination = MutableStateFlow(WelcomeNavigation.WELCOME_ROUTE)
    val startedDestination: StateFlow<String> = _startedDestination

    val token = dataStoreManager.token.asLiveData()
    val userId = dataStoreManager.accountId.asLiveData()

    init {
        welcomed()
    }

    private fun welcomed() {
        viewModelScope.launch {
            val dataFlow = combine(
                dataStoreManager.isWelcomed,
                dataStoreManager.roleId
            ) { isWelcomed, roleId ->
                if (isWelcomed == true) {
                    when (roleId) {
                        1 -> JobSeekerNavigation.JOB_SEEKER_ROUTE
                        2 -> JobProviderNavigation.JOB_PROVIDER_ROUTE
                        else -> AuthNavigation.AUTH_ROUTE
                    }
                } else WelcomeNavigation.WELCOME_ROUTE
            }
            dataFlow.collect { route ->
                _startedDestination.value = route
            }
        }
    }
}