package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.consts.Disabilities
import com.novandi.core.consts.Skills
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobSeekerEditRequest
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
class JobSeekerEditViewModel @Inject constructor(
    private val jobSeekerUseCase: JobSeekerUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _response = MutableStateFlow<Resource<GeneralResult>?>(null)
    val response: StateFlow<Resource<GeneralResult>?> get() = _response

    val disabilities = Disabilities.getDisabilities()
    val skills = Skills.getSkills()

    val accountId = dataStoreManager.accountId.asLiveData()
    val token = dataStoreManager.token.asLiveData()

    var loading by mutableStateOf(false)
        private set

    var fullName by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    var address by mutableStateOf("")
        private set

    var age by mutableStateOf("")
        private set

    var gender by mutableIntStateOf(-1)
        private set

    var disability by mutableIntStateOf(-1)
        private set

    var skillOne by mutableIntStateOf(-1)
        private set

    var skillTwo by mutableIntStateOf(-1)
        private set

    fun setOnLoading(value: Boolean) {
        loading = value
    }

    fun setOnFullName(value: String) {
        fullName = value
    }

    fun setOnPhoneNumber(value: String) {
        phoneNumber = value
    }

    fun setOnAddress(value: String) {
        address = value
    }

    fun setOnAge(value: String) {
        age = value
    }

    fun setOnGender(index: Int) {
        gender = index
    }

    fun setOnDisability(index: Int) {
        disability = index
    }

    fun setOnSkillOne(index: Int) {
        skillOne = index
    }

    fun setOnSkillTwo(index: Int) {
        skillTwo = index
    }

    fun setDisabilityDataStore(disability: String) {
        viewModelScope.launch {
            dataStoreManager.setDisability(disability)
        }
    }

    fun setSkillOneDataStore(skillOne: String) {
        viewModelScope.launch {
            dataStoreManager.setSkillOne(skillOne)
        }
    }

    fun setSkillTwoDataStore(skillTwo: String) {
        viewModelScope.launch {
            dataStoreManager.setSkillTwo(skillTwo)
        }
    }

    fun update(token: String, id: String, request: JobSeekerEditRequest) {
        viewModelScope.launch {
            jobSeekerUseCase.updateJobSeeker(token, id, request)
                .catch { err ->
                    _response.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _response.value = result
                }
        }
    }

    fun validateFields(): Boolean =
        fullName.isNotEmpty() && phoneNumber.isNotEmpty() && address.isNotEmpty() &&
                age.isNotEmpty() && gender != -1 && disability != -1
}