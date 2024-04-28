package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateCvResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("cv")
    val cv: String
)
