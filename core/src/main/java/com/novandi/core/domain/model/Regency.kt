package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Regency(
    val id: String,
    val city: String,
    val provinceId: String
)
