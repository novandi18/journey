package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.consts.Provinces
import com.novandi.core.consts.Sectors
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobProviderEditRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.Regency
import com.novandi.core.domain.usecase.JobProviderUseCase
import com.novandi.core.domain.usecase.RegencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobProviderEditViewModel @Inject constructor(
    private val jobProviderUseCase: JobProviderUseCase,
    private val regencyUseCase: RegencyUseCase,
    dataStoreManager: DataStoreManager
): ViewModel() {
    private val _response = MutableStateFlow<Resource<GeneralResult>?>(null)
    val response: StateFlow<Resource<GeneralResult>?> get() = _response

    private val _regenciesData: MutableLiveData<Resource<List<Regency>>> = MutableLiveData(Resource.Loading())
    val regenciesData: MutableLiveData<Resource<List<Regency>>> get() = _regenciesData

    val accountId = dataStoreManager.accountId.asLiveData()
    val provinces = Provinces.getProvinces().map { it.province }
    val sectors = Sectors.getSectors()

    var loading by mutableStateOf(false)
        private set

    var name by mutableStateOf("")
        private set

    var sectorId by mutableIntStateOf(-1)
        private set

    var address by mutableStateOf("")
        private set

    var province by mutableIntStateOf(-1)
        private set

    var regency by mutableIntStateOf(-1)
        private set

    var regencies by mutableStateOf<List<String>>(listOf())
        private set

    var employees by mutableIntStateOf(0)
        private set

    fun setOnLoading(value: Boolean) {
        loading = value
    }

    fun setOnName(value: String) {
        name = value
    }

    fun setOnSectorId(value: Int) {
        sectorId = value
    }

    fun setOnAddress(value: String) {
        address = value
    }

    fun setOnProvince(value: Int) {
        province = value
    }

    fun setOnRegency(value: Int) {
        regency = value
    }

    fun setOnRegencies(regency: List<Regency>) {
        regencies = regency.map { it.city }
    }

    fun setOnEmployees(value: Int) {
        employees = value
    }

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

    fun update(id: String, request: JobProviderEditRequest) {
        viewModelScope.launch {
            jobProviderUseCase.updateJobProvider(id, request)
                .catch { err ->
                    _response.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _response.value = result
                }
        }
    }

    fun validateFields() = name.isNotEmpty() && sectorId != -1 && address.isNotEmpty() &&
            province != -1 && regency != -1 && employees >= 1
}