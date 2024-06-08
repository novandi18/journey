package com.novandi.core.domain.usecase

import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.AssistantRequest
import com.novandi.core.domain.model.AssistantChat
import com.novandi.core.domain.model.AssistantResult
import kotlinx.coroutines.flow.Flow

interface AssistantUseCase {
    fun getAssistantResult(request: AssistantRequest): Flow<Resource<AssistantResult>>
    fun getAll(userId: String): Flow<List<AssistantChat>>
    fun saveChat(chat: AssistantChat)
    fun deleteAll(userId: String)
    fun deleteChat(id: Int)
}