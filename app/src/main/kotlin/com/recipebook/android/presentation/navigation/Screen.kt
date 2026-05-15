package com.recipebook.android.presentation.navigation

sealed class Screen(val route: String) {
    object Login         : Screen("login")
    object Register      : Screen("register")
    object ForgotPassword: Screen("forgot_password")
    object Home          : Screen("home")
    object Search        : Screen("search")
    object Favorites     : Screen("favorites")
    object MealPlan      : Screen("meal_plan")
    object ShoppingList  : Screen("shopping_list")
    object Profile       : Screen("profile")

    object RecipeDetails : Screen("recipe_details/{recipeId}") {
        const val ARG_RECIPE_ID = "recipeId"
        fun route(recipeId: String) = "recipe_details/$recipeId"
    }

    object Comments : Screen("comments/{recipeId}") {
        const val ARG_RECIPE_ID = "recipeId"
        fun route(recipeId: String) = "comments/$recipeId"
    }
}
