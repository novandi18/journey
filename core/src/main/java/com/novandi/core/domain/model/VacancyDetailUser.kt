package com.novandi.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class VacancyDetailUser(
    val vacancyId: String,
    val position: String,
    val description: String,
    val deadlineTime: String,
    val jobType: String,
    val disabilityName: String,
    val skillOne: String,
    val skillTwo: String,
    val companyId: String,
    val companyName: String,
    val companyLogo: String,
    val companySector: String,
    val userCv: String? = null,
    val statusApply: String? = null,
    val notesApply: String? = null,
    val dateTimeApply: String? = null
)
