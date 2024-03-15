package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class UpdateEmailRequest(
    @SerializedName("old_email")
    val currentEmail: String,

    @SerializedName("new_email")
    val newEmail: String,

    @SerializedName("password")
    val password: String
)
