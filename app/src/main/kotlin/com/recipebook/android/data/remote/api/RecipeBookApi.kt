package com.recipebook.android.data.remote.api

import com.recipebook.android.data.remote.dto.AddCommentRequestDto
import com.recipebook.android.data.remote.dto.AddToMealPlanRequestDto
import com.recipebook.android.data.remote.dto.CommentDto
import com.recipebook.android.data.remote.dto.CreateRecipeRequestDto
import com.recipebook.android.data.remote.dto.IngredientDto
import com.recipebook.android.data.remote.dto.MealPlanEntryDto
import com.recipebook.android.data.remote.dto.PagedResponseDto
import com.recipebook.android.data.remote.dto.RateRecipeRequestDto
import com.recipebook.android.data.remote.dto.RatingDto
import com.recipebook.android.data.remote.dto.RecipeDto
import com.recipebook.android.data.remote.dto.ShoppingItemDto
import com.recipebook.android.data.remote.dto.TagDto
import com.recipebook.android.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeBookApi {

    @GET("recipes")
    suspend fun getRecipes(
        @Query("tagId") tagIds: List<String> = emptyList()
    ): PagedResponseDto<RecipeDto>

    @GET("recipes/my")
    suspend fun getMyRecipes(): PagedResponseDto<RecipeDto>

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: String): RecipeDto

    @GET("recipes/search")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("tagId") tagIds: List<String> = emptyList()
    ): PagedResponseDto<RecipeDto>

    @POST("recipes")
    suspend fun createRecipe(@Body request: CreateRecipeRequestDto): RecipeDto

    @PUT("recipes/{id}")
    suspend fun updateRecipe(
        @Path("id") id: String,
        @Body request: CreateRecipeRequestDto
    ): RecipeDto

    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(@Path("id") id: String)

    @GET("favorites")
    suspend fun getFavorites(): PagedResponseDto<RecipeDto>

    @POST("favorites/{recipeId}")
    suspend fun addFavorite(@Path("recipeId") recipeId: String)

    @DELETE("favorites/{recipeId}")
    suspend fun removeFavorite(@Path("recipeId") recipeId: String)

    @GET("meal-plan")
    suspend fun getMealPlan(
        @Query("from") from: String? = null,
        @Query("to")   to: String?   = null
    ): List<MealPlanEntryDto>

    @POST("meal-plan")
    suspend fun addToMealPlan(@Body request: AddToMealPlanRequestDto): MealPlanEntryDto

    @DELETE("meal-plan/{id}")
    suspend fun removeFromMealPlan(@Path("id") id: String)

    @GET("shopping-list")
    suspend fun getShoppingList(
        @Query("from") from: String,
        @Query("to")   to: String
    ): List<ShoppingItemDto>

    @GET("recipes/{recipeId}/comments")
    suspend fun getComments(@Path("recipeId") recipeId: String): PagedResponseDto<CommentDto>

    @POST("recipes/{recipeId}/comments")
    suspend fun addComment(
        @Path("recipeId") recipeId: String,
        @Body request: AddCommentRequestDto
    ): CommentDto

    @DELETE("comments/{id}")
    suspend fun deleteComment(@Path("id") id: String)

    @GET("recipes/{recipeId}/rating")
    suspend fun getRating(@Path("recipeId") recipeId: String): RatingDto?

    @POST("recipes/{recipeId}/rating")
    suspend fun rateRecipe(
        @Path("recipeId") recipeId: String,
        @Body request: RateRecipeRequestDto
    )

    @GET("users/me")
    suspend fun getCurrentUser(): UserDto

    @PUT("users/me")
    suspend fun updateUser(@Body user: UserDto): UserDto

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): UserDto

    @GET("tags")
    suspend fun getTags(): List<TagDto>

    @GET("ingredients")
    suspend fun getIngredients(): List<IngredientDto>
}
