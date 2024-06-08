package com.novandi.core.domain.model

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
