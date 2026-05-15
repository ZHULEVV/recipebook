package com.recipebook.android.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.recipebook.android.presentation.comments.CommentsScreen
import com.recipebook.android.presentation.favorites.FavoritesScreen
import com.recipebook.android.presentation.home.HomeScreen
import com.recipebook.android.presentation.mealplan.MealPlanScreen
import com.recipebook.android.presentation.profile.ProfileScreen
import com.recipebook.android.presentation.recipedetails.RecipeDetailsScreen
import com.recipebook.android.presentation.search.SearchScreen
import com.recipebook.android.presentation.shoppinglist.ShoppingListScreen

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
                HomeScreen(
                    onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.route(id)) }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.route(id)) }
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.route(id)) }
                )
            }
            composable(Screen.MealPlan.route) {
                MealPlanScreen(
                    onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.route(id)) }
                )
            }
            composable(Screen.ShoppingList.route) {
                ShoppingListScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = Screen.RecipeDetails.route,
                arguments = listOf(navArgument(Screen.RecipeDetails.ARG_RECIPE_ID) { type = NavType.StringType })
            ) {
                RecipeDetailsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCommentsClick = { id -> navController.navigate(Screen.Comments.route(id)) }
                )
            }
            composable(
                route = Screen.Comments.route,
                arguments = listOf(navArgument(Screen.Comments.ARG_RECIPE_ID) { type = NavType.StringType })
            ) {
                CommentsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
