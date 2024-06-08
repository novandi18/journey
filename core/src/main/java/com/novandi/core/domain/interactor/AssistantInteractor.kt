package com.novandi.core.domain.interactor

import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.AssistantRequest
import com.novandi.core.domain.model.AssistantChat
import com.novandi.core.domain.model.AssistantResult
import com.novandi.core.domain.repository.AssistantRepository
import com.novandi.core.domain.usecase.AssistantUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AssistantInteractor @Inject constructor(
    private val assistantRepository: AssistantRepository
): AssistantUseCase {
    override fun getAssistantResult(request: AssistantRequest): Flow<Resource<AssistantResult>> =
        assistantRepository.getAssistantResult(request)

    override fun getAll(userId: String): Flow<List<AssistantChat>> = assistantRepository.getAll(userId)

    override fun saveChat(chat: AssistantChat) = assistantRepository.saveChat(chat)

    override fun deleteAll(userId: String) = assistantRepository.deleteAll(userId)
    override fun deleteChat(id: Int) = assistantRepository.deleteChat(id)
}