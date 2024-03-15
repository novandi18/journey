package com.novandi.core.mapper

import com.novandi.core.data.source.local.entity.SearchEntity
import com.novandi.core.domain.model.Search

object SearchMapper {
    fun mapEntityToDomain(input: List<SearchEntity>): List<Search> =
        input.map { searchEntity ->
            Search(searchEntity.id, searchEntity.keyword)
        }

    fun domainToEntity(input: Search): SearchEntity = SearchEntity(input.id, input.keyword)
}