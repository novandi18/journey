package com.novandi.core.domain.model

data class JobProvider(
    val name: String,
    val address: String,
    val city: String,
    val province: String,
    val employees: Int,
    val email: String,
    val password: String,
    val sectorId: Int
)