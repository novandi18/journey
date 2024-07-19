package com.novandi.core.data.repository

import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.request.MessagingRegisterRequest
import com.novandi.core.data.source.remote.request.MessagingRequest
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.repository.MessagingRepository
import com.novandi.core.mapper.VacancyMapper
import javax.inject.Inject

class MessagingRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): MessagingRepository {
    override suspend fun registerFcmToken(request: MessagingRegisterRequest): GeneralResult {
        val response = remoteDataSource.registerFcmToken(request)
        return VacancyMapper.mapGeneralResponseToDomainNoFlow(response)
    }

    override suspend fun sendNotification(request: MessagingRequest): GeneralResult {
        val response = remoteDataSource.sendNotification(request)
        return VacancyMapper.mapGeneralResponseToDomainNoFlow(response)
    }
}