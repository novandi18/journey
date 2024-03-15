package com.novandi.core.data.source.remote.network

import com.novandi.core.data.source.remote.request.JobProviderEditRequest
import com.novandi.core.data.source.remote.request.JobProviderRegisterRequest
import com.novandi.core.data.source.remote.request.JobSeekerRegisterRequest
import com.novandi.core.data.source.remote.request.LoginRequest
import com.novandi.core.data.source.remote.request.UpdateEmailRequest
import com.novandi.core.data.source.remote.request.UpdatePasswordRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.data.source.remote.response.ApplicantItem
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.JobApplyStatusResponse
import com.novandi.core.data.source.remote.response.LoginJobProviderResponse
import com.novandi.core.data.source.remote.response.LoginJobSeekerResponse
import com.novandi.core.data.source.remote.response.ProfileJobProviderResponse
import com.novandi.core.data.source.remote.response.ProfileJobSeekerResponse
import com.novandi.core.data.source.remote.response.RegisterResponse
import com.novandi.core.data.source.remote.response.VacancyDetailResponse
import com.novandi.core.data.source.remote.response.VacancyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("vacancies")
    suspend fun getVacancies(
        @Query("key") key: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): VacancyResponse

    @GET("vacancies/latest")
    suspend fun getLatestVacancies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): VacancyResponse

    @GET("vacancies/popular")
    suspend fun getPopularVacancies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): VacancyResponse

    @GET("vacancies/{id}")
    suspend fun getVacancy(
        @Path("id") id: String
    ): VacancyDetailResponse

    @POST("users/login")
    suspend fun loginJobSeeker(
        @Body loginRequest: LoginRequest
    ): LoginJobSeekerResponse

    @POST("companies/login")
    suspend fun loginJobProvider(
        @Body loginRequest: LoginRequest
    ): LoginJobProviderResponse

    @POST("users")
    suspend fun registerJobSeeker(
        @Body request: JobSeekerRegisterRequest
    ): RegisterResponse

    @POST("companies")
    suspend fun registerJobProvider(
        @Body request: JobProviderRegisterRequest
    ): RegisterResponse

    @GET("users/{id}")
    suspend fun getJobSeeker(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ProfileJobSeekerResponse

    @GET("companies/{id}")
    suspend fun getJobProvider(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ProfileJobProviderResponse

    @POST("users/{userId}/vacancies/{vacancyId}/apply")
    suspend fun postJobApply(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("vacancyId") vacancyId: String
    ): GeneralResponse

    @GET("users/applicants/{userId}")
    suspend fun getApplyStatus(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): JobApplyStatusResponse

    @GET("companies/{companyId}/vacancies")
    suspend fun getJobProviderVacancies(
        @Header("Authorization") token: String,
        @Path("companyId") companyId: String
    ): VacancyResponse

    @POST("companies/{companyId}/vacancies")
    suspend fun addVacancy(
        @Header("Authorization") token: String,
        @Path("companyId") companyId: String,
        @Body vacancyRequest: VacancyRequest
    ): GeneralResponse

    @GET("vacancies/all-vacancies")
    suspend fun getVacanciesWithoutPager(): VacancyResponse

    @PUT("companies/{companyId}/vacancies/{vacancyId}")
    suspend fun updateVacancy(
        @Path("companyId") companyId: String,
        @Path("vacancyId") vacancyId: String,
        @Body request: VacancyRequest
    ): GeneralResponse

    @GET("companies/{companyId}/vacancies/{vacancyId}/applicants")
    suspend fun getApplicants(
        @Header("Authorization") token: String,
        @Path("companyId") companyId: String,
        @Path("vacancyId") vacancyId: String
    ): List<ApplicantItem>

    @PUT("companies/{companyId}/vacancies/{vacancyId}/applicants/{applicantsId}/accept")
    suspend fun postAcceptApplicants(
        @Header("Authorization") token: String,
        @Path("companyId") companyId: String,
        @Path("vacancyId") vacancyId: String,
        @Path("applicantsId") applicantsId: String
    ): GeneralResponse

    @PUT("companies/{companyId}/vacancies/{vacancyId}/applicants/{applicantsId}/reject")
    suspend fun postRejectApplicants(
        @Header("Authorization") token: String,
        @Path("companyId") companyId: String,
        @Path("vacancyId") vacancyId: String,
        @Path("applicantsId") applicantsId: String
    ): GeneralResponse

    @GET("vacancies/name/{position}")
    suspend fun getSearchVacancy(
        @Path("position") position: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): VacancyResponse

    @PUT("companies/{id}")
    suspend fun updateJobProvider(
        @Path("id") companyId: String,
        @Body request: JobProviderEditRequest
    ): GeneralResponse

    @PUT("companies/email/{id}")
    suspend fun updateJobProviderEmail(
        @Header("Authorization") token: String,
        @Path("id") companyId: String,
        @Body request: UpdateEmailRequest
    ): GeneralResponse

    @PUT("companies/password/{id}")
    suspend fun updateJobProviderPassword(
        @Header("Authorization") token: String,
        @Path("id") companyId: String,
        @Body request: UpdatePasswordRequest
    ): GeneralResponse
}