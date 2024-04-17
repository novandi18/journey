package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UpdatedJobStatusItem(
    @SerializedName("vacancy_id")
    val vacancyId: String,

    @SerializedName("position")
    val position: String,

    @SerializedName("company")
    val company: String,

    @SerializedName("status")
    val status: String
)