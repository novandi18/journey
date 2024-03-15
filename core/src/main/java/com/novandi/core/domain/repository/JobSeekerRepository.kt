package com.novandi.core.domain.repository

import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobSeekerRegisterRequest
import com.novandi.core.data.source.remote.request.LoginRequest
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.JobApplyStatus
import com.novandi.core.domain.model.LoginResult
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.core.domain.model.RegisterResult
import kotlinx.coroutines.flow.Flow

interface JobSeekerRepository {
    fun loginJobSeeker(loginRequest: LoginRequest): Flow<Resource<LoginResult>>
    fun registerJobSeeker(request: JobSeekerRegisterRequest): Flow<Resource<RegisterResult>>
    fun getJobSeeker(token: String, id: String): Flow<Resource<ProfileJobSeeker>>
    fun postJobApply(token: String, userId: String, vacancyId: String): Flow<Resource<GeneralResult>>
    fun getApplyStatus(token: String, userId: String): Flow<Resource<List<JobApplyStatus>>>
}