package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class WhatsappResult(
    val status: Int,
    val messageStatus: String
)
