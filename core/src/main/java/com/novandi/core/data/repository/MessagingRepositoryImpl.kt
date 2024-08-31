package com.novandi.core.data.repository

import com.novandi.core.data.response.NetworkOnlyResource
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.network.ApiResponse
import com.novandi.core.data.source.remote.request.MessagingRegisterRequest
import com.novandi.core.data.source.remote.request.MessagingRequest
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.repository.MessagingRepository
import com.novandi.core.mapper.VacancyMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessagingRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): MessagingRepository {
    override suspend fun registerFcmToken(request: MessagingRegisterRequest): GeneralResult {
        val response = remoteDataSource.registerFcmToken(request)
        return VacancyMapper.mapGeneralResponseToDomainNoFlow(response)
    }

    override suspend fun sendNotification(request: MessagingRequest): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                VacancyMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.sendNotification(request)
        }.asFlow()
}