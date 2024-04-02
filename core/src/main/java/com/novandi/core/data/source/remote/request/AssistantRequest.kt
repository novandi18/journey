package com.novandi.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class AssistantRequest(
    @SerializedName("prompt")
    val prompt: String
)
