package com.novandi.journey.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.novandi.core.domain.model.JobSeeker
import com.novandi.journey.presentation.common.Screen
import com.novandi.journey.presentation.screen.JobProviderLoginScreen
import com.novandi.journey.presentation.screen.JobProviderRegisterScreen
import com.novandi.journey.presentation.screen.JobSeekerLoginScreen
import com.novandi.journey.presentation.screen.JobSeekerRegisterScreen
import com.novandi.journey.presentation.screen.JobSeekerSkillScreen
import com.novandi.journey.presentation.screen.StartedScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(startDestination = Screen.Started.route, route = AuthNavigation.AUTH_ROUTE) {
        composable(Screen.Started.route) {
            StartedScreen(
                navigateToJobSeeker = {
                    navController.navigate(Screen.JobSeekerLogin.route)
                },
                navigateToJobProvider = {
                    navController.navigate(Screen.JobProviderLogin.route)
                }
            )
        }
        composable(Screen.JobSeekerLogin.route) {
            JobSeekerLoginScreen(
                backToStarted = {
                    navController.navigateUp()
                },
                navigateToRegister = {
                    navController.navigate(Screen.JobSeekerRegister.route)
                },
                navigateToHome = {
                    navController.navigate(Screen.JobSeekerHome.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.JobProviderLogin.route) {
            JobProviderLoginScreen(
                backToStarted = {
                    navController.navigateUp()
                },
                navigateToRegister = {
                    navController.navigate(Screen.JobProviderRegister.route)
                },
                navigateToHome = {
                    navController.navigate(Screen.JobProviderHome.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.JobSeekerRegister.route) {
            JobSeekerRegisterScreen(
                backToLogin = {
                    navController.navigateUp()
                },
                navigateToSkill = { dataFromRegister ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "dataFromRegister",
                        value = dataFromRegister
                    )
                    navController.navigate(Screen.JobSeekerSkill.route)
                }
            )
        }
        composable(Screen.JobProviderRegister.route) {
            JobProviderRegisterScreen(
                backToLogin = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screen.JobSeekerSkill.route) {
            val dataFromRegister = navController.previousBackStackEntry?.savedStateHandle?.get<JobSeeker>("dataFromRegister")
            JobSeekerSkillScreen(
                dataFromRegister = dataFromRegister,
                backToLogin = {
                    navController.navigate(Screen.JobSeekerLogin.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

object AuthNavigation {
    const val AUTH_ROUTE = "auth_route"
}