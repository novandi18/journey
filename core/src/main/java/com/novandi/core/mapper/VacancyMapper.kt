package com.novandi.core.mapper

import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.data.source.remote.response.GeneralResponse
import com.novandi.core.data.source.remote.response.RecommendationResponse
import com.novandi.core.data.source.remote.response.VacancyDetailResponse
import com.novandi.core.data.source.remote.response.VacancyResponse
import com.novandi.utility.data.dateFormatter
import com.novandi.utility.image.imageProfileUrl
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
                companyLogo = it.companyLogo.imageProfileUrl(),
                sectorName = it.sectorName,
                companyName = it.companyName
            )
        }

    fun mapDetailResponseToDomain(input: VacancyDetailResponse): Flow<Vacancy> = flowOf(
        Vacancy(
            id = input.vacancy.id,
            placementAddress = input.vacancy.placementAddress,
            description = input.vacancy.description,
            createdAt = input.vacancy.createdAt,
            updatedAt = input.vacancy.createdAt,
            deadlineTime = dateFormatter(input.vacancy.deadlineTime),
            jobType = input.vacancy.jobType,
            skillOne = input.vacancy.skillOne,
            skillTwo = input.vacancy.skillTwo,
            disabilityName = input.vacancy.disabilityName,
            companyLogo = input.vacancy.companyLogo.imageProfileUrl(),
            sectorName = input.vacancy.sectorName,
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
                companyLogo = it.companyLogo.imageProfileUrl(),
                sectorName = it.sectorName,
                companyName = it.companyName,
                totalApplicants = it.totalApplicants
            )
        }
    )

    fun mapGeneralResponseToDomain(input: GeneralResponse): Flow<GeneralResult> = flowOf(
        GeneralResult(input.message)
    )

    fun recommendationToList(input: RecommendationResponse): Flow<List<String>> = flowOf(
        input.predictions
    )
}