package com.novandi.core.data.source.remote.network

import com.novandi.core.data.source.remote.request.AcceptApplicantRequest
import com.novandi.core.data.source.remote.request.AssistantRequest
import com.novandi.core.data.source.remote.request.CloseVacancyRequest
import com.novandi.core.data.source.remote.request.JobProviderEditRequest
import com.novandi.core.data.source.remote.request.JobProviderRegisterRequest
import com.novandi.core.data.source.remote.request.JobSeekerEditRequest
import com.novandi.core.data.source.remote.request.JobSeekerRegisterRequest
import com.novandi.core.data.source.remote.request.LoginRequest
import com.novandi.core.data.source.remote.request.MessagingRegisterRequest
import com.novandi.core.data.source.remote.request.MessagingRequest
import com.novandi.core.data.source.remote.request.UpdateEmailRequest
import com.novandi.core.data.source.remote.request.UpdatePasswordRequest
import com.novandi.core.data.source.remote.request.VacanciesSearchRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.data.source.remote.response.ApplicantItem
import com.novandi.core.data.source.remote.response.AssistantResponse
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.JobApplyStatusResponse
import com.novandi.core.data.source.remote.response.LoginJobProviderResponse
import com.novandi.core.data.source.remote.response.LoginJobSeekerResponse
import com.novandi.core.data.source.remote.response.ProfileJobProviderResponse
import com.novandi.core.data.source.remote.response.ProfileJobSeekerResponse
import com.novandi.core.data.source.remote.response.RegisterResponse
import com.novandi.core.data.source.remote.response.UpdateCvResponse
import com.novandi.core.data.source.remote.response.UpdateProfilePhotoResponse
import com.novandi.core.data.source.remote.response.UpdatedJobStatusItem
import com.novandi.core.data.source.remote.response.VacancyDetailCompanyResponse
import com.novandi.core.data.source.remote.response.VacancyDetailUserResponse
import com.novandi.core.data.source.remote.response.VacancyResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("vacancies/company/{companyId}")
    suspend fun getVacancyCompanyDetail(
        @Path("companyId") companyId: String
    ): ProfileJobProviderResponse

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

    @GET("companies/{companyId}/applicants")
    suspend fun getJobProviderApplicants(
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
        @Path("applicantsId") applicantsId: String,
        @Body request: AcceptApplicantRequest
    ): GeneralResponse

    @PUT("companies/{companyId}/vacancies/{vacancyId}/applicants/{applicantsId}/reject")
    suspend fun postRejectApplicants(
        @Header("Authorization") token: String,
        @Path("companyId") companyId: String,
        @Path("vacancyId") vacancyId: String,
        @Path("applicantsId") applicantsId: String
    ): GeneralResponse

    @POST("vacancies/name/{position}")
    suspend fun getSearchVacancy(
        @Path("position") position: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Body request: VacanciesSearchRequest
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

    @Multipart
    @PUT("companies/logo/{id}")
    suspend fun updateJobProviderLogo(
        @Path("id") companyId: String,
        @Part logo: MultipartBody.Part
    ): UpdateProfilePhotoResponse

    @PUT("users/{id}")
    suspend fun updateJobSeeker(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Body request: JobSeekerEditRequest
    ): GeneralResponse

    @PUT("users/email/{id}")
    suspend fun updateJobSeekerEmail(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Body request: UpdateEmailRequest
    ): GeneralResponse

    @PUT("users/password/{id}")
    suspend fun updateJobSeekerPassword(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Body request: UpdatePasswordRequest
    ): GeneralResponse

    @Multipart
    @PUT("users/photo/{id}")
    suspend fun updateJobSeekerPhoto(
        @Path("id") userId: String,
        @Part photo: MultipartBody.Part
    ): UpdateProfilePhotoResponse

    @GET("users/applicants/updated/{userId}")
    suspend fun getUpdatedApplyStatus(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): List<UpdatedJobStatusItem>

    @POST("assistant")
    suspend fun getAssistantResult(
        @Body request: AssistantRequest
    ): AssistantResponse

    @GET("companies/{companyId}/applicants/{applicantId}")
    suspend fun getApplicantById(
        @Header("Authorization") token: String,
        @Path("companyId") companyId: String,
        @Path("applicantId") applicantId: String
    ): ProfileJobSeekerResponse

    @Multipart
    @PUT("users/cv/{id}")
    suspend fun updateJobSeekerCv(
        @Path("id") userId: String,
        @Part cv: MultipartBody.Part
    ): UpdateCvResponse

    @PUT("vacancies/closed")
    suspend fun closeVacancy(
        @Header("Authorization") token: String,
        @Body request: CloseVacancyRequest
    ): GeneralResponse

    @GET("vacancies/detail/company/{vacancyId}")
    suspend fun getVacancyInCompany(
        @Path("vacancyId") id: String
    ): VacancyDetailCompanyResponse

    @GET("vacancies/detail/{vacancyId}/user/{userId}")
    suspend fun getVacancyInUser(
        @Path("vacancyId") vacancyId: String,
        @Path("userId") userId: String,
    ): VacancyDetailUserResponse

    @POST("fcm/register")
    suspend fun registerFcmToken(
        @Body request: MessagingRegisterRequest
    ): GeneralResponse

    @POST("fcm/notification")
    suspend fun sendNotification(
        @Body request: MessagingRequest
    ): GeneralResponse
}