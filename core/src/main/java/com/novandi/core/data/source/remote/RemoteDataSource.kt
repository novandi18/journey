package com.novandi.core.data.source.remote

import android.util.Log
import com.google.gson.Gson
import com.novandi.core.data.response.ErrorResponse
import com.novandi.core.data.source.remote.network.ApiResponse
import com.novandi.core.data.source.remote.network.ApiService
import com.novandi.core.data.source.remote.network.RegencyApiService
import com.novandi.core.data.source.remote.request.AssistantRequest
import com.novandi.core.data.source.remote.request.JobProviderEditRequest
import com.novandi.core.data.source.remote.request.JobProviderRegisterRequest
import com.novandi.core.data.source.remote.request.JobSeekerEditRequest
import com.novandi.core.data.source.remote.request.JobSeekerRegisterRequest
import com.novandi.core.data.source.remote.request.LoginRequest
import com.novandi.core.data.source.remote.request.UpdateEmailRequest
import com.novandi.core.data.source.remote.request.UpdatePasswordRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.data.source.remote.response.ApplicantItem
import com.novandi.core.data.source.remote.response.AssistantResponse
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.JobApplyStatusResponse
import com.novandi.core.data.source.remote.response.LoginJobProviderResponse
import com.novandi.core.data.source.remote.response.LoginJobSeekerResponse
import com.novandi.core.data.source.remote.response.ProfileJobProviderResponse
import com.novandi.core.data.source.remote.response.ProfileJobSeekerResponse
import com.novandi.core.data.source.remote.response.RegencyItem
import com.novandi.core.data.source.remote.response.RegisterResponse
import com.novandi.core.data.source.remote.response.UpdateProfilePhotoResponse
import com.novandi.core.data.source.remote.response.UpdatedJobStatusItem
import com.novandi.core.data.source.remote.response.VacancyDetailResponse
import com.novandi.core.data.source.remote.response.VacancyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val regencyApiService: RegencyApiService
) {
    suspend fun getRegency(provinceId: String): Flow<ApiResponse<List<RegencyItem>>> = flow {
        try {
            val response = regencyApiService.getRegencyById(provinceId)
            if (response.isNotEmpty()) {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getVacancies(key: String, page: Int, limit: Int) =
        apiService.getVacancies(key, page, limit)

    suspend fun getLatestVacancies(page: Int, limit: Int) =
        apiService.getLatestVacancies(page, limit)

    suspend fun getPopularVacancies(page: Int, limit: Int) =
        apiService.getPopularVacancies(page, limit)

    suspend fun getVacancy(id: String): Flow<ApiResponse<VacancyDetailResponse>> = flow {
        try {
            val response = apiService.getVacancy(id)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun loginJobSeeker(loginRequest: LoginRequest)
        : Flow<ApiResponse<LoginJobSeekerResponse>> = flow {
        try {
            val response = apiService.loginJobSeeker(loginRequest)
            if (response.token.isNotEmpty()) {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun loginJobProvider(loginRequest: LoginRequest)
        : Flow<ApiResponse<LoginJobProviderResponse>> = flow {
        try {
            val response = apiService.loginJobProvider(loginRequest)
            if (response.token.isNotEmpty()) {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun registerJobSeeker(request: JobSeekerRegisterRequest)
        : Flow<ApiResponse<RegisterResponse>> = flow {
        try {
            val response = apiService.registerJobSeeker(request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun registerJobProvider(request: JobProviderRegisterRequest)
        : Flow<ApiResponse<RegisterResponse>> = flow {
        try {
            val response = apiService.registerJobProvider(request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getJobSeeker(token: String, id: String)
        : Flow<ApiResponse<ProfileJobSeekerResponse>> = flow {
        try {
            val response = apiService.getJobSeeker(token, id)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getJobProvider(token: String, id: String)
        : Flow<ApiResponse<ProfileJobProviderResponse>> = flow {
        try {
            val response = apiService.getJobProvider(token, id)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addJobApply(token: String, userId: String, vacancyId: String)
        : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.postJobApply(token, userId, vacancyId)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getApplyStatus(token: String, userId: String)
        : Flow<ApiResponse<JobApplyStatusResponse>> = flow {
        try {
            val response = apiService.getApplyStatus(token, userId)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getJobProviderVacancies(token: String, companyId: String)
        : Flow<ApiResponse<VacancyResponse>> = flow {
        try {
            val response = apiService.getJobProviderVacancies(token, companyId)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getJobProviderApplicants(token: String, companyId: String)
            : Flow<ApiResponse<VacancyResponse>> = flow {
        try {
            val response = apiService.getJobProviderApplicants(token, companyId)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addVacancy(token: String, companyId: String, vacancyRequest: VacancyRequest)
        : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.addVacancy(token, companyId, vacancyRequest)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getVacanciesWithoutPager(): Flow<ApiResponse<VacancyResponse>> = flow {
        try {
            val response = apiService.getVacanciesWithoutPager()
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateVacancy(companyId: String, vacancyId: String, request: VacancyRequest)
        : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.updateVacancy(companyId, vacancyId, request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getApplicants(token: String, companyId: String, vacancyId: String)
        : Flow<ApiResponse<List<ApplicantItem>>> = flow {
        try {
            val response = apiService.getApplicants(token, companyId, vacancyId)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun postAcceptApplicants(token: String, companyId: String, vacancyId: String, applicantsId: String)
        : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.postAcceptApplicants(token, companyId, vacancyId, applicantsId)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun postRejectApplicants(token: String, companyId: String, vacancyId: String, applicantsId: String)
            : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.postRejectApplicants(token, companyId, vacancyId, applicantsId)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getSearchVacancy(position: String, page: Int, limit: Int) =
        apiService.getSearchVacancy(position, page, limit)

    suspend fun updateJobProvider(companyId: String, request: JobProviderEditRequest)
            : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.updateJobProvider(companyId, request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateJobProviderEmail(token: String, companyId: String, request: UpdateEmailRequest)
            : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.updateJobProviderEmail(token, companyId, request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateJobProviderPassword(token: String, companyId: String, request: UpdatePasswordRequest)
            : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.updateJobProviderPassword(token, companyId, request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateJobProviderLogo(companyId: String, logo: MultipartBody.Part)
            : Flow<ApiResponse<UpdateProfilePhotoResponse>> = flow {
        try {
            val response = apiService.updateJobProviderLogo(companyId, logo)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateJobSeeker(token: String, userId: String, request: JobSeekerEditRequest)
            : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.updateJobSeeker(token, userId, request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateJobSeekerEmail(token: String, userId: String, request: UpdateEmailRequest)
            : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.updateJobSeekerEmail(token, userId, request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateJobSeekerPassword(token: String, userId: String, request: UpdatePasswordRequest)
            : Flow<ApiResponse<GeneralResponse>> = flow {
        try {
            val response = apiService.updateJobSeekerPassword(token, userId, request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateJobSeekerPhoto(userId: String, photo: MultipartBody.Part)
            : Flow<ApiResponse<UpdateProfilePhotoResponse>> = flow {
        try {
            val response = apiService.updateJobSeekerPhoto(userId, photo)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getUpdatedApplyStatus(token: String, userId: String): List<UpdatedJobStatusItem> =
        apiService.getUpdatedApplyStatus(token, userId)

    suspend fun getAssistantResult(request: AssistantRequest): Flow<ApiResponse<AssistantResponse>> = flow {
        try {
            val response = apiService.getAssistantResult(request)
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
            } catch (e: Exception) { null }
            emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
            Log.e("RemoteDataSource", errorMessage ?: "Unknown error")
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)
}