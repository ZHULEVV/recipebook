package com.recipebook.android.data.remote.mapper

import com.recipebook.android.data.remote.dto.CommentDto
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import com.recipebook.android.data.remote.dto.IngredientDto
import com.recipebook.android.data.remote.dto.MealPlanEntryDto
import com.recipebook.android.data.remote.dto.RatingDto
import com.recipebook.android.data.remote.dto.RecipeDto
import com.recipebook.android.data.remote.dto.RecipeIngredientDto
import com.recipebook.android.data.remote.dto.StepDto
import com.recipebook.android.data.remote.dto.TagDto
import com.recipebook.android.data.remote.dto.UserDto
import com.recipebook.android.domain.model.Comment
import com.recipebook.android.domain.model.Ingredient
import com.recipebook.android.domain.model.MealPlanEntry
import com.recipebook.android.domain.model.MealType
import com.recipebook.android.domain.model.Rating
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.model.RecipeIngredient
import com.recipebook.android.domain.model.Step
import com.recipebook.android.domain.model.Tag
import com.recipebook.android.domain.model.User

fun TagDto.toDomain() = Tag(id = id, name = name)

fun IngredientDto.toDomain() = Ingredient(id = id, name = name)

fun StepDto.toDomain() = Step(
    stepNumber = stepNumber,
    content    = content,
    imageUrl   = imageUrl
)

fun RecipeIngredientDto.toDomain() = RecipeIngredient(
    ingredient = Ingredient(id = ingredientId, name = name),
    amount     = amount,
    unit       = unit
)

fun RecipeDto.toDomain() = Recipe(
    id             = id,
    title          = title,
    description    = description,
    imageUrl       = imageUrl,
    cookingTimeMin = cookingTimeMin,
    baseServings   = baseServings,
    caloriesPer100g = caloriesPer100g,
    proteinPer100g  = proteinPer100g,
    fatPer100g      = fatPer100g,
    carbsPer100g    = carbsPer100g,
    averageRating   = averageRating,
    isFavorite      = isFavorite,
    tags            = tags.map { it.toDomain() },
    ingredients     = ingredients.map { it.toDomain() },
    steps           = steps.map { it.toDomain() },
    authorId        = authorId
)

fun UserDto.toDomain() = User(
    id          = id,
    email       = email,
    displayName = displayName,
    avatarUrl   = avatarUrl
)

private fun parseIso8601(dateStr: String): Long {
    val normalized = dateStr.replace(Regex("(\\.\\d{3})\\d+"), "$1")
    val formats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'"
    )
    for (fmt in formats) {
        runCatching {
            return SimpleDateFormat(fmt, Locale.US)
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
                .parse(normalized)!!.time
        }
    }
    return 0L
}

fun CommentDto.toDomain() = Comment(
    id        = id,
    recipeId  = recipeId,
    userId    = userId,
    userName  = userName,
    text      = text,
    createdAt = parseIso8601(createdAt)
)

fun RatingDto.toDomain() = Rating(
    recipeId = recipeId,
    userId   = userId,
    value    = value
)

fun MealPlanEntryDto.toDomain() = MealPlanEntry(
    id       = id,
    recipeId = recipeId,
    recipe   = recipe?.toDomain(),
    date     = date,
    mealType = runCatching { MealType.valueOf(mealType) }.getOrDefault(MealType.LUNCH)
)
