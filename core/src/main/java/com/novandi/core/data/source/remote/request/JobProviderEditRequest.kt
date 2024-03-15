package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class JobProviderEditRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("province")
    val province: String,

    @SerializedName("employees")
    val employees: Int,

    @SerializedName("id_sector")
    val sectorId: Int
)
