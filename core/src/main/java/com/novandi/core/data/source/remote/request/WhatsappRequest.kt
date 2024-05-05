package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class WhatsappRequest(
    @SerializedName("session")
    val session: String = "sesisatu",

    @SerializedName("to")
    val to: String,

    @SerializedName("text")
    val messages: String
)
