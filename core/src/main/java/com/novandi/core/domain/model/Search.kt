package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Search(
    val id: Int = 0,
    val keyword: String
)
