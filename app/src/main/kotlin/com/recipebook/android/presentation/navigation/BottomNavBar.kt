package com.recipebook.android.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick  = { navController.navigate(Screen.Home.route) { launchSingleTop = true } },
            icon     = {
                Icon(
                    if (currentRoute == Screen.Home.route) Icons.Filled.Home
                    else Icons.Outlined.Home,
                    contentDescription = null
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Search.route,
            onClick  = { navController.navigate(Screen.Search.route) { launchSingleTop = true } },
            icon     = { Icon(Icons.Outlined.Search, contentDescription = null) }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Favorites.route,
            onClick  = { navController.navigate(Screen.Favorites.route) { launchSingleTop = true } },
            icon     = {
                Icon(
                    if (currentRoute == Screen.Favorites.route) Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = null
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.MealPlan.route,
            onClick  = { navController.navigate(Screen.MealPlan.route) { launchSingleTop = true } },
            icon     = { Icon(Icons.Outlined.CalendarMonth, contentDescription = null) }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Profile.route,
            onClick  = { navController.navigate(Screen.Profile.route) { launchSingleTop = true } },
            icon     = {
                Icon(
                    if (currentRoute == Screen.Profile.route) Icons.Filled.Person
                    else Icons.Outlined.Person,
                    contentDescription = null
                )
            }
        )
    }
}
