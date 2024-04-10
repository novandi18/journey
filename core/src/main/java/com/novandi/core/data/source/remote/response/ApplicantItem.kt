package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ApplicantItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("age")
    val age: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("applied_at")
    val appliedAt: String,

    @SerializedName("disability_name")
    val disabilityName: String,

    @SerializedName("skill_one_name")
    val skillOne: String,

    @SerializedName("skill_two_name")
    val skillTwo: String,

    @SerializedName("status")
    val status: String
)
