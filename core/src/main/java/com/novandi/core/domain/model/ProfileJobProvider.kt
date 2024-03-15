package com.novandi.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileJobProvider(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val city: String = "",
    val province: String = "",
    val logo: String = "",
    val employees: Int = 0,
    val email: String = "",
    val roleId: Int = 0,
    val sectorName: String = ""
): Parcelable