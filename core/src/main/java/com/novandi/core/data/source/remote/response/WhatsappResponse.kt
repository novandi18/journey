package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class WhatsappResponse(
    @SerializedName("message_status")
    val messageStatus: String,

    @SerializedName("data")
    val data: WhatsappResponseItems
)

data class WhatsappResponseItems(
    @SerializedName("from")
    val from: String,

    @SerializedName("to")
    val to: Int,

    @SerializedName("status_code")
    val statusCode: Int
)
