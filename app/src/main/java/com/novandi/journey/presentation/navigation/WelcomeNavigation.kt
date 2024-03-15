package com.novandi.journey.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.novandi.journey.presentation.common.Screen
import com.novandi.journey.presentation.screen.WelcomeScreen

fun NavGraphBuilder.welcomeGraph(navController: NavController) {
    navigation(startDestination = Screen.Welcome.route, route = WelcomeNavigation.WELCOME_ROUTE) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                navigateToHome = {
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

object WelcomeNavigation {
    const val WELCOME_ROUTE = "welcome_route"
}