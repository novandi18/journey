package com.novandi.core.data.repository

import com.novandi.core.data.response.NetworkOnlyResource
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.network.ApiResponse
import com.novandi.core.data.source.remote.request.JobSeekerRegisterRequest
import com.novandi.core.data.source.remote.request.LoginRequest
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.JobApplyStatusResponse
import com.novandi.core.data.source.remote.response.LoginJobSeekerResponse
import com.novandi.core.data.source.remote.response.ProfileJobSeekerResponse
import com.novandi.core.data.source.remote.response.RegisterResponse
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.JobApplyStatus
import com.novandi.core.domain.model.LoginResult
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.core.domain.model.RegisterResult
import com.novandi.core.domain.repository.JobSeekerRepository
import com.novandi.core.mapper.JobSeekerMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JobSeekerRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): JobSeekerRepository {
    override fun loginJobSeeker(loginRequest: LoginRequest): Flow<Resource<LoginResult>> =
        object : NetworkOnlyResource<LoginResult, LoginJobSeekerResponse>() {
            override fun loadFromNetwork(data: LoginJobSeekerResponse): Flow<LoginResult> =
                JobSeekerMapper.mapLoginResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<LoginJobSeekerResponse>> =
                remoteDataSource.loginJobSeeker(loginRequest)
        }.asFlow()

    override fun registerJobSeeker(request: JobSeekerRegisterRequest): Flow<Resource<RegisterResult>> =
        object : NetworkOnlyResource<RegisterResult, RegisterResponse>() {
            override fun loadFromNetwork(data: RegisterResponse): Flow<RegisterResult> =
                JobSeekerMapper.mapRegisterResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<RegisterResponse>> =
                remoteDataSource.registerJobSeeker(request)
        }.asFlow()

    override fun getJobSeeker(token: String, id: String): Flow<Resource<ProfileJobSeeker>> =
        object : NetworkOnlyResource<ProfileJobSeeker, ProfileJobSeekerResponse>() {
            override fun loadFromNetwork(data: ProfileJobSeekerResponse): Flow<ProfileJobSeeker> =
                JobSeekerMapper.mapProfileResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<ProfileJobSeekerResponse>> =
                remoteDataSource.getJobSeeker(token, id)
        }.asFlow()

    override fun postJobApply(
        token: String,
        userId: String,
        vacancyId: String
    ): Flow<Resource<GeneralResult>> =
        object : NetworkOnlyResource<GeneralResult, GeneralResponse>() {
            override fun loadFromNetwork(data: GeneralResponse): Flow<GeneralResult> =
                JobSeekerMapper.mapGeneralResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> =
                remoteDataSource.addJobApply(token, userId, vacancyId)
        }.asFlow()

    override fun getApplyStatus(
        token: String,
        userId: String
    ): Flow<Resource<List<JobApplyStatus>>> =
        object : NetworkOnlyResource<List<JobApplyStatus>, JobApplyStatusResponse>() {
            override fun loadFromNetwork(data: JobApplyStatusResponse): Flow<List<JobApplyStatus>> =
                JobSeekerMapper.mapApplyStatusResponseToDomain(data)

            override suspend fun createCall(): Flow<ApiResponse<JobApplyStatusResponse>> =
                remoteDataSource.getApplyStatus(token, userId)
        }.asFlow()
}