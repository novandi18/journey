package com.novandi.core.domain.repository

import androidx.paging.PagingData
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.CloseVacancyRequest
import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.request.RecommendationVacanciesRequest
import com.novandi.core.data.source.remote.request.VacancyRequest
import com.novandi.core.domain.model.GeneralResult
import com.novandi.core.domain.model.Vacancy
import kotlinx.coroutines.flow.Flow

interface VacancyRepository {
    fun getVacancies(key: String): Flow<PagingData<Vacancy>>
    fun getLatestVacancies(): Flow<PagingData<Vacancy>>
    fun getPopularVacancies(): Flow<PagingData<Vacancy>>
    fun getVacancy(id: String): Flow<Resource<Vacancy>>
    fun getVacanciesWithoutPager(): Flow<Resource<List<Vacancy>>>
    fun addVacancy(token: String, companyId: String, request: VacancyRequest): Flow<Resource<GeneralResult>>
    fun searchVacancy(position: String): Flow<PagingData<Vacancy>>
    fun getJobProviderVacancies(token: String, companyId: String): Flow<Resource<List<Vacancy>>>
    fun getRecommendation(request: RecommendationRequest): Flow<Resource<List<String>>>
    fun getRecommendationVacancies(recommendations: RecommendationVacanciesRequest): Flow<PagingData<Vacancy>>
    fun closeVacancy(token: String, request: CloseVacancyRequest): Flow<Resource<GeneralResult>>
}