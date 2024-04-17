package com.novandi.core.domain.interactor

import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobProviderEditRequest
import com.novandi.core.data.source.remote.request.JobProviderRegisterRequest
import com.novandi.core.data.source.remote.request.LoginRequest
import com.novandi.core.data.source.remote.request.UpdateEmailRequest
import com.novandi.core.data.source.remote.request.UpdatePasswordRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.domain.model.Applicant
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.LoginResult
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.core.domain.model.RegisterResult
import com.novandi.core.domain.model.UpdateProfilePhotoResult
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.repository.JobProviderRepository
import com.novandi.core.domain.usecase.JobProviderUseCase
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class JobProviderInteractor @Inject constructor(
    private val jobProviderRepository: JobProviderRepository
): JobProviderUseCase {
    override fun loginJobProvider(loginRequest: LoginRequest): Flow<Resource<LoginResult>> =
        jobProviderRepository.loginJobProvider(loginRequest)

    override fun registerJobProvider(request: JobProviderRegisterRequest): Flow<Resource<RegisterResult>> =
        jobProviderRepository.registerJobProvider(request)

    override fun getJobProvider(token: String, id: String): Flow<Resource<ProfileJobProvider>> =
        jobProviderRepository.getJobProvider(token, id)

    override fun getApplicants(
        token: String,
        companyId: String,
        vacancyId: String
    ): Flow<Resource<List<Applicant>>> = jobProviderRepository.getApplicants(token, companyId, vacancyId)

    override fun postAcceptApplicants(
        token: String,
        companyId: String,
        vacancyId: String,
        applicantId: String
    ): Flow<Resource<GeneralResult>> = jobProviderRepository.postAcceptApplicants(
        token, companyId, vacancyId, applicantId
    )

    override fun postRejectApplicants(
        token: String,
        companyId: String,
        vacancyId: String,
        applicantId: String
    ): Flow<Resource<GeneralResult>> = jobProviderRepository.postRejectApplicants(
        token, companyId, vacancyId, applicantId
    )

    override fun updateVacancy(
        companyId: String,
        vacancyId: String,
        request: VacancyRequest
    ): Flow<Resource<GeneralResult>> = jobProviderRepository.updateVacancy(
        companyId, vacancyId, request
    )

    override fun updateJobProvider(
        companyId: String,
        request: JobProviderEditRequest
    ): Flow<Resource<GeneralResult>> = jobProviderRepository.updateJobProvider(
        companyId, request
    )

    override fun updateJobProviderEmail(
        token: String,
        companyId: String,
        request: UpdateEmailRequest
    ): Flow<Resource<GeneralResult>> = jobProviderRepository.updateJobProviderEmail(
        token, companyId, request
    )

    override fun updateJobProviderPassword(
        token: String,
        companyId: String,
        request: UpdatePasswordRequest
    ): Flow<Resource<GeneralResult>> = jobProviderRepository.updateJobProviderPassword(
        token, companyId, request
    )

    override fun updateJobProviderLogo(
        companyId: String,
        logo: MultipartBody.Part
    ): Flow<Resource<UpdateProfilePhotoResult>> = jobProviderRepository.updateJobProviderLogo(
        companyId, logo
    )

    override fun getVacanciesApplicants(
        token: String,
        companyId: String
    ): Flow<Resource<List<Vacancy>>> = jobProviderRepository.getVacanciesApplicants(
        token, companyId
    )

    override fun getApplicantById(
        token: String,
        companyId: String,
        applicantId: String
    ): Flow<Resource<ProfileJobSeeker>> = jobProviderRepository.getApplicantById(
        token, companyId, applicantId
    )
}