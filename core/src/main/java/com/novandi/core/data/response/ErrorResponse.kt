package com.novandi.core.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message")
    val message: String,
    val status: String? = "Error"
)
