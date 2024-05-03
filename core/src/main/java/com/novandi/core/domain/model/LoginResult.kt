package com.novandi.core.domain.model

data class LoginResult(
    val status: String,
    val id: String,
    val roleId: Int,
    val token: String,
    val disability: String? = null,
    val skillOne: String? = null,
    val skillTwo: String? = null
)
