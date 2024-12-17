package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class JobApplyStatus(
    val id: String = "",
    val vacancyId: String,
    val status: String,
    val appliedAt: String,
    val companyName: String = "",
    val vacancyPlacementAddress: String = "",
    val disabilityName: String = "",
    val skillOne: String = "",
    val skillTwo: String = "",
    val notes: String? = null
)
