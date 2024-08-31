package com.novandi.core.domain.interactor

import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.MessagingRegisterRequest
import com.novandi.core.data.source.remote.request.MessagingRequest
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.repository.MessagingRepository
import com.novandi.core.domain.usecase.MessagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessagingInteractor @Inject constructor(
    private val messagingRepository: MessagingRepository
): MessagingUseCase {
    override suspend fun registerFcmToken(request: MessagingRegisterRequest) =
        messagingRepository.registerFcmToken(request)
    override suspend fun sendNotification(request: MessagingRequest): Flow<Resource<GeneralResult>> =
        messagingRepository.sendNotification(request)
}