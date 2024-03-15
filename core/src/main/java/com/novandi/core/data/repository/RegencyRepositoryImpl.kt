package com.novandi.core.data.repository

import com.novandi.core.data.response.NetworkOnlyResource
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.network.ApiResponse
import com.novandi.core.data.source.remote.response.RegencyItem
import com.novandi.core.domain.model.Regency
import com.novandi.core.domain.repository.RegencyRepository
import com.novandi.core.mapper.RegencyMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegencyRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): RegencyRepository {
    override fun getRegency(provinceId: String): Flow<Resource<List<Regency>>> =
        object : NetworkOnlyResource<List<Regency>, List<RegencyItem>>() {
            override fun loadFromNetwork(data: List<RegencyItem>): Flow<List<Regency>> =
                RegencyMapper.mapListRegencyToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<List<RegencyItem>>> =
                remoteDataSource.getRegency(provinceId)
        }.asFlow()
}