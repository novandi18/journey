package com.novandi.core.mapper

import com.novandi.core.data.source.local.entity.AssistantEntity
import com.novandi.core.data.source.remote.response.AssistantResponse
import com.novandi.core.domain.model.AssistantChat
import com.novandi.core.domain.model.AssistantResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object AssistantMapper {
    fun assistantResponseToDomain(input: AssistantResponse): Flow<AssistantResult> = flowOf(
        AssistantResult(input.user, input.bot)
    )

    fun entityToDomain(input: List<AssistantEntity>) = input.map { assistantEntity ->
        AssistantChat(
            message = assistantEntity.chat,
            isFromMe = assistantEntity.isFromMe
        )
    }

    fun domainToEntity(input: AssistantChat) = AssistantEntity(
        chat = input.message,
        isFromMe = input.isFromMe,
        createdAt = System.currentTimeMillis()
    )
}