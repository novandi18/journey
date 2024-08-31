package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class UpdateCvResult(
    val message: String,
    val cv: String
)
