package com.novandi.core.domain.model

data class AssistantChat(
    val message: String,
    val isFromMe: Boolean = true
)
