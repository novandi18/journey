package com.novandi.core.data.source.local

import com.novandi.core.data.source.local.dao.SearchDao
import com.novandi.core.data.source.local.entity.SearchEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val searchDao: SearchDao
) {
    fun getSearch(): Flow<List<SearchEntity>> = searchDao.getSearch()
    fun saveSearch(search: SearchEntity) = searchDao.saveSearch(search)
    fun deleteSearch(id: Int) = searchDao.deleteSearch(id)
}