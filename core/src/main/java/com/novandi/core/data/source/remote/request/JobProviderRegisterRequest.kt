package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class JobProviderRegisterRequest(
    @SerializedName("email")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("province")
    val province: String,

    @SerializedName("employees")
    val employees: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("id_sector")
    val sectorId: Int
)