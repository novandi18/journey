package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("old_password")
    val oldPassword: String,

    @SerializedName("new_password")
    val newPassword: String
)
