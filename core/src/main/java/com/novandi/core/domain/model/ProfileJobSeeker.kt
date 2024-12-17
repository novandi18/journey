package com.novandi.core.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
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
    val profilePhotoUrl: String,
    var cv: String? = null
): Parcelable