package com.novandi.core.data.source.local

import com.novandi.core.data.source.local.dao.AllVacancyDao
import com.novandi.core.data.source.local.dao.AssistantDao
import com.novandi.core.data.source.local.dao.LatestVacancyDao
import com.novandi.core.data.source.local.dao.SearchDao
import com.novandi.core.data.source.local.dao.PopularVacancyDao
import com.novandi.core.data.source.local.dao.RecommendationVacancyDao
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.local.entity.AssistantEntity
import com.novandi.core.data.source.local.entity.LatestVacancyEntity
import com.novandi.core.data.source.local.entity.SearchEntity
import com.novandi.core.data.source.local.entity.PopularVacancyEntity
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val searchDao: SearchDao,
    private val assistantDao: AssistantDao,
    private val recommendationVacancyDao: RecommendationVacancyDao,
    private val popularVacancyDao: PopularVacancyDao,
    private val latestVacancyDao: LatestVacancyDao,
    private val allVacancyDao: AllVacancyDao
) {
    fun getSearch(): Flow<List<SearchEntity>> = searchDao.getSearch()
    fun saveSearch(search: SearchEntity) = searchDao.saveSearch(search)
    fun deleteSearch(id: Int) = searchDao.deleteSearch(id)
    fun getPromptAssistant(userId: String) = assistantDao.getAll(userId)
    fun savePrompt(chat: AssistantEntity) = assistantDao.saveChat(chat)
    fun deletePrompts(userId: String) = assistantDao.deleteAll(userId)
    fun deletePrompt(id: Int) = assistantDao.delete(id)
    fun getPopularVacancies() = popularVacancyDao.getAll()
    fun insertPopularVacancy(vacancies: List<PopularVacancyEntity>) =
        popularVacancyDao.insertAll(vacancies)
    fun deletePopularVacancies() = popularVacancyDao.deleteAll()
    fun getLatestVacancies() = latestVacancyDao.getAll()
    fun insertLatestVacancy(vacancies: List<LatestVacancyEntity>) =
        latestVacancyDao.insertAll(vacancies)
    fun deleteLatestVacancies() = latestVacancyDao.deleteAll()
    fun getAllVacancies() = allVacancyDao.getAll()
    fun insertAllVacancies(vacancies: List<AllVacancyEntity>) = allVacancyDao.insertAll(vacancies)
    fun deleteAllVacancies() = allVacancyDao.deleteAll()
    fun getRecommendationVacancies() = recommendationVacancyDao.getAll()
    fun insertRecommendationVacancies(vacancies: List<RecommendationVacancyEntity>) =
        recommendationVacancyDao.insertAll(vacancies)
    fun deleteRecommendationVacancies() = recommendationVacancyDao.deleteAll()
}