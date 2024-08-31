package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class AssistantResult(
    val user: String,
    val bot: String
)