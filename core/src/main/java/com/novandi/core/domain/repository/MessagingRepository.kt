package com.novandi.core.domain.repository

import com.novandi.core.data.source.remote.request.MessagingRegisterRequest
import com.novandi.core.data.source.remote.request.MessagingRequest
import com.novandi.core.domain.model.GeneralResult

interface MessagingRepository {
    suspend fun registerFcmToken(request: MessagingRegisterRequest): GeneralResult
    suspend fun sendNotification(request: MessagingRequest): GeneralResult
}