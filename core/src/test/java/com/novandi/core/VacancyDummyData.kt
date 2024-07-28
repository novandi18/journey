package com.novandi.core

import com.novandi.core.data.source.remote.response.VacancyItem
import com.novandi.core.data.source.remote.response.VacancyResponse

object VacancyDummyData {
    fun get(page: Int = 1, limit: Int = 10): VacancyResponse {
        val vacancies = mutableListOf<VacancyItem>()
        for (i in 1..limit) {
            val vacancyId = (page - 1) * limit + i
            vacancies.add(
                VacancyItem(
                    id = vacancyId.toString(),
                    placementAddress = "City $vacancyId",
                    description = "Description $vacancyId",
                    createdAt = "2023-06-07T23:48:31.725Z",
                    updatedAt = "2023-06-07T23:48:31.725Z",
                    deadlineTime = "2023-06-07T23:48:31.725Z",
                    jobType = vacancyId % 3,
                    skillOne = "Skill ${vacancyId * 2}",
                    skillTwo = "Skill ${vacancyId * 2 + 1}",
                    disabilityName = if (vacancyId % 2 == 0) "Disability A" else "Disability B",
                    companyLogo = "logo${vacancyId}.png",
                    sectorName = if (vacancyId % 3 == 0) "IT" else "Finance",
                    companyName = "Company ${'A' + vacancyId % 5}",
                    totalApplicants = vacancyId * 10,
                    companyId = "company${vacancyId}"
                )
            )
        }
        return VacancyResponse(
            status = "success",
            vacancies = vacancies
        )
    }
}