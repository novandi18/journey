package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class ApplicantItemStatus(
    val applicantId: String,
    val isAccepted: Boolean
)
