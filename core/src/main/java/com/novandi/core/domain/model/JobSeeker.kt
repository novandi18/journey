package com.novandi.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobSeeker(
    val fullName: String,
    val email: String,
    val password: String,
    val skillOne: Int = 0,
    val skillTwo: Int = 0,
    val disabilityId: Int,
    val address: String,
    val gender: String,
    val age: Int,
    val phoneNumber: String
) : Parcelable