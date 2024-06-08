package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.VacancyDetailCompany
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacancyDetailCompanyViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase
): ViewModel() {
    private val _vacancy = MutableStateFlow<Resource<VacancyDetailCompany>?>(null)
    val vacancy: StateFlow<Resource<VacancyDetailCompany>?> get() = _vacancy

    var loading by mutableStateOf(true)
        private set

    var vacancyData by mutableStateOf<VacancyDetailCompany?>(null)
        private set

    fun setOnLoading(value: Boolean) {
        loading = value
    }

    fun setOnVacancyData(value: VacancyDetailCompany?) {
        vacancyData = value
    }

    fun getVacancy(vacancyId: String) {
        viewModelScope.launch {
            vacancyUseCase.getVacancyInCompany(vacancyId)
                .catch { err ->
                    _vacancy.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _vacancy.value = result
                }
        }
    }
}