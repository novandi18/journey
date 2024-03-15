package com.novandi.core.domain.repository

import com.novandi.core.domain.model.Search
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getSearch(): Flow<List<Search>>
    fun saveSearch(search: Search)
    fun deleteSearch(id: Int)
}