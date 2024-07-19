package com.novandi.core.domain.usecase

import com.novandi.core.data.source.remote.request.MessagingRegisterRequest
import com.novandi.core.data.source.remote.request.MessagingRequest
import com.novandi.core.domain.model.GeneralResult

interface MessagingUseCase {
    suspend fun registerFcmToken(request: MessagingRegisterRequest): GeneralResult
    suspend fun sendNotification(request: MessagingRequest): GeneralResult
}