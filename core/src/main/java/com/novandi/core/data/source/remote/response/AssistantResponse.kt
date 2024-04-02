package com.novandi.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AssistantResponse(
    @SerializedName("user")
    val user: String,

    @SerializedName("bot")
    val bot: String
)
