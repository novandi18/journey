package com.novandi.core.domain.repository

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
import com.novandi.core.domain.model.RegisterResult
import kotlinx.coroutines.flow.Flow

interface JobProviderRepository {
    fun loginJobProvider(loginRequest: LoginRequest): Flow<Resource<LoginResult>>
    fun registerJobProvider(request: JobProviderRegisterRequest): Flow<Resource<RegisterResult>>
    fun getJobProvider(token: String, id: String): Flow<Resource<ProfileJobProvider>>
    fun getApplicants(token: String, companyId: String, vacancyId: String): Flow<Resource<List<Applicant>>>
    fun postAcceptApplicants(token: String, companyId: String, vacancyId: String, applicantId: String)
        : Flow<Resource<GeneralResult>>
    fun postRejectApplicants(token: String, companyId: String, vacancyId: String, applicantId: String)
        : Flow<Resource<GeneralResult>>
    fun updateVacancy(companyId: String, vacancyId: String, request: VacancyRequest)
        : Flow<Resource<GeneralResult>>
    fun updateJobProvider(companyId: String, request: JobProviderEditRequest)
        : Flow<Resource<GeneralResult>>
    fun updateJobProviderEmail(token: String, companyId: String, request: UpdateEmailRequest)
        : Flow<Resource<GeneralResult>>
    fun updateJobProviderPassword(token: String, companyId: String, request: UpdatePasswordRequest)
            : Flow<Resource<GeneralResult>>
}