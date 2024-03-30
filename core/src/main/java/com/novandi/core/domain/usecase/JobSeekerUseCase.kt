package com.novandi.core.domain.usecase

import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobSeekerEditRequest
import com.novandi.core.data.source.remote.request.JobSeekerRegisterRequest
import com.novandi.core.data.source.remote.request.LoginRequest
import com.novandi.core.data.source.remote.request.UpdateEmailRequest
import com.novandi.core.data.source.remote.request.UpdatePasswordRequest
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.JobApplyStatus
import com.novandi.core.domain.model.LoginResult
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.core.domain.model.RegisterResult
import com.novandi.core.domain.model.UpdateProfilePhotoResult
import com.novandi.core.domain.model.UpdatedJobStatus
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface JobSeekerUseCase {
    fun loginJobSeeker(loginRequest: LoginRequest): Flow<Resource<LoginResult>>
    fun registerJobSeeker(request: JobSeekerRegisterRequest): Flow<Resource<RegisterResult>>
    fun getJobSeeker(token: String, id: String): Flow<Resource<ProfileJobSeeker>>
    fun postJobApply(token: String, userId: String, vacancyId: String): Flow<Resource<GeneralResult>>
    fun getApplyStatus(token: String, userId: String): Flow<Resource<List<JobApplyStatus>>>
    fun updateJobSeeker(token: String, userId: String, request: JobSeekerEditRequest)
        : Flow<Resource<GeneralResult>>
    fun updateJobSeekerEmail(token: String, userId: String, request: UpdateEmailRequest)
        : Flow<Resource<GeneralResult>>
    fun updateJobSeekerPassword(token: String, userId: String, request: UpdatePasswordRequest)
        : Flow<Resource<GeneralResult>>
    fun updateJobSeekerPhoto(userId: String, photo: MultipartBody.Part)
        : Flow<Resource<UpdateProfilePhotoResult>>
    suspend fun getUpdatedJobStatus(token: String, userId: String): List<UpdatedJobStatus>
}