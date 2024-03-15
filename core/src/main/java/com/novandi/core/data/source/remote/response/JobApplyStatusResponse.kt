package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class JobApplyStatusResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: List<JobApplyStatusItem>
)

data class JobApplyStatusItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("id_vacancy")
    val vacancyId: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("applied_at")
    val appliedAt: String,

    @SerializedName("company_name")
    val companyName: String,

    @SerializedName("vacancy_placement_address")
    val vacancyPlacementAddress: String,

    @SerializedName("disability_name")
    val disabilityName: String,

    @SerializedName("skill_one_name")
    val skillOne: String,

    @SerializedName("skill_two_name")
    val skillTwo: String
)
