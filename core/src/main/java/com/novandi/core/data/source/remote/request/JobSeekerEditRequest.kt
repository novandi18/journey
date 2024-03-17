package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class JobSeekerEditRequest(
    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("id_disability")
    val disabilityId: Int,

    @SerializedName("address")
    val address: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("skill_one")
    val skillOne: Int,

    @SerializedName("skill_two")
    val skillTwo: Int
)
