package com.novandi.core.domain.model

data class UpdatedJobStatus(
    val vacancyId: String,
    val position: String,
    val company: String,
    val status: String
)
