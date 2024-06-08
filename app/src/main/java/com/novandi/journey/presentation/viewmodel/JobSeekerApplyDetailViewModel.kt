package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.VacancyDetailUser
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobSeekerApplyDetailViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _vacancy = MutableStateFlow<Resource<VacancyDetailUser>?>(null)
    val vacancy: StateFlow<Resource<VacancyDetailUser>?> get() = _vacancy

    val accountId = dataStoreManager.accountId.asLiveData()
    val token = dataStoreManager.token.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var data by mutableStateOf<VacancyDetailUser?>(null)
        private set

    fun setOnLoading(isLoading: Boolean) {
        loading = isLoading
    }

    fun setOnData(value: VacancyDetailUser) {
        data = value
    }

    fun resetVacancyState() {
        _vacancy.value = null
    }

    fun getVacancy(vacancyId: String, userId: String) {
        viewModelScope.launch {
            vacancyUseCase.getVacancyInUser(vacancyId, userId)
                .catch { err ->
                    _vacancy.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _vacancy.value = result
                }
        }
    }
}