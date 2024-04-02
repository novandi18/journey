package com.novandi.core.domain.repository

import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.AssistantRequest
import com.novandi.core.domain.model.AssistantChat
import com.novandi.core.domain.model.AssistantResult
import kotlinx.coroutines.flow.Flow

interface AssistantRepository {
    fun getAssistantResult(request: AssistantRequest): Flow<Resource<AssistantResult>>
    fun getAll(): Flow<List<AssistantChat>>
    fun saveChat(chat: AssistantChat)
    fun deleteAll()
}