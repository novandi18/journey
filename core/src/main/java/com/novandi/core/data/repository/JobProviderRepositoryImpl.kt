package com.novandi.core.data.repository

import com.novandi.core.data.response.NetworkOnlyResource
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.network.ApiResponse
import com.novandi.core.data.source.remote.request.AcceptApplicantRequest
import com.novandi.core.data.source.remote.request.JobProviderEditRequest
import com.novandi.core.data.source.remote.request.JobProviderRegisterRequest
import com.novandi.core.data.source.remote.request.LoginRequest
import com.novandi.core.data.source.remote.request.UpdateEmailRequest
import com.novandi.core.data.source.remote.request.UpdatePasswordRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.data.source.remote.request.WhatsappRequest
import com.novandi.core.data.source.remote.response.ApplicantItem
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.LoginJobProviderResponse
import com.novandi.core.data.source.remote.response.ProfileJobProviderResponse
import com.novandi.core.data.source.remote.response.ProfileJobSeekerResponse
import com.novandi.core.data.source.remote.response.RegisterResponse
import com.novandi.core.data.source.remote.response.UpdateProfilePhotoResponse
import com.novandi.core.data.source.remote.response.VacancyResponse
import com.novandi.core.data.source.remote.response.WhatsappResponse
import com.novandi.core.domain.model.Applicant
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.LoginResult
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.core.domain.model.RegisterResult
import com.novandi.core.domain.model.UpdateProfilePhotoResult
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.model.WhatsappResult
import com.novandi.core.domain.repository.JobProviderRepository
import com.novandi.core.mapper.JobProviderMapper
import com.novandi.core.mapper.JobSeekerMapper
import com.novandi.core.mapper.VacancyMapper
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class JobProviderRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): JobProviderRepository {
    override fun loginJobProvider(loginRequest: LoginRequest): Flow<Resource<LoginResult>> =
        object : NetworkOnlyResource<LoginResult, LoginJobProviderResponse>() {
            override fun loadFromNetwork(data: LoginJobProviderResponse): Flow<LoginResult> =
                JobProviderMapper.mapLoginResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<LoginJobProviderResponse>> =
                remoteDataSource.loginJobProvider(loginRequest)
        }.asFlow()

    override fun registerJobProvider(request: JobProviderRegisterRequest): Flow<Resource<RegisterResult>> =
        object : NetworkOnlyResource<RegisterResult, RegisterResponse>() {
            override fun loadFromNetwork(data: RegisterResponse): Flow<RegisterResult> =
                JobProviderMapper.mapRegisterResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<RegisterResponse>> =
                remoteDataSource.registerJobProvider(request)
        }.asFlow()

    override fun getJobProvider(token: String, id: String): Flow<Resource<ProfileJobProvider>> =
        object : NetworkOnlyResource<ProfileJobProvider, ProfileJobProviderResponse>() {
            override fun loadFromNetwork(data: ProfileJobProviderResponse): Flow<ProfileJobProvider> =
                JobProviderMapper.mapProfileResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<ProfileJobProviderResponse>> =
                remoteDataSource.getJobProvider(token, id)
        }.asFlow()

    override fun getApplicants(
        token: String,
        companyId: String,
        vacancyId: String
    ): Flow<Resource<List<Applicant>>> =
        object : NetworkOnlyResource<List<Applicant>, List<ApplicantItem>>() {
            override fun loadFromNetwork(data: List<ApplicantItem>): Flow<List<Applicant>> =
                JobProviderMapper.mapApplicantsToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<List<ApplicantItem>>> =
                remoteDataSource.getApplicants(token, companyId, vacancyId)
        }.asFlow()

    override fun postAcceptApplicants(
        token: String,
        companyId: String,
        vacancyId: String,
        applicantId: String,
        request: AcceptApplicantRequest
    ): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                JobProviderMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.postAcceptApplicants(
                    token, companyId, vacancyId, applicantId, request
                )
        }.asFlow()

    override fun postRejectApplicants(
        token: String,
        companyId: String,
        vacancyId: String,
        applicantId: String
    ): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                JobProviderMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.postRejectApplicants(token, companyId, vacancyId, applicantId)
        }.asFlow()

    override fun updateVacancy(
        companyId: String,
        vacancyId: String,
        request: VacancyRequest
    ): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                JobProviderMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.updateVacancy(companyId, vacancyId, request)
        }.asFlow()

    override fun updateJobProvider(
        companyId: String,
        request: JobProviderEditRequest
    ): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                JobProviderMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.updateJobProvider(companyId, request)
        }.asFlow()

    override fun updateJobProviderEmail(
        token: String,
        companyId: String,
        request: UpdateEmailRequest
    ): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                JobProviderMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.updateJobProviderEmail(token, companyId, request)
        }.asFlow()

    override fun updateJobProviderPassword(
        token: String,
        companyId: String,
        request: UpdatePasswordRequest
    ): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                JobProviderMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.updateJobProviderPassword(token, companyId, request)
        }.asFlow()

    override fun updateJobProviderLogo(
        companyId: String,
        logo: MultipartBody.Part
    ): Flow<Resource<UpdateProfilePhotoResult>> =
        object : NetworkOnlyResource<UpdateProfilePhotoResult, UpdateProfilePhotoResponse>() {
            override fun loadFromNetwork(data: UpdateProfilePhotoResponse)
                : Flow<UpdateProfilePhotoResult> =
                JobProviderMapper.updateProfilePhotoResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<UpdateProfilePhotoResponse>> =
                remoteDataSource.updateJobProviderLogo(companyId, logo)
        }.asFlow()

    override fun getVacanciesApplicants(
        token: String,
        companyId: String
    ): Flow<Resource<List<Vacancy>>> =
        object : NetworkOnlyResource<List<Vacancy>, VacancyResponse>() {
            override fun loadFromNetwork(data: VacancyResponse): Flow<List<Vacancy>> =
                VacancyMapper.mapNoPagerResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<VacancyResponse>> =
                remoteDataSource.getJobProviderApplicants(token, companyId)
        }.asFlow()

    override fun getApplicantById(
        token: String,
        companyId: String,
        applicantId: String
    ): Flow<Resource<ProfileJobSeeker>> =
        object : NetworkOnlyResource<ProfileJobSeeker, ProfileJobSeekerResponse>() {
            override fun loadFromNetwork(data: ProfileJobSeekerResponse): Flow<ProfileJobSeeker> =
                JobSeekerMapper.mapProfileResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<ProfileJobSeekerResponse>> =
                remoteDataSource.getApplicantById(token, companyId, applicantId)
        }.asFlow()

    override fun sendWhatsappMessage(request: WhatsappRequest): Flow<Resource<WhatsappResult>> =
        object : NetworkOnlyResource<WhatsappResult, WhatsappResponse>() {
            override fun loadFromNetwork(data: WhatsappResponse): Flow<WhatsappResult> =
                JobProviderMapper.mapWhatsappResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<WhatsappResponse>> =
                remoteDataSource.sendWhatsappMessage(request)

        }.asFlow()
}