package com.novandi.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileJobSeeker(
    val id: String,
    val fullName: String,
    val email: String,
    val skillOne: String,
    val skillTwo: String,
    val disabilityName: String,
    val address: String,
    val gender: String,
    val age: String,
    val phoneNumber: String,
    val profilePhotoUrl: String
): Parcelable