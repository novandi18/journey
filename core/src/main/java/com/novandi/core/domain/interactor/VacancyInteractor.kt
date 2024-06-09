package com.novandi.core.domain.interactor

import androidx.paging.PagingData
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.local.entity.LatestVacancyEntity
import com.novandi.core.data.source.local.entity.PopularVacancyEntity
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity
import com.novandi.core.data.source.remote.request.CloseVacancyRequest
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.request.RecommendationVacanciesRequest
import com.novandi.core.data.source.remote.request.VacanciesSearchRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.data.source.remote.request.WhatsappRequest
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.model.VacancyDetailCompany
import com.novandi.core.domain.model.VacancyDetailUser
import com.novandi.core.domain.model.WhatsappResult
import com.novandi.core.domain.repository.VacancyRepository
import com.novandi.core.domain.usecase.VacancyUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VacancyInteractor @Inject constructor(
    private val vacancyRepository: VacancyRepository
): VacancyUseCase {
    override fun getVacancies(key: String): Flow<PagingData<AllVacancyEntity>> =
        vacancyRepository.getVacancies(key)

    override fun getLatestVacancies(): Flow<PagingData<LatestVacancyEntity>> =
        vacancyRepository.getLatestVacancies()

    override fun getPopularVacancies(): Flow<PagingData<PopularVacancyEntity>>
        = vacancyRepository.getPopularVacancies()

    override fun getVacancyInUser(
        vacancyId: String,
        userId: String
    ): Flow<Resource<VacancyDetailUser>> = vacancyRepository.getVacancyInUser(vacancyId, userId)

    override fun getVacancyInCompany(vacancyId: String): Flow<Resource<VacancyDetailCompany>> =
        vacancyRepository.getVacancyInCompany(vacancyId)

    override fun getVacanciesWithoutPager(): Flow<Resource<List<Vacancy>>> =
        vacancyRepository.getVacanciesWithoutPager()

    override fun addVacancy(
        token: String,
        companyId: String,
        request: VacancyRequest
    ): Flow<Resource<GeneralResult>> = vacancyRepository.addVacancy(token, companyId, request)

    override fun searchVacancy(position: String, request: VacanciesSearchRequest): Flow<PagingData<Vacancy>> =
        vacancyRepository.searchVacancy(position, request)

    override fun getJobProviderVacancies(
        token: String,
        companyId: String
    ): Flow<Resource<List<Vacancy>>> = vacancyRepository.getJobProviderVacancies(token, companyId)

    override fun getRecommendation(request: RecommendationRequest): Flow<Resource<List<String>>> =
        vacancyRepository.getRecommendation(request)

    override fun getRecommendationVacancies(recommendations: RecommendationVacanciesRequest)
    : Flow<PagingData<RecommendationVacancyEntity>> =
        vacancyRepository.getRecommendationVacancies(recommendations)

    override fun closeVacancy(token: String, request: CloseVacancyRequest): Flow<Resource<GeneralResult>> =
        vacancyRepository.closeVacancy(token, request)

    override fun getVacancyCompany(companyId: String): Flow<Resource<ProfileJobProvider>> =
        vacancyRepository.getVacancyCompany(companyId)

    override fun sendWhatsappMessage(request: WhatsappRequest): Flow<Resource<WhatsappResult>> =
        vacancyRepository.sendWhatsappMessage(request)
}