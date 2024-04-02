package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.AssistantRequest
import com.novandi.core.domain.model.AssistantChat
import com.novandi.core.domain.model.AssistantResult
import com.novandi.core.domain.usecase.AssistantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val assistantUseCase: AssistantUseCase
): ViewModel() {
    private val _results = MutableStateFlow<Resource<AssistantResult>?>(null)
    val results: StateFlow<Resource<AssistantResult>?> get() = _results

    val chats = assistantUseCase.getAll().asLiveData()

    var loading by mutableStateOf(false)
        private set

    var prompt by mutableStateOf("")
        private set

    fun setOnLoading(value: Boolean) {
        loading = value
    }

    fun setOnPrompt(value: String) {
        prompt = value
    }

    fun resetResults() {
        _results.value = null
    }

    fun ask(chat: String) {
        viewModelScope.launch {
            assistantUseCase.getAssistantResult(AssistantRequest(chat))
                .catch { err ->
                    _results.value = Resource.Error(err.message.toString())
                }
                .collect { result ->
                    _results.value = result
                }
        }
    }

    fun addChat(chat: AssistantChat) {
        viewModelScope.launch {
            assistantUseCase.saveChat(chat)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            assistantUseCase.deleteAll()
        }
    }
}