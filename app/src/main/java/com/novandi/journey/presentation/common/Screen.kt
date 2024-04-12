package com.novandi.journey.presentation.common

sealed class Screen(val route: String) {
    data object Welcome: Screen("welcome")
    data object Started: Screen("started")
    data object JobSeekerLogin: Screen("job_seeker_login")
    data object JobProviderLogin: Screen("job_provider_login")
    data object JobSeekerRegister: Screen("job_seeker_register")
    data object JobProviderRegister: Screen("job_provider_register")
    data object JobSeekerSkill: Screen("job_seeker_skill")
    data object JobSeekerHome: Screen("job_seeker_home")
    data object JobSeekerApply: Screen("job_seeker_apply")
    data object JobSeekerProfile: Screen("job_seeker_profile")
    data object JobProviderHome: Screen("job_provider_home")
    data object JobProviderApplicant: Screen("job_provider_applicant")
    data object JobProviderProfile: Screen("job_provider_profile")
    data object JobProviderVacancy: Screen("job_provider_vacancy")
    data object JobProviderEdit: Screen("job_provider_edit")
    data object JobProviderEmail: Screen("job_provider_email")
    data object JobProviderPassword: Screen("job_provider_password")
    data object JobSeekerEdit: Screen("job_seeker_edit")
    data object JobSeekerEmail: Screen("job_seeker_email")
    data object JobSeekerPassword: Screen("job_seeker_password")
    data object Assistant: Screen("assistant")
    data object JobProviderApplicantDetail: Screen("job_provider_applicant/{vacancyId}") {
        fun createRoute(vacancyId: String) = "job_provider_applicant/$vacancyId"
    }
    data object Vacancy: Screen("vacancy/{vacancyId}") {
        fun createRoute(vacancyId: String) = "vacancy/$vacancyId"
    }
    data object JobSeekerApplyDetail: Screen("job_seeker_apply/{vacancyId}") {
        fun createRoute(vacancyId: String) = "job_seeker_apply/$vacancyId"
    }
}