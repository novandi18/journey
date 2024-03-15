package com.novandi.journey.presentation.ui.component.bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.novandi.journey.presentation.common.BottomBarState
import com.novandi.journey.presentation.common.NavigationItem
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.Light

@Composable
fun BottomBar(
    navController: NavHostController,
    navigationItems: List<NavigationItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val state = BottomBarState.STATE.isShowing(currentRoute?.route.toString())

    AnimatedVisibility(
        visible = state,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar(
            modifier = Modifier.shadow(16.dp),
            containerColor = Light,
            contentColor = Blue40
        ) {
            navigationItems.map { item ->
                NavigationBarItem(
                    selected = currentRoute?.route == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(id = item.contentDescription)
                        )
                    },
                    label = {
                        Text(text = stringResource(id = item.title))
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Blue40,
                        selectedTextColor = Blue40,
                        selectedIconColor = Light,
                        unselectedTextColor = DarkGray40,
                        unselectedIconColor = DarkGray40
                    )
                )
            }
        }
    }
}