package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class VacancyDetailCompany(
    val id: String,
    val position: String,
    val description: String,
    val updatedAt: String,
    val jobType: String,
    val skillOne: String,
    val skillTwo: String,
    val disability: String,
    val deadlineTime: String,
    val companyName: String
)