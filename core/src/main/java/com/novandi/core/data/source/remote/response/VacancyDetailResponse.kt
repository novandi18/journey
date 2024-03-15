package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class VacancyDetailResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("vacancy")
    var vacancy: VacancyItem
)
