package com.novandi.core.domain.interactor

import com.novandi.core.data.response.Resource
import com.novandi.core.domain.repository.RegencyRepository
import com.novandi.core.domain.usecase.RegencyUseCase
import com.novandi.core.domain.model.Regency
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegencyInteractor @Inject constructor(
    private val regencyRepository: RegencyRepository
): RegencyUseCase {
    override fun getRegency(provinceId: String): Flow<Resource<List<Regency>>> =
        regencyRepository.getRegency(provinceId)
}