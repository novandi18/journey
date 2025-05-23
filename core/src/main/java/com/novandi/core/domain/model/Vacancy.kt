package com.novandi.core.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class Vacancy(
    val id: String = "",
    val placementAddress: String = "",
    val description: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val deadlineTime: String = "",
    val jobType: Int = 0,
    val skillOne: String = "",
    val skillTwo: String = "",
    val disabilityName: String = "",
    val companyLogo: String = "",
    val sectorName: String = "",
    val companyName: String = "",
    val totalApplicants: Int? = null,
    val companyId: String = ""
): Parcelable
