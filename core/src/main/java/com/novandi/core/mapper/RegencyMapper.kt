package com.novandi.core.mapper

import com.novandi.core.domain.model.Regency
import com.novandi.core.data.source.remote.response.RegencyItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object RegencyMapper {
    fun mapListRegencyToDomain(input: List<RegencyItem>): Flow<List<Regency>> = flowOf(
        input.map { regency ->
            Regency(regency.id, regency.name, regency.provinceId)
        }
    )
}