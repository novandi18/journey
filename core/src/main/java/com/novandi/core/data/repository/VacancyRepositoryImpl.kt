package com.novandi.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.novandi.core.data.paging.LatestVacanciesPagingSource
import com.novandi.core.data.paging.PopularVacanciesPagingSource
import com.novandi.core.data.paging.RecommendationVacanciesPagingSource
import com.novandi.core.data.paging.SearchVacanciesPagingSource
import com.novandi.core.data.paging.VacanciesPagingSource
import com.novandi.core.data.response.NetworkOnlyResource
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.network.ApiResponse
import com.novandi.core.data.source.remote.request.CloseVacancyRequest
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.request.RecommendationVacanciesRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.RecommendationResponse
import com.novandi.core.data.source.remote.response.VacancyDetailResponse
import com.novandi.core.data.source.remote.response.VacancyResponse
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.repository.VacancyRepository
import com.novandi.core.mapper.VacancyMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VacancyRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): VacancyRepository {
    override fun getVacancies(key: String): Flow<PagingData<Vacancy>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                VacanciesPagingSource(remoteDataSource, key)
            }
        ).flow

    override fun getLatestVacancies(): Flow<PagingData<Vacancy>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                LatestVacanciesPagingSource(remoteDataSource)
            }
        ).flow

    override fun getPopularVacancies(): Flow<PagingData<Vacancy>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                PopularVacanciesPagingSource(remoteDataSource)
            }
        ).flow

    override fun getVacancy(id: String): Flow<Resource<Vacancy>> =
        object : NetworkOnlyResource<Vacancy, VacancyDetailResponse>() {
            override fun loadFromNetwork(data: VacancyDetailResponse): Flow<Vacancy> =
                VacancyMapper.mapDetailResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<VacancyDetailResponse>> =
                remoteDataSource.getVacancy(id)
        }.asFlow()

    override fun getVacanciesWithoutPager(): Flow<Resource<List<Vacancy>>> =
        object : NetworkOnlyResource<List<Vacancy>, VacancyResponse>() {
            override fun loadFromNetwork(data: VacancyResponse): Flow<List<Vacancy>> =
                VacancyMapper.mapNoPagerResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<VacancyResponse>> =
                remoteDataSource.getVacanciesWithoutPager()
        }.asFlow()

    override fun addVacancy(
        token: String,
        companyId: String,
        request: VacancyRequest
    ): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                VacancyMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.addVacancy(token, companyId, request)
        }.asFlow()

    override fun searchVacancy(position: String): Flow<PagingData<Vacancy>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchVacanciesPagingSource(remoteDataSource, position)
            }
        ).flow

    override fun getJobProviderVacancies(
        token: String,
        companyId: String
    ): Flow<Resource<List<Vacancy>>> =
        object : NetworkOnlyResource<List<Vacancy>, VacancyResponse>() {
            override fun loadFromNetwork(data: VacancyResponse): Flow<List<Vacancy>> =
                VacancyMapper.mapNoPagerResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<VacancyResponse>> =
                remoteDataSource.getJobProviderVacancies(token, companyId)
        }.asFlow()

    override fun getRecommendation(request: RecommendationRequest): Flow<Resource<List<String>>> =
        object : NetworkOnlyResource<List<String>, RecommendationResponse>() {
            override fun loadFromNetwork(data: RecommendationResponse): Flow<List<String>> =
                VacancyMapper.recommendationToList(data)

            override suspend fun createCall(): Flow<ApiResponse<RecommendationResponse>> =
                remoteDataSource.getRecommendation(request)
        }.asFlow()

    override fun getRecommendationVacancies(recommendations: RecommendationVacanciesRequest)
    : Flow<PagingData<Vacancy>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                RecommendationVacanciesPagingSource(remoteDataSource, recommendations)
            }
        ).flow

    override fun closeVacancy(token: String, request: CloseVacancyRequest): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                VacancyMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.closeVacancy(token, request)
        }.asFlow()
}