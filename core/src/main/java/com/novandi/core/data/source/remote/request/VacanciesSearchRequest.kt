package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class VacanciesSearchRequest(
    @SerializedName("jobType_filter")
    val jobType: String? = null,

    @SerializedName("disability_filter")
    val disability: String? = null,

    @SerializedName("province_filter")
    val province: String? = null
)
