package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class RecommendationVacanciesRequest(
    @SerializedName("recommendations")
    val recommendations: List<String>
)
