package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class LoginJobSeekerResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("id_user")
    val id: String,

    @SerializedName("role_id")
    val roleId: Int,

    @SerializedName("token")
    val token: String,

    @SerializedName("disability")
    val disability: String,

    @SerializedName("skill_one")
    val skillOne: String,

    @SerializedName("skill_two")
    val skillTwo: String
)
