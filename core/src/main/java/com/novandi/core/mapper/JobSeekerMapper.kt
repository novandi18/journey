package com.novandi.core.mapper

import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.JobApplyStatus
import com.novandi.core.domain.model.LoginResult
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.core.domain.model.RegisterResult
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.JobApplyStatusResponse
import com.novandi.core.data.source.remote.response.LoginJobSeekerResponse
import com.novandi.core.data.source.remote.response.ProfileJobSeekerResponse
import com.novandi.core.data.source.remote.response.RegisterResponse
import com.novandi.core.data.source.remote.response.UpdatedJobStatusItem
import com.novandi.core.domain.model.UpdatedJobStatus
import com.novandi.utility.data.dateFormatter
import com.novandi.utility.image.imageProfileUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object JobSeekerMapper {
    fun mapLoginResponseToDomain(input: LoginJobSeekerResponse): Flow<LoginResult> = flowOf(
        LoginResult(input.status, input.id, input.roleId, input.token)
    )

    fun mapRegisterResponseToDomain(input: RegisterResponse): Flow<RegisterResult> = flowOf(
        RegisterResult(input.status, input.message, input.id)
    )

    fun mapProfileResponseToDomain(input: ProfileJobSeekerResponse): Flow<ProfileJobSeeker> = flowOf(
        ProfileJobSeeker(
            id = input.user.id,
            fullName = input.user.fullName,
            email = input.user.email,
            skillOne = input.user.skillOne,
            skillTwo = input.user.skillTwo,
            disabilityName = input.user.disabilityName,
            address = input.user.address,
            gender = input.user.gender,
            age = input.user.age,
            phoneNumber = input.user.phoneNumber,
            profilePhotoUrl = input.user.profilePhotoUrl.imageProfileUrl()
        )
    )

    fun mapGeneralResponseToDomain(input: GeneralResponse): Flow<GeneralResult> = flowOf(
        GeneralResult(input.message)
    )

    fun mapApplyStatusResponseToDomain(input: JobApplyStatusResponse): Flow<List<JobApplyStatus>> =
        flowOf(
            input.data.map {
                JobApplyStatus(
                    id = it.id,
                    vacancyId = it.vacancyId,
                    status = it.status,
                    appliedAt = dateFormatter(it.appliedAt, true),
                    companyName = it.companyName,
                    vacancyPlacementAddress = it.vacancyPlacementAddress,
                    disabilityName = it.disabilityName,
                    skillOne = it.skillOne,
                    skillTwo = it.skillTwo,
                    notes = it.notes
                )
            }
        )

    fun updatedJobStatusResponseToDomain(input: List<UpdatedJobStatusItem>): List<UpdatedJobStatus> =
        input.map {
            UpdatedJobStatus(it.vacancyId, it.position, it.company, it.status)
        }
}