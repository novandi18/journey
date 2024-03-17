package com.novandi.core.domain.interactor

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
import com.novandi.core.domain.repository.JobSeekerRepository
import com.novandi.core.domain.usecase.JobSeekerUseCase
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class JobSeekerInteractor @Inject constructor(
    private val jobSeekerRepository: JobSeekerRepository
): JobSeekerUseCase {
    override fun loginJobSeeker(loginRequest: LoginRequest): Flow<Resource<LoginResult>> =
        jobSeekerRepository.loginJobSeeker(loginRequest)

    override fun registerJobSeeker(request: JobSeekerRegisterRequest): Flow<Resource<RegisterResult>> =
        jobSeekerRepository.registerJobSeeker(request)

    override fun getJobSeeker(token: String, id: String): Flow<Resource<ProfileJobSeeker>> =
        jobSeekerRepository.getJobSeeker(token, id)

    override fun postJobApply(
        token: String,
        userId: String,
        vacancyId: String
    ): Flow<Resource<GeneralResult>> = jobSeekerRepository.postJobApply(token, userId, vacancyId)

    override fun getApplyStatus(
        token: String,
        userId: String
    ): Flow<Resource<List<JobApplyStatus>>> = jobSeekerRepository.getApplyStatus(token, userId)

    override fun updateJobSeeker(
        token: String,
        userId: String,
        request: JobSeekerEditRequest
    ): Flow<Resource<GeneralResult>> = jobSeekerRepository.updateJobSeeker(
        token, userId, request
    )

    override fun updateJobSeekerEmail(
        token: String,
        userId: String,
        request: UpdateEmailRequest
    ): Flow<Resource<GeneralResult>> = jobSeekerRepository.updateJobSeekerEmail(
        token, userId, request
    )

    override fun updateJobSeekerPassword(
        token: String,
        userId: String,
        request: UpdatePasswordRequest
    ): Flow<Resource<GeneralResult>> = jobSeekerRepository.updateJobSeekerPassword(
        token, userId, request
    )

    override fun updateJobSeekerPhoto(
        userId: String,
        photo: MultipartBody.Part
    ): Flow<Resource<UpdateProfilePhotoResult>> = jobSeekerRepository.updateJobSeekerPhoto(
        userId, photo
    )
}