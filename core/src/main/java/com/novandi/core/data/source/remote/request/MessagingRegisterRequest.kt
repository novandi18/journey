package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class MessagingRegisterRequest(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("token")
    val token: String
)
