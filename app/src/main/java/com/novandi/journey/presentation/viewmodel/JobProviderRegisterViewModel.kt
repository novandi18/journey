package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandi.core.consts.Provinces
import com.novandi.core.consts.Sectors
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobProviderRegisterRequest
import com.novandi.core.domain.model.Regency
import com.novandi.core.domain.model.RegisterResult
import com.novandi.core.domain.usecase.JobProviderUseCase
import com.novandi.core.domain.usecase.RegencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderRegisterViewModel @Inject constructor(
    private val jobProviderUseCase: JobProviderUseCase,
    private val regencyUseCase: RegencyUseCase
): ViewModel() {
    private val _regenciesData: MutableLiveData<Resource<List<Regency>>> = MutableLiveData(Resource.Loading())
    val regenciesData: MutableLiveData<Resource<List<Regency>>> get() = _regenciesData

    private val _register = MutableStateFlow<Resource<RegisterResult>?>(null)
    val register: StateFlow<Resource<RegisterResult>?> get() = _register

    val provinces = Provinces.getProvinces().map { it.province }
    val sectors = Sectors.getSectors()

    var regencies by mutableStateOf<List<String>>(listOf())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var companyName by mutableStateOf("")
        private set

    var province by mutableIntStateOf(-1)
        private set

    var city by mutableIntStateOf(-1)
        private set

    var sector by mutableIntStateOf(-1)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var address by mutableStateOf("")
        private set

    var employees by mutableStateOf("")
        private set

    fun setOnRegencies(regency: List<Regency>) {
        regencies = regency.map { it.city }
    }

    fun setOnIsLoading(loading: Boolean) {
        isLoading = loading
    }

    fun setOnCompanyName(value: String) {
        companyName = value
    }

    fun setOnProvince(value: Int) {
        province = value
    }

    fun setOnCity(value: Int) {
        city = value
    }

    fun setOnSector(value: Int) {
        sector = value
    }

    fun setOnEmail(value: String) {
        email = value
    }

    fun setOnPassword(value: String) {
        password = value
    }

    fun setOnAddress(value: String) {
        address = value
    }

    fun setOnEmployees(value: String) {
        employees = value
    }

    fun validateFields(): Boolean = email.isNotEmpty() && password.isNotEmpty()
        && companyName.isNotEmpty() && address.isNotEmpty() && employees.isNotEmpty()

    fun getRegencies() {
        viewModelScope.launch {
            regencyUseCase.getRegency(Provinces.getProvinces()[province].id)
                .catch { err ->
                    _regenciesData.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _regenciesData.value = result
                }
        }
    }

    fun register(request: JobProviderRegisterRequest) {
        viewModelScope.launch {
            jobProviderUseCase.registerJobProvider(request)
                .catch { err ->
                    _register.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _register.value = result
                }
        }
    }
}