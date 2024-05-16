package com.novandi.core.data.repository

import com.novandi.core.data.response.NetworkOnlyResource
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.local.LocalDataSource
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.network.ApiResponse
import com.novandi.core.data.source.remote.request.AssistantRequest
import com.novandi.core.data.source.remote.response.AssistantResponse
import com.novandi.core.domain.model.AssistantChat
import com.novandi.core.domain.model.AssistantResult
import com.novandi.core.domain.repository.AssistantRepository
import com.novandi.core.mapper.AssistantMapper
import com.novandi.utility.data.AppExecutors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AssistantRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): AssistantRepository {
    override fun getAssistantResult(request: AssistantRequest): Flow<Resource<AssistantResult>> =
        object : NetworkOnlyResource<AssistantResult, AssistantResponse>() {
            override fun loadFromNetwork(data: AssistantResponse): Flow<AssistantResult> =
                AssistantMapper.assistantResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<AssistantResponse>> =
                remoteDataSource.getAssistantResult(request)
        }.asFlow()

    override fun getAll(): Flow<List<AssistantChat>> = localDataSource.getPromptAssistant().map {
        AssistantMapper.entityToDomain(it)
    }

    override fun saveChat(chat: AssistantChat) {
        val entity = AssistantMapper.domainToEntity(chat)
        appExecutors.diskIO().execute {
            localDataSource.savePrompt(entity)
        }
    }

    override fun deleteAll() {
        appExecutors.diskIO().execute {
            localDataSource.deletePrompts()
        }
    }

    override fun deleteChat(id: Int) {
        appExecutors.diskIO().execute {
            localDataSource.deletePrompt(id)
        }
    }
}