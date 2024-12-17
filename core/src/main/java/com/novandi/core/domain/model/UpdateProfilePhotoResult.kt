package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class UpdateProfilePhotoResult(
    val message: String,
    val imageUrl: String
)
