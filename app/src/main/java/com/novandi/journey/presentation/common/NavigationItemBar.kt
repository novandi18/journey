package com.novandi.journey.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material.icons.rounded.WorkHistory
import com.novandi.journey.R

object NavigationItemBar {
    val jobSeeker = listOf(
        NavigationItem(
            title = R.string.vacancy,
            icon = Icons.Rounded.Home,
            screen = Screen.JobSeekerHome,
            contentDescription = R.string.vacancy
        ),
        NavigationItem(
            title = R.string.assistant,
            icon = Icons.Rounded.AutoAwesome,
            screen = Screen.Assistant,
            contentDescription = R.string.assistant
        ),
        NavigationItem(
            title = R.string.job_apply,
            icon = Icons.Rounded.Work,
            screen = Screen.JobSeekerApply,
            contentDescription = R.string.job_apply
        ),
        NavigationItem(
            title = R.string.profile,
            icon = Icons.Rounded.Person,
            screen = Screen.JobSeekerProfile,
            contentDescription = R.string.profile
        )
    )

    val jobProvider = listOf(
        NavigationItem(
            title = R.string.job_vacancy,
            icon = Icons.Rounded.Home,
            screen = Screen.JobProviderHome,
            contentDescription = R.string.job_vacancy
        ),
        NavigationItem(
            title = R.string.job_vacancy_user,
            icon = Icons.Rounded.WorkHistory,
            screen = Screen.JobProviderApplicant,
            contentDescription = R.string.job_vacancy_user
        ),
        NavigationItem(
            title = R.string.profile,
            icon = Icons.Rounded.Person,
            screen = Screen.JobProviderProfile,
            contentDescription = R.string.profile
        )
    )
}