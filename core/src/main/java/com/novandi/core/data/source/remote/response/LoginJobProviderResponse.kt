package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class LoginJobProviderResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("id_company")
    val id: String,

    @SerializedName("role_id")
    val roleId: Int,

    @SerializedName("token")
    val token: String
)
