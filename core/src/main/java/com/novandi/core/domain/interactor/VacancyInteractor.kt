package com.novandi.core.domain.interactor

import androidx.paging.PagingData
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.repository.VacancyRepository
import com.novandi.core.domain.usecase.VacancyUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VacancyInteractor @Inject constructor(
    private val vacancyRepository: VacancyRepository
): VacancyUseCase {
    override fun getVacancies(key: String): Flow<PagingData<Vacancy>> =
        vacancyRepository.getVacancies(key)

    override fun getLatestVacancies(): Flow<PagingData<Vacancy>> =
        vacancyRepository.getLatestVacancies()

    override fun getPopularVacancies(): Flow<PagingData<Vacancy>> = vacancyRepository.getPopularVacancies()

    override fun getVacancy(id: String): Flow<Resource<Vacancy>> = vacancyRepository.getVacancy(id)

    override fun getVacanciesWithoutPager(): Flow<Resource<List<Vacancy>>> =
        vacancyRepository.getVacanciesWithoutPager()

    override fun addVacancy(
        token: String,
        companyId: String,
        request: VacancyRequest
    ): Flow<Resource<GeneralResult>> = vacancyRepository.addVacancy(token, companyId, request)

    override fun searchVacancy(position: String): Flow<PagingData<Vacancy>> =
        vacancyRepository.searchVacancy(position)

    override fun getJobProviderVacancies(
        token: String,
        companyId: String
    ): Flow<Resource<List<Vacancy>>> = vacancyRepository.getJobProviderVacancies(token, companyId)
}