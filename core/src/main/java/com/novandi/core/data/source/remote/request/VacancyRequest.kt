package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class VacancyRequest(
    @SerializedName("placement_address")
    val placementAddress: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("id_disability")
    val disabilityId: Int,

    @SerializedName("skill_one")
    val skillOne: String,

    @SerializedName("skill_two")
    val skillTwo: String,

    @SerializedName("job_type")
    val jobType: Int,

    @SerializedName("deadline_time")
    val deadlineTime: String
)
