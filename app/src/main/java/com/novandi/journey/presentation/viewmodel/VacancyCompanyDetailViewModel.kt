package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacancyCompanyDetailViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase
): ViewModel() {
    private val _profile = MutableStateFlow<Resource<ProfileJobProvider>?>(null)
    val profile: StateFlow<Resource<ProfileJobProvider>?> get() = _profile

    private var _profileData = MutableStateFlow<ProfileJobProvider?>(null)
    var profileData: StateFlow<ProfileJobProvider?> = _profileData

    var loading by mutableStateOf(true)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnProfileData(value: ProfileJobProvider) {
        _profileData.value = value
    }

    fun resetState() {
        _profile.value = null
    }

    fun getProfile(companyId: String) {
        viewModelScope.launch {
            vacancyUseCase.getVacancyCompany(companyId)
                .catch { err ->
                    _profile.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _profile.value = result
                }
        }
    }
}