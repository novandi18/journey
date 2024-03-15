package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RegencyItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("province_id")
    val provinceId: String
)
