package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class VacancyDetailUserResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("vacancy")
    var vacancy: VacancyDetailUserItem
)

data class VacancyDetailUserItem(
    @SerializedName("id")
    val vacancyId: String,

    @SerializedName("placement_address")
    val position: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("deadline_time")
    val deadlineTime: String,

    @SerializedName("job_type")
    val jobType: Int,

    @SerializedName("disability_name")
    val disabilityName: String,

    @SerializedName("skill_one_name")
    val skillOne: String,

    @SerializedName("skill_two_name")
    val skillTwo: String,

    @SerializedName("company_id")
    val companyId: String,

    @SerializedName("company_name")
    val companyName: String,

    @SerializedName("company_logo")
    val companyLogo: String,

    @SerializedName("company_sector")
    val companySector: String,

    @SerializedName("user_cv")
    val userCv: String? = null,

    @SerializedName("status_apply")
    val statusApply: String? = null,

    @SerializedName("notes_apply")
    val notesApply: String? = null,

    @SerializedName("datetime_apply")
    val dateTimeApply: String? = null,
)
