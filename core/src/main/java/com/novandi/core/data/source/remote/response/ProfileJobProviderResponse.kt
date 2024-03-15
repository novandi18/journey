package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileJobProviderResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("company")
    val company: ProfileJobProviderItem
)

data class ProfileJobProviderItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("province")
    val province: String,

    @SerializedName("logo")
    val logo: String,

    @SerializedName("employees")
    val employees: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("roleId")
    val roleId: Int,

    @SerializedName("sector_name")
    val sectorName: String
)