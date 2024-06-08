package com.novandi.core.data.source.local

import com.novandi.core.data.source.local.dao.AssistantDao
import com.novandi.core.data.source.local.dao.SearchDao
import com.novandi.core.data.source.local.entity.AssistantEntity
import com.novandi.core.data.source.local.entity.SearchEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val searchDao: SearchDao,
    private val assistantDao: AssistantDao
) {
    fun getSearch(): Flow<List<SearchEntity>> = searchDao.getSearch()
    fun saveSearch(search: SearchEntity) = searchDao.saveSearch(search)
    fun deleteSearch(id: Int) = searchDao.deleteSearch(id)
    fun getPromptAssistant(userId: String) = assistantDao.getAll(userId)
    fun savePrompt(chat: AssistantEntity) = assistantDao.saveChat(chat)
    fun deletePrompts(userId: String) = assistantDao.deleteAll(userId)
    fun deletePrompt(id: Int) = assistantDao.delete(id)
}