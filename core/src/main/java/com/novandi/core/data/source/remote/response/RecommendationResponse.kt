package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(
    @SerializedName("predictions")
    val predictions: List<String>
)