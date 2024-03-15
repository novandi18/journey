package com.novandi.core.domain.usecase

import com.novandi.core.domain.model.Search
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {
    fun getSearch(): Flow<List<Search>>
    fun saveSearch(search: Search)
    fun deleteSearch(id: Int)
}