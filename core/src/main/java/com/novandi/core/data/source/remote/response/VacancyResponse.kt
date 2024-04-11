package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class VacancyResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("vacancies")
    var vacancies: List<VacancyItem>
)

data class VacancyItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("placement_address")
    val placementAddress: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deadline_time")
    val deadlineTime: String,

    @SerializedName("job_type")
    val jobType: Int,

    @SerializedName("skill_one_name")
    val skillOne: String,

    @SerializedName("skill_two_name")
    val skillTwo: String,

    @SerializedName("disability_name")
    val disabilityName: String,

    @SerializedName("company_logo")
    val companyLogo: String,

    @SerializedName("sector_name")
    val sectorName: String,

    @SerializedName("company_name")
    val companyName: String,

    @SerializedName("total_applicants")
    val totalApplicants: Int? = null
)
