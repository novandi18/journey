package com.novandi.journey.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.journey.presentation.common.Screen
import com.novandi.journey.presentation.screen.AssistantScreen
import com.novandi.journey.presentation.screen.JobSeekerApplyDetailScreen
import com.novandi.journey.presentation.screen.JobSeekerApplyScreen
import com.novandi.journey.presentation.screen.JobSeekerEditScreen
import com.novandi.journey.presentation.screen.JobSeekerEmailScreen
import com.novandi.journey.presentation.screen.JobSeekerHomeScreen
import com.novandi.journey.presentation.screen.JobSeekerPasswordScreen
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
            JobSeekerApplyScreen(
                navigateToDetail = { vacancyId ->
                    navController.navigate(
                        Screen.JobSeekerApplyDetail.createRoute(vacancyId)
                    )
                }
            )
        }
        composable(Screen.JobSeekerProfile.route) {
            JobSeekerProfileScreen(
                navigateToStarted = {
                    navController.navigate(Screen.Started.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                },
                navigateToEdit = { profile ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "edit_profile",
                        value = profile
                    )
                    navController.navigate(Screen.JobSeekerEdit.route)
                },
                navigateToEmail = { email ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "email",
                        value = email
                    )
                    navController.navigate(Screen.JobSeekerEmail.route)
                },
                navigateToPassword = {
                    navController.navigate(Screen.JobSeekerPassword.route)
                }
            )
        }
        composable(Screen.JobSeekerEdit.route) {
            val profile = navController.previousBackStackEntry
                ?.savedStateHandle?.get<ProfileJobSeeker>("edit_profile")
            JobSeekerEditScreen(
                profile = profile,
                back = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screen.JobSeekerEmail.route) {
            val email = navController.previousBackStackEntry
                ?.savedStateHandle?.get<String>("email")
            JobSeekerEmailScreen(
                email = email,
                back = {
                    navController.navigateUp()
                },
                navigateToStarted = {
                    navController.navigate(Screen.Started.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.JobSeekerPassword.route) {
            JobSeekerPasswordScreen(
                back = {
                    navController.navigateUp()
                },
                navigateToStarted = {
                    navController.navigate(Screen.Started.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.Assistant.route) {
            AssistantScreen()
        }
        composable(
            route = Screen.JobSeekerApplyDetail.route,
            arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
        ) {
            val vacancyId = it.arguments?.getString("vacancyId") ?: ""
            JobSeekerApplyDetailScreen(
                vacancyId = vacancyId,
                back = {
                    navController.navigateUp()
                }
            )
        }
    }
}

object JobSeekerNavigation {
    const val JOB_SEEKER_ROUTE = "job_seeker_route"
}