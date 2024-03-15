package com.novandi.core.data.repository

import com.novandi.core.data.source.local.LocalDataSource
import com.novandi.core.domain.model.Search
import com.novandi.core.domain.repository.SearchRepository
import com.novandi.core.mapper.SearchMapper
import com.novandi.utility.data.AppExecutors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): SearchRepository {
    override fun getSearch(): Flow<List<Search>> = localDataSource.getSearch().map {
        SearchMapper.mapEntityToDomain(it)
    }

    override fun saveSearch(search: Search) {
        val entity = SearchMapper.domainToEntity(search)
        appExecutors.diskIO().execute {
            localDataSource.saveSearch(entity)
        }
    }

    override fun deleteSearch(id: Int) {
        appExecutors.diskIO().execute {
            localDataSource.deleteSearch(id)
        }
    }
}