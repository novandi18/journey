package com.novandi.core.domain.repository

import androidx.paging.PagingData
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.local.entity.LatestVacancyEntity
import com.novandi.core.data.source.local.entity.PopularVacancyEntity
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity
import com.novandi.core.data.source.remote.request.CloseVacancyRequest
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.request.VacanciesSearchRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.data.source.remote.request.WhatsappRequest
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.domain.model.VacancyDetailCompany
import com.novandi.core.domain.model.VacancyDetailUser
import com.novandi.core.domain.model.WhatsappResult
import kotlinx.coroutines.flow.Flow

interface VacancyRepository {
    fun getVacancies(key: String): Flow<PagingData<AllVacancyEntity>>
    fun getLatestVacancies(): Flow<PagingData<LatestVacancyEntity>>
    fun getPopularVacancies(): Flow<PagingData<PopularVacancyEntity>>
    fun getVacancyInUser(vacancyId: String, userId: String): Flow<Resource<VacancyDetailUser>>
    fun getVacancyInCompany(vacancyId: String): Flow<Resource<VacancyDetailCompany>>
    fun getVacanciesWithoutPager(): Flow<Resource<List<Vacancy>>>
    fun addVacancy(token: String, companyId: String, request: VacancyRequest): Flow<Resource<GeneralResult>>
    fun searchVacancy(position: String, request: VacanciesSearchRequest): Flow<PagingData<Vacancy>>
    fun getJobProviderVacancies(token: String, companyId: String): Flow<Resource<List<Vacancy>>>
    fun getRecommendation(request: RecommendationRequest): Flow<PagingData<RecommendationVacancyEntity>>
    fun closeVacancy(token: String, request: CloseVacancyRequest): Flow<Resource<GeneralResult>>
    fun getVacancyCompany(companyId: String): Flow<Resource<ProfileJobProvider>>
    fun sendWhatsappMessage(request: WhatsappRequest): Flow<Resource<WhatsappResult>>
}