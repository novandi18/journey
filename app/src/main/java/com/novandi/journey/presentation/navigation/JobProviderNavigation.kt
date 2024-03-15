package com.novandi.journey.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.core.domain.model.Vacancy
import com.novandi.journey.presentation.common.Screen
import com.novandi.journey.presentation.screen.JobProviderApplicantDetailScreen
import com.novandi.journey.presentation.screen.JobProviderApplicantScreen
import com.novandi.journey.presentation.screen.JobProviderEditScreen
import com.novandi.journey.presentation.screen.JobProviderEmailScreen
import com.novandi.journey.presentation.screen.JobProviderHomeScreen
import com.novandi.journey.presentation.screen.JobProviderPasswordScreen
import com.novandi.journey.presentation.screen.JobProviderProfileScreen
import com.novandi.journey.presentation.screen.JobProviderVacancyScreen

fun NavGraphBuilder.jobProviderGraph(navController: NavController) {
    navigation(
        startDestination = Screen.JobProviderHome.route,
        route = JobProviderNavigation.JOB_PROVIDER_ROUTE
    ) {
        composable(Screen.JobProviderHome.route) {
            JobProviderHomeScreen(
                navigateToVacancy = { id ->
                    navController.navigate(
                        Screen.Vacancy.createRoute(vacancyId = id)
                    )
                },
                navigateToAdd = {
                    navController.navigate(Screen.JobProviderVacancy.route)
                }
            )
        }
        composable(Screen.JobProviderApplicant.route) {
            JobProviderApplicantScreen(
                navigateToApplicant = { vacancyId ->
                    navController.navigate(
                        Screen.JobProviderApplicantDetail.createRoute(vacancyId)
                    )
                }
            )
        }
        composable(Screen.JobProviderProfile.route) {
            JobProviderProfileScreen(
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
                    navController.navigate(Screen.JobProviderEdit.route)
                },
                navigateToEmail = { email ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "email",
                        value = email
                    )
                    navController.navigate(Screen.JobProviderEmail.route)
                },
                navigateToPassword = {
                    navController.navigate(Screen.JobProviderPassword.route)
                }
            )
        }
        composable(
            route = Screen.JobProviderApplicantDetail.route,
            arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
        ) {
            val vacancyId = it.arguments?.getString("vacancyId") ?: ""
            JobProviderApplicantDetailScreen(
                vacancyId = vacancyId,
                back = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screen.JobProviderVacancy.route) {
            val vacancy = navController.previousBackStackEntry?.savedStateHandle?.get<Vacancy>("edit_vacancy")
            JobProviderVacancyScreen(
                vacancy = vacancy,
                back = { isAdded ->
                    if (isAdded) {
                        navController.navigate(Screen.JobProviderHome.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                    } else navController.navigateUp()
                }
            )
        }
        composable(Screen.JobProviderEdit.route) {
            val profile = navController.previousBackStackEntry
                ?.savedStateHandle?.get<ProfileJobProvider>("edit_profile")
            JobProviderEditScreen(
                profile = profile,
                back = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screen.JobProviderEmail.route) {
            val email = navController.previousBackStackEntry
                ?.savedStateHandle?.get<String>("email")
            JobProviderEmailScreen(
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
        composable(Screen.JobProviderPassword.route) {
            JobProviderPasswordScreen(
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
    }
}

object JobProviderNavigation {
    const val JOB_PROVIDER_ROUTE = "job_provider_route"
}