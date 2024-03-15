package com.novandi.core.domain.repository

import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.Regency
import kotlinx.coroutines.flow.Flow

interface RegencyRepository {
    fun getRegency(provinceId: String): Flow<Resource<List<Regency>>>
}