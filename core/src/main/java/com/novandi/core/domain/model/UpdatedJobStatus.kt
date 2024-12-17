package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class UpdatedJobStatus(
    val vacancyId: String,
    val position: String,
    val company: String,
    val status: String
)
