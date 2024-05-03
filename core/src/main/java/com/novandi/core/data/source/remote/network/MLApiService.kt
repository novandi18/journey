package com.novandi.core.data.source.remote.network

import com.novandi.core.data.source.remote.request.RecommendationRequest
import com.novandi.core.data.source.remote.response.RecommendationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MLApiService {
    @POST("predict")
    suspend fun getRecommendation(
        @Body request: RecommendationRequest
    ): RecommendationResponse
}