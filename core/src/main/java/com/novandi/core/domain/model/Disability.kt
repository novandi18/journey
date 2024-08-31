package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Disability(
    val id: Int,
    val disability: String,
    val en: String
)
