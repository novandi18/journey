package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class MessagingRequest(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("messageTitle")
    val messageTitle: String,

    @SerializedName("messageBody")
    val messageBody: String
)
