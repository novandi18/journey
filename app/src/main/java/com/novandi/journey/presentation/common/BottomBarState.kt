package com.novandi.journey.presentation.common

enum class BottomBarState(val screens: List<String>) {
    STATE(
        screens = listOf(
            Screen.JobSeekerHome.route,
            Screen.Assistant.route,
            Screen.JobSeekerApply.route,
            Screen.JobSeekerProfile.route,
            Screen.JobProviderHome.route,
            Screen.JobProviderApplicant.route,
            Screen.JobProviderProfile.route
        )
    );

    fun isShowing(screen: String) = STATE.screens.contains(screen)
}