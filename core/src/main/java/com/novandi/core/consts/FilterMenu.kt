package com.novandi.core.consts

import com.novandi.utility.data.move

object FilterMenu {
    fun menu(): List<List<String>> {
        val jobTypes = JobTypes.types().toMutableList()
        val disabilities = Disabilities.getDisabilities().map { it.disability }.toMutableList()
        val provinces = Provinces.getProvinces().map { it.province }.toMutableList()
        jobTypes.add(0, "Semua")
        disabilities.move(7, 0)
        disabilities[0] = "Semua"
        provinces.add(0, "Semua")
        return listOf(
            jobTypes,
            disabilities,
            provinces
        )
    }
}