package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateProfilePhotoResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("imageUrl")
    val imageUrl: String
)