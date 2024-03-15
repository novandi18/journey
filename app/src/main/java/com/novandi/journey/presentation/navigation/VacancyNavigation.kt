package com.novandi.journey.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.novandi.journey.presentation.common.Screen
import com.novandi.journey.presentation.screen.VacancyScreen

fun NavGraphBuilder.vacancyGraph(navController: NavController) {
    navigation(startDestination = Screen.Vacancy.route, route = VacancyNavigation.VACANCY_ROUTE) {
        composable(
            route = Screen.Vacancy.route,
            arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
        ) {
            val vacancyId = it.arguments?.getString("vacancyId") ?: ""
            VacancyScreen(
                vacancyId = vacancyId,
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToEditVacancy = { vacancy ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "edit_vacancy",
                        value = vacancy
                    )
                    navController.navigate(Screen.JobProviderVacancy.route)
                }
            )
        }
    }
}

object VacancyNavigation {
    const val VACANCY_ROUTE = "vacancy_route"
}