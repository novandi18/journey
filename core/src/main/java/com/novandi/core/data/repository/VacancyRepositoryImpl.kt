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