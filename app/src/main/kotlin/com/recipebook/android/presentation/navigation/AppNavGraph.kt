package com.recipebook.android.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.recipebook.android.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.recipebook.android.presentation.auth.login.LoginScreen
import com.recipebook.android.presentation.auth.register.RegisterScreen

private val bottomNavRoutes = setOf(
    Screen.Home.route,
    Screen.Search.route,
    Screen.Favorites.route,
    Screen.MealPlan.route,
    Screen.Profile.route
)

@Composable
fun AppNavGraph(startDestination: String = Screen.Login.route) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Home.route) {
                PlaceholderScreen("Home")
            }
            composable(Screen.Search.route) {
                PlaceholderScreen("Search")
            }
            composable(Screen.Favorites.route) {
                PlaceholderScreen("Favorites")
            }
            composable(Screen.MealPlan.route) {
                PlaceholderScreen("MealPlan")
            }
            composable(Screen.ShoppingList.route) {
                PlaceholderScreen("ShoppingList")
            }
            composable(Screen.Profile.route) {
                PlaceholderScreen("Profile")
            }
            composable(
                route = Screen.RecipeDetails.route,
                arguments = listOf(navArgument(Screen.RecipeDetails.ARG_RECIPE_ID) { type = NavType.StringType })
            ) {
                PlaceholderScreen("RecipeDetails")
            }
            composable(
                route = Screen.Comments.route,
                arguments = listOf(navArgument(Screen.Comments.ARG_RECIPE_ID) { type = NavType.StringType })
            ) {
                PlaceholderScreen("Comments")
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name)
    }
}
