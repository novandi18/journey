package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class VacancyDetailCompanyResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("vacancy")
    var vacancy: VacancyDetailCompanyResponseItem
)

data class VacancyDetailCompanyResponseItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("placement_address")
    val position: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("job_type")
    val jobType: Int,

    @SerializedName("skill_one_name")
    val skillOne: String,

    @SerializedName("skill_two_name")
    val skillTwo: String,

    @SerializedName("disability_name")
    val disability: String,

    @SerializedName("deadline_time")
    val deadlineTime: String,

    @SerializedName("company_name")
    val companyName: String
)
