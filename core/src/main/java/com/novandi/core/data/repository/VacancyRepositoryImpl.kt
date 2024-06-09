package com.novandi.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.novandi.core.data.mediator.AllVacanciesRemoteMediator
import com.novandi.core.data.mediator.LatestVacanciesRemoteMediator
import com.novandi.core.data.mediator.PopularVacanciesRemoteMediator
import com.novandi.core.data.mediator.RecommendationVacanciesRemoteMediator
import com.novandi.core.data.paging.SearchVacanciesPagingSource
import com.novandi.core.data.response.NetworkOnlyResource
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.local.LocalDataSource
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.local.entity.LatestVacancyEntity
import com.novandi.core.data.source.local.entity.PopularVacancyEntity
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.network.ApiResponse
import com.novandi.core.data.source.remote.request.CloseVacancyRequest
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.request.RecommendationVacanciesRequest
import com.novandi.core.data.source.remote.request.VacanciesSearchRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.data.source.remote.request.WhatsappRequest
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.ProfileJobProviderResponse
import com.novandi.core.data.source.remote.response.RecommendationResponse
import com.novandi.core.data.source.remote.response.VacancyDetailCompanyResponse
import com.novandi.core.data.source.remote.response.VacancyDetailUserResponse
import com.novandi.core.data.source.remote.response.VacancyResponse
import com.novandi.core.data.source.remote.response.WhatsappResponse
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.model.VacancyDetailCompany
import com.novandi.core.domain.model.VacancyDetailUser
import com.novandi.core.domain.model.WhatsappResult
import com.novandi.core.domain.repository.VacancyRepository
import com.novandi.core.mapper.JobProviderMapper
import com.novandi.core.mapper.VacancyMapper
import com.novandi.utility.data.AppExecutors
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VacancyRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): VacancyRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getVacancies(key: String): Flow<PagingData<AllVacancyEntity>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = AllVacanciesRemoteMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource,
                appExecutors = appExecutors
            ),
            pagingSourceFactory = {
                localDataSource.getAllVacancies()
            }
        ).flow

    @OptIn(ExperimentalPagingApi::class)
    override fun getLatestVacancies(): Flow<PagingData<LatestVacancyEntity>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = LatestVacanciesRemoteMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource,
                appExecutors = appExecutors
            ),
            pagingSourceFactory = {
                localDataSource.getLatestVacancies()
            }
        ).flow

    @OptIn(ExperimentalPagingApi::class)
    override fun getPopularVacancies(): Flow<PagingData<PopularVacancyEntity>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PopularVacanciesRemoteMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource,
                appExecutors = appExecutors
            ),
            pagingSourceFactory = {
                localDataSource.getPopularVacancies()
            }
        ).flow

    override fun getVacancyInUser(
        companyId: String,
        userId: String
    ): Flow<Resource<VacancyDetailUser>> =
        object : NetworkOnlyResource<VacancyDetailUser, VacancyDetailUserResponse>() {
            override fun loadFromNetwork(data: VacancyDetailUserResponse): Flow<VacancyDetailUser> =
                VacancyMapper.mapDetailUserToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<VacancyDetailUserResponse>> =
                remoteDataSource.getVacancyInUser(companyId, userId)
        }.asFlow()

    override fun getVacancyInCompany(vacancyId: String): Flow<Resource<VacancyDetailCompany>> =
        object : NetworkOnlyResource<VacancyDetailCompany, VacancyDetailCompanyResponse>() {
            override fun loadFromNetwork(data: VacancyDetailCompanyResponse): Flow<VacancyDetailCompany> =
                VacancyMapper.mapDetailCompanyToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<VacancyDetailCompanyResponse>> =
                remoteDataSource.getVacancyInCompany(vacancyId)
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

    override fun searchVacancy(position: String, request: VacanciesSearchRequest)
    : Flow<PagingData<Vacancy>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchVacanciesPagingSource(remoteDataSource, position, request)
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

    @OptIn(ExperimentalPagingApi::class)
    override fun getRecommendationVacancies(recommendations: RecommendationVacanciesRequest)
    : Flow<PagingData<RecommendationVacancyEntity>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = RecommendationVacanciesRemoteMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource,
                appExecutors = appExecutors,
                recommendations = recommendations
            ),
            pagingSourceFactory = {
                localDataSource.getRecommendationVacancies()
            }
        ).flow

    override fun closeVacancy(token: String, request: CloseVacancyRequest): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                VacancyMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.closeVacancy(token, request)
        }.asFlow()

    override fun getVacancyCompany(companyId: String): Flow<Resource<ProfileJobProvider>> =
        object : NetworkOnlyResource<ProfileJobProvider, ProfileJobProviderResponse>() {
            override fun loadFromNetwork(data: ProfileJobProviderResponse): Flow<ProfileJobProvider> =
                JobProviderMapper.mapProfileResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<ProfileJobProviderResponse>> =
                remoteDataSource.getVacancyCompanyDetail(companyId)
        }.asFlow()

    override fun sendWhatsappMessage(request: WhatsappRequest): Flow<Resource<WhatsappResult>> =
        object : NetworkOnlyResource<WhatsappResult, WhatsappResponse>() {
            override fun loadFromNetwork(data: WhatsappResponse): Flow<WhatsappResult> =
                JobProviderMapper.mapWhatsappResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<WhatsappResponse>> =
                remoteDataSource.sendWhatsappMessage(request)

        }.asFlow()
}