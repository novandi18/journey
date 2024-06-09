package com.novandi.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendation_vacancies")
data class RecommendationVacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vacancyId: String,
    val placementAddress: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val deadlineTime: String,
    val jobType: Int = 0,
    val skillOne: String,
    val skillTwo: String,
    val disabilityName: String,
    val companyLogo: String,
    val sectorName: String,
    val companyName: String,
    val totalApplicants: Int? = null,
    val companyId: String? = null
)
