package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.consts.Disabilities
import com.novandi.core.consts.JobTypes
import com.novandi.core.consts.Skills
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.usecase.JobProviderUseCase
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderVacancyViewModel @Inject constructor(
    private val vacancyUseCase: VacancyUseCase,
    private val jobProviderUseCase: JobProviderUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _response = MutableStateFlow<Resource<GeneralResult>?>(null)
    val response: StateFlow<Resource<GeneralResult>?> get() = _response

    val jobTypes = JobTypes.types()
    val disabilities = Disabilities.getDisabilities()
    val skills = Skills.getSkills()

    var loading by mutableStateOf(false)
        private set

    var position by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var jobType by mutableIntStateOf(-1)
        private set

    var disability by mutableIntStateOf(-1)
        private set

    var skillOne by mutableIntStateOf(-1)
        private set

    var skillTwo by mutableIntStateOf(-1)
        private set

    var deadline by mutableStateOf("")
        private set

    fun setOnLoading(value: Boolean) {
        loading = value
    }

    fun setOnPosition(value: String) {
        position = value
    }

    fun setOnDescription(value: String) {
        description = value
    }

    fun setOnJobType(value: Int) {
        jobType = value
    }

    fun setOnDisability(value: Int) {
        disability = value
    }

    fun setOnSkillOne(value: Int) {
        skillOne = value
    }

    fun setOnSkillTwo(value: Int) {
        skillTwo = value
    }

    fun setOnDeadline(value: String) {
        deadline = value
    }

    val token = dataStoreManager.token.asLiveData()
    val accountId = dataStoreManager.accountId.asLiveData()

    fun addVacancy(token: String, companyId: String, request: VacancyRequest) {
        viewModelScope.launch {
            vacancyUseCase.addVacancy(token, companyId, request)
                .catch { err ->
                    _response.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _response.value = result
                }
        }
    }

    fun updateVacancy(companyId: String, vacancyId: String, request: VacancyRequest) {
        viewModelScope.launch {
            jobProviderUseCase.updateVacancy(companyId, vacancyId, request)
                .catch { err ->
                    _response.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _response.value = result
                }
        }
    }

    fun validateFields(): Boolean = position.isNotEmpty() && description.isNotEmpty() &&
            deadline.isNotEmpty() && jobType != -1 && disability != -1 && skillOne != -1 && skillTwo != -1
}