package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobSeekerRegisterRequest
import com.novandi.core.domain.model.RegisterResult
import com.novandi.core.domain.usecase.JobSeekerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobSeekerSkillViewModel @Inject constructor(
    private val jobSeekerUseCase: JobSeekerUseCase
): ViewModel() {
    private val _register = MutableStateFlow<Resource<RegisterResult>?>(null)
    val register: StateFlow<Resource<RegisterResult>?> get() = _register

    var isLoading by mutableStateOf(false)
        private set

    var skillOne by mutableIntStateOf(-1)
        private set

    var skillTwo by mutableIntStateOf(-1)
        private set

    fun setIsLoading(loading: Boolean) {
        isLoading = loading
    }

    fun setOnSkillOne(index: Int) {
        skillOne = index
    }

    fun setOnSkillTwo(index: Int) {
        skillTwo = index
    }

    fun register(request: JobSeekerRegisterRequest) {
        viewModelScope.launch {
            jobSeekerUseCase.registerJobSeeker(request)
                .catch { err ->
                    _register.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _register.value = result
                }
        }
    }
}