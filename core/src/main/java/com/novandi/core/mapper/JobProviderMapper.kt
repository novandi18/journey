package com.novandi.core.mapper

import com.novandi.core.domain.model.Applicant
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.LoginResult
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.model.RegisterResult
import com.novandi.core.data.source.remote.response.ApplicantItem
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.LoginJobProviderResponse
import com.novandi.core.data.source.remote.response.ProfileJobProviderResponse
import com.novandi.core.data.source.remote.response.RegisterResponse
import com.novandi.core.data.source.remote.response.UpdateProfilePhotoResponse
import com.novandi.core.data.source.remote.response.WhatsappResponse
import com.novandi.core.domain.model.UpdateProfilePhotoResult
import com.novandi.core.domain.model.WhatsappResult
import com.novandi.utility.data.dateFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object JobProviderMapper {
    fun mapLoginResponseToDomain(input: LoginJobProviderResponse): Flow<LoginResult> = flowOf(
        LoginResult(input.status, input.id, input.roleId, input.token)
    )

    fun mapRegisterResponseToDomain(input: RegisterResponse): Flow<RegisterResult> = flowOf(
        RegisterResult(input.message, input.status, input.id)
    )

    fun mapProfileResponseToDomain(input: ProfileJobProviderResponse): Flow<ProfileJobProvider> = flowOf(
        ProfileJobProvider(
            id = input.company.id,
            name = input.company.name,
            address = input.company.address,
            city = input.company.city,
            province = input.company.province,
            logo = input.company.logo,
            employees = input.company.employees,
            email = input.company.email,
            roleId = input.company.roleId,
            sectorName = input.company.sectorName
        )
    )

    fun mapApplicantsToDomain(input: List<ApplicantItem>): Flow<List<Applicant>> = flowOf(
        input.map {
            Applicant(
                id = it.id,
                fullName = it.fullName,
                email = it.email,
                address = it.address,
                profilePhotoUrl = it.profilePhotoUrl,
                gender = it.gender,
                age = it.age,
                phoneNumber = it.phoneNumber,
                appliedAt = dateFormatter(it.appliedAt, true),
                disabilityName = it.disabilityName,
                skillOne = it.skillOne,
                skillTwo = it.skillTwo,
                status = it.status,
                notes = it.notes
            )
        }
    )

    fun mapGeneralResponseToDomain(input: GeneralResponse): Flow<GeneralResult> = flowOf(
        GeneralResult(input.message)
    )

    fun updateProfilePhotoResponseToDomain(input: UpdateProfilePhotoResponse)
        : Flow<UpdateProfilePhotoResult> = flowOf(
            UpdateProfilePhotoResult(input.message, input.imageUrl)
        )

    fun mapWhatsappResponseToDomain(input: WhatsappResponse): Flow<WhatsappResult> = flowOf(
        WhatsappResult(
            status = input.data.statusCode,
            messageStatus = input.messageStatus
        )
    )
}