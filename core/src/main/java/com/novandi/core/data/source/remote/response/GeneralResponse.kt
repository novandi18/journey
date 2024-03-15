package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("message")
    val message: String
)
