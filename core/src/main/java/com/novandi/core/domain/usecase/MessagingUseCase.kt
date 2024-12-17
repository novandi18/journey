package com.novandi.core.domain.usecase

import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.MessagingRegisterRequest
import com.novandi.core.data.source.remote.request.MessagingRequest
import com.novandi.core.domain.model.GeneralResult
import kotlinx.coroutines.flow.Flow

interface MessagingUseCase {
    suspend fun registerFcmToken(request: MessagingRegisterRequest): GeneralResult
    suspend fun sendNotification(request: MessagingRequest): Flow<Resource<GeneralResult>>
}