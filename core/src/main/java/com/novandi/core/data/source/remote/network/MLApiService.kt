package com.novandi.core.data.source.remote.network

import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.response.VacancyResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface MLApiService {
    @POST("predict")
    suspend fun getRecommendation(
        @Body request: RecommendationRequest,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): VacancyResponse
}