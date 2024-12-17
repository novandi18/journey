package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class RegisterResult(
    val status: String,
    val message: String,
    val id: String
)
