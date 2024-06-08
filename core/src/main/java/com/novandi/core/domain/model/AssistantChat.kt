package com.novandi.core.domain.model

data class AssistantChat(
    val id: Int? = null,
    val userMessage: String? = null,
    val message: String,
    val isFromMe: Boolean = true,
    val isError: Boolean = false,
    val userId: String
)
