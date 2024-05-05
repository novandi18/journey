package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class WhatsappResponse(
    @SerializedName("data")
    val data: WhatsappResponseItems
)

data class WhatsappResponseItems(
    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String
)
