package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class CloseVacancyRequest(
    @SerializedName("companyId")
    val companyId: String,

    @SerializedName("vacancyId")
    val vacancyId: String
)
