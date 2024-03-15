package com.novandi.journey.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.novandi.journey.presentation.common.Screen
import com.novandi.journey.presentation.screen.JobSeekerHomeScreen
import com.novandi.journey.presentation.screen.JobSeekerJobApplyScreen
import com.novandi.journey.presentation.screen.JobSeekerProfileScreen

fun NavGraphBuilder.jobSeekerGraph(navController: NavController) {
    navigation(
        startDestination = Screen.JobSeekerHome.route,
        route = JobSeekerNavigation.JOB_SEEKER_ROUTE
    ) {
        composable(Screen.JobSeekerHome.route) {
            JobSeekerHomeScreen(
                navigateToVacancy = { id ->
                    navController.navigate(
                        Screen.Vacancy.createRoute(vacancyId = id)
                    )
                }
            )
        }
        composable(Screen.JobSeekerApply.route) {
            JobSeekerJobApplyScreen()
        }
        composable(Screen.JobSeekerProfile.route) {
            JobSeekerProfileScreen(
                navigateToStarted = {
                    navController.navigate(Screen.Started.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

object JobSeekerNavigation {
    const val JOB_SEEKER_ROUTE = "job_seeker_route"
}