package com.novandi.journey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _vacancies = MutableStateFlow<Resource<List<Vacancy>>?>(null)
    val vacancies: StateFlow<Resource<List<Vacancy>>?> get() = _vacancies

    fun setWelcomed(isWelcomed: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setIsWelcome(isWelcomed)
        }
    }

    fun vacancies() {
        viewModelScope.launch {
            vacancyUseCase.getVacanciesWithoutPager()
                .catch { err ->
                    _vacancies.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _vacancies.value = result
                }
        }
    }
}