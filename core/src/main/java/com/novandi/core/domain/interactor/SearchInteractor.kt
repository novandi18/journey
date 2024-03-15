package com.novandi.core.domain.interactor

import com.novandi.core.domain.model.Search
import com.novandi.core.domain.repository.SearchRepository
import com.novandi.core.domain.usecase.SearchUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchInteractor @Inject constructor(
    private val searchRepository: SearchRepository
): SearchUseCase {
    override fun getSearch(): Flow<List<Search>> = searchRepository.getSearch()

    override fun saveSearch(search: Search) = searchRepository.saveSearch(search)

    override fun deleteSearch(id: Int) = searchRepository.deleteSearch(id)
}