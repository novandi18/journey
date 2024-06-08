package com.novandi.journey.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.novandi.journey.presentation.common.Screen
import com.novandi.journey.presentation.screen.VacancyCompanyDetailScreen
import com.novandi.journey.presentation.screen.VacancyDetailCompanyScreen
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
                navigateToCompanyDetail = { companyId ->
                    navController.navigate(
                        Screen.VacancyCompanyDetail.createRoute(companyId)
                    )
                }
            )
        }
        composable(
            route = Screen.VacancyDetailCompany.route,
            arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
        ) {
            val vacancyId = it.arguments?.getString("vacancyId") ?: ""
            VacancyDetailCompanyScreen(
                vacancyId = vacancyId,
                back = {
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
        composable(
            route = Screen.VacancyCompanyDetail.route,
            arguments = listOf(navArgument("companyId") { type = NavType.StringType })
        ) {
            val companyId = it.arguments?.getString("companyId") ?: ""
            VacancyCompanyDetailScreen(
                companyId = companyId,
                back = {
                    navController.navigateUp()
                }
            )
        }
    }
}

object VacancyNavigation {
    const val VACANCY_ROUTE = "vacancy_route"
}