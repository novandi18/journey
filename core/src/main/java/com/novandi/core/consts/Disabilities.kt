package com.novandi.core.consts

import com.novandi.core.domain.model.Disability

object Disabilities {
    fun getDisabilities() = listOf(
        Disability(id = 1, disability = "Gangguan Penglihatan"),
        Disability(id = 2, disability = "Gangguan Pendengaran"),
        Disability(id = 3, disability = "Disabilitas Fisik"),
        Disability(id = 4, disability = "Disabilitas Kognitif"),
        Disability(id = 5, disability = "Keberagaman Neurologis"),
        Disability(id = 6, disability = "Gangguan Berbicara"),
        Disability(id = 7, disability = "Gangguan Mobilitas"),
        Disability(id = 8, disability = "Semua Jenis Disabilitas"),
        Disability(id = 9, disability = "Gangguan Belajar")
    )
}