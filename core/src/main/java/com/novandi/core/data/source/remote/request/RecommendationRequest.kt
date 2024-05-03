package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class RecommendationRequest(
    @SerializedName("id_disability")
    val disabilityName: String,

    @SerializedName("skill_one")
    val skillOne: String,

    @SerializedName("skill_two")
    val skillTwo: String
)
