package com.novandi.core.consts

import com.novandi.core.domain.model.Disability

object Disabilities {
    fun getDisabilities() = listOf(
        Disability(id = 1, disability = "Gangguan Penglihatan", en = "Visual Impairment"),
        Disability(id = 2, disability = "Gangguan Pendengaran", en = "Hearing Impairment"),
        Disability(id = 3, disability = "Disabilitas Fisik", en = "Physical Disability"),
        Disability(id = 4, disability = "Disabilitas Kognitif", en = "Cognitive Disability"),
        Disability(id = 5, disability = "Keberagaman Neurologis", en = "Neurodiversity"),
        Disability(id = 6, disability = "Gangguan Berbicara", en = "Speech Impairment"),
        Disability(id = 7, disability = "Gangguan Mobilitas", en = "Mobility Impairment"),
        Disability(id = 8, disability = "Semua Jenis Disabilitas", en = "All Types of Disabilities"),
        Disability(id = 9, disability = "Gangguan Belajar", en = "Learning Disability")
    )
}