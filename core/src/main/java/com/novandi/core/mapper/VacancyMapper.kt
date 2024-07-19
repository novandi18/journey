package com.novandi.core.mapper

import com.novandi.core.consts.JobTypes
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.local.entity.LatestVacancyEntity
import com.novandi.core.data.source.local.entity.PopularVacancyEntity
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.VacancyDetailCompanyResponse
import com.novandi.core.data.source.remote.response.VacancyDetailUserResponse
import com.novandi.core.data.source.remote.response.VacancyResponse
import com.novandi.core.domain.model.VacancyDetailCompany
import com.novandi.core.domain.model.VacancyDetailUser
import com.novandi.utility.data.dateFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object VacancyMapper {
    fun mapResponseToDomain(input: VacancyResponse): List<Vacancy> =
        input.vacancies.map {
            Vacancy(
                id = it.id,
                placementAddress = it.placementAddress,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                deadlineTime = dateFormatter(it.deadlineTime),
                jobType = it.jobType,
                skillOne = it.skillOne,
                skillTwo = it.skillTwo,
                disabilityName = it.disabilityName,
                companyLogo = it.companyLogo,
                sectorName = it.sectorName,
                companyName = it.companyName
            )
        }

    fun mapDetailUserToDomain(input: VacancyDetailUserResponse): Flow<VacancyDetailUser> = flowOf(
        VacancyDetailUser(
            vacancyId = input.vacancy.vacancyId,
            position = input.vacancy.position,
            description = input.vacancy.description,
            deadlineTime = dateFormatter(input.vacancy.deadlineTime),
            jobType = JobTypes.types()[input.vacancy.jobType - 1],
            disabilityName = input.vacancy.disabilityName,
            skillOne = input.vacancy.skillOne,
            skillTwo = input.vacancy.skillTwo,
            companyId = input.vacancy.companyId,
            companyName = input.vacancy.companyName,
            companyLogo = input.vacancy.companyLogo,
            companySector = input.vacancy.companySector,
            userCv = input.vacancy.userCv,
            statusApply = input.vacancy.statusApply,
            notesApply = input.vacancy.notesApply,
            dateTimeApply = input.vacancy.dateTimeApply
        )
    )

    fun mapDetailCompanyToDomain(input: VacancyDetailCompanyResponse): Flow<VacancyDetailCompany> = flowOf(
        VacancyDetailCompany(
            id = input.vacancy.id,
            position = input.vacancy.position,
            description = input.vacancy.description,
            updatedAt = input.vacancy.updatedAt,
            jobType = JobTypes.types()[input.vacancy.jobType - 1],
            skillOne = input.vacancy.skillOne,
            skillTwo = input.vacancy.skillTwo,
            disability = input.vacancy.disability,
            deadlineTime = dateFormatter(input.vacancy.deadlineTime),
            companyName = input.vacancy.companyName
        )
    )

    fun mapNoPagerResponseToDomain(input: VacancyResponse): Flow<List<Vacancy>> = flowOf(
        input.vacancies.map {
            Vacancy(
                id = it.id,
                placementAddress = it.placementAddress,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                deadlineTime = dateFormatter(it.deadlineTime),
                jobType = it.jobType,
                skillOne = it.skillOne,
                skillTwo = it.skillTwo,
                disabilityName = it.disabilityName,
                companyLogo = it.companyLogo,
                sectorName = it.sectorName,
                companyName = it.companyName,
                totalApplicants = it.totalApplicants
            )
        }
    )

    fun mapGeneralResponseToDomain(input: GeneralResponse): Flow<GeneralResult> = flowOf(
        GeneralResult(input.message)
    )

    fun mapResponseToPopularVacancyEntity(input: VacancyResponse): List<PopularVacancyEntity> =
        input.vacancies.map {
            PopularVacancyEntity(
                vacancyId = it.id,
                placementAddress = it.placementAddress,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                deadlineTime = dateFormatter(it.deadlineTime),
                jobType = it.jobType,
                skillOne = it.skillOne,
                skillTwo = it.skillTwo,
                disabilityName = it.disabilityName,
                companyLogo = it.companyLogo,
                sectorName = it.sectorName,
                companyName = it.companyName,
                totalApplicants = it.totalApplicants,
                companyId = it.companyId
            )
        }

    fun mapPopularVacancyEntityToDomain(input: PopularVacancyEntity): Vacancy =
        Vacancy(
            id = input.vacancyId,
            placementAddress = input.placementAddress,
            description = input.description,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
            deadlineTime = input.deadlineTime,
            jobType = input.jobType,
            skillOne = input.skillOne,
            skillTwo = input.skillTwo,
            disabilityName = input.disabilityName,
            companyLogo = input.companyLogo,
            sectorName = input.sectorName,
            companyName = input.companyName,
            totalApplicants = input.totalApplicants,
            companyId = input.companyId ?: ""
        )

    fun mapResponseToLatestVacancyEntity(input: VacancyResponse): List<LatestVacancyEntity> =
        input.vacancies.map {
            LatestVacancyEntity(
                vacancyId = it.id,
                placementAddress = it.placementAddress,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                deadlineTime = dateFormatter(it.deadlineTime),
                jobType = it.jobType,
                skillOne = it.skillOne,
                skillTwo = it.skillTwo,
                disabilityName = it.disabilityName,
                companyLogo = it.companyLogo,
                sectorName = it.sectorName,
                companyName = it.companyName,
                totalApplicants = it.totalApplicants,
                companyId = it.companyId
            )
        }

    fun mapLatestVacancyEntityToDomain(input: LatestVacancyEntity): Vacancy =
        Vacancy(
            id = input.vacancyId,
            placementAddress = input.placementAddress,
            description = input.description,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
            deadlineTime = input.deadlineTime,
            jobType = input.jobType,
            skillOne = input.skillOne,
            skillTwo = input.skillTwo,
            disabilityName = input.disabilityName,
            companyLogo = input.companyLogo,
            sectorName = input.sectorName,
            companyName = input.companyName,
            totalApplicants = input.totalApplicants,
            companyId = input.companyId ?: ""
        )

    fun mapResponseToAllVacancyEntity(input: VacancyResponse): List<AllVacancyEntity> =
        input.vacancies.map {
            AllVacancyEntity(
                vacancyId = it.id,
                placementAddress = it.placementAddress,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                deadlineTime = dateFormatter(it.deadlineTime),
                jobType = it.jobType,
                skillOne = it.skillOne,
                skillTwo = it.skillTwo,
                disabilityName = it.disabilityName,
                companyLogo = it.companyLogo,
                sectorName = it.sectorName,
                companyName = it.companyName,
                totalApplicants = it.totalApplicants,
                companyId = it.companyId
            )
        }

    fun mapAllVacancyEntityToDomain(input: AllVacancyEntity): Vacancy =
        Vacancy(
            id = input.vacancyId,
            placementAddress = input.placementAddress,
            description = input.description,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
            deadlineTime = input.deadlineTime,
            jobType = input.jobType,
            skillOne = input.skillOne,
            skillTwo = input.skillTwo,
            disabilityName = input.disabilityName,
            companyLogo = input.companyLogo,
            sectorName = input.sectorName,
            companyName = input.companyName,
            totalApplicants = input.totalApplicants,
            companyId = input.companyId ?: ""
        )

    fun mapResponseToRecommendationVacancyEntity(input: VacancyResponse): List<RecommendationVacancyEntity> =
        input.vacancies.map {
            RecommendationVacancyEntity(
                vacancyId = it.id,
                placementAddress = it.placementAddress,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                deadlineTime = dateFormatter(it.deadlineTime),
                jobType = it.jobType,
                skillOne = it.skillOne,
                skillTwo = it.skillTwo,
                disabilityName = it.disabilityName,
                companyLogo = it.companyLogo,
                sectorName = it.sectorName,
                companyName = it.companyName,
                totalApplicants = it.totalApplicants,
                companyId = it.companyId
            )
        }

    fun mapRecommendationVacancyEntityToDomain(input: RecommendationVacancyEntity): Vacancy =
        Vacancy(
            id = input.vacancyId,
            placementAddress = input.placementAddress,
            description = input.description,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
            deadlineTime = input.deadlineTime,
            jobType = input.jobType,
            skillOne = input.skillOne,
            skillTwo = input.skillTwo,
            disabilityName = input.disabilityName,
            companyLogo = input.companyLogo,
            sectorName = input.sectorName,
            companyName = input.companyName,
            totalApplicants = input.totalApplicants,
            companyId = input.companyId ?: ""
        )

    fun mapGeneralResponseToDomainNoFlow(input: GeneralResponse): GeneralResult =
        GeneralResult(input.message)
}