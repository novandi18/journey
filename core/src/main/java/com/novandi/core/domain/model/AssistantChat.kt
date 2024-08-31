package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class AssistantChat(
    val id: Int? = null,
    val userMessage: String? = null,
    val message: String,
    val isFromMe: Boolean = true,
    val isError: Boolean = false,
    val userId: String
)
