package com.novandi.core.consts

import com.novandi.core.domain.model.Disability

object Disabilities {
    fun getDisabilities() = listOf(
        Disability(id = 1, disability = "Visual Impairment"),
        Disability(id = 2, disability = "Hearing Impairment"),
        Disability(id = 3, disability = "Physical Disability"),
        Disability(id = 4, disability = "Cognitive Disability"),
        Disability(id = 5, disability = "Neurodiversity"),
        Disability(id = 6, disability = "Speech Impairment"),
        Disability(id = 7, disability = "Mobility Impairment"),
        Disability(id = 8, disability = "All Types of Disabilities"),
        Disability(id = 5, disability = "Learning Disability")
    )
}