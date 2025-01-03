package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Applicant(
    val id: String,
    val fullName: String,
    val email: String,
    val address: String,
    val profilePhotoUrl: String,
    val gender: String,
    val age: String,
    val phoneNumber: String,
    val appliedAt: String,
    val disabilityName: String,
    val skillOne: String,
    val skillTwo: String,
    val status: String,
    var notes: String? = null
)
