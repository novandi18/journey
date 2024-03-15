package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class JobSeekerRegisterRequest(
    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("skill_one")
    val skillOne: Int,

    @SerializedName("skill_two")
    val skillTwo: Int,

    @SerializedName("id_disability")
    val disabilityId: Int,

    @SerializedName("address")
    val address: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("phone_number")
    val phoneNumber: String
)
