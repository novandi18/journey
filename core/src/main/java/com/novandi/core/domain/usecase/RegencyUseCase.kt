package com.novandi.core.domain.usecase

import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.Regency
import kotlinx.coroutines.flow.Flow

interface RegencyUseCase {
    fun getRegency(provinceId: String): Flow<Resource<List<Regency>>>
}