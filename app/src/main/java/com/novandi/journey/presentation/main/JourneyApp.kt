package com.novandi.journey.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.novandi.journey.presentation.common.NavigationItemBar
import com.novandi.journey.presentation.navigation.JobSeekerNavigation
import com.novandi.journey.presentation.navigation.authGraph
import com.novandi.journey.presentation.navigation.jobProviderGraph
import com.novandi.journey.presentation.navigation.jobSeekerGraph
import com.novandi.journey.presentation.navigation.vacancyGraph
import com.novandi.journey.presentation.navigation.welcomeGraph
import com.novandi.journey.presentation.ui.component.bar.BottomBar

@Composable
fun JourneyApp(
    navHostController: NavHostController = rememberNavController(),
    startedDestination: String
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navHostController,
                navigationItems = if (startedDestination == JobSeekerNavigation.JOB_SEEKER_ROUTE)
                    NavigationItemBar.jobSeeker else NavigationItemBar.jobProvider
            )
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navHostController,
            startDestination = startedDestination
        ) {
            welcomeGraph(navHostController)
            authGraph(navHostController)
            jobSeekerGraph(navHostController)
            jobProviderGraph(navHostController)
            vacancyGraph(navHostController)
        }
    }
}