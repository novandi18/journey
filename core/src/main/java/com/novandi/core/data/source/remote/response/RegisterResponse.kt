package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("id")
    val id: String
)
