package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class AcceptApplicantRequest(
    @SerializedName("notes")
    val notes: String
)
