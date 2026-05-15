package com.recipebook.android.data.local.mapper

import com.recipebook.android.data.local.database.entity.RecipeCacheEntity
import com.recipebook.android.data.local.database.entity.SearchHistoryEntity
import com.recipebook.android.data.remote.dto.RecipeIngredientDto
import com.recipebook.android.data.remote.dto.StepDto
import com.recipebook.android.data.remote.dto.TagDto
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.model.SearchHistoryEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun Recipe.toEntity(cachedAt: Long = System.currentTimeMillis()): RecipeCacheEntity =
    RecipeCacheEntity(
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
        tagsJson        = json.encodeToString(tags.map { TagDto(it.id, it.name) }),
        ingredientsJson = json.encodeToString(
            ingredients.map { ri ->
                RecipeIngredientDto(
                    ingredient = TagDto(ri.ingredient.id, ri.ingredient.name).let {
                        com.recipebook.android.data.remote.dto.IngredientDto(ri.ingredient.id, ri.ingredient.name)
                    },
                    amount = ri.amount,
                    unit   = ri.unit
                )
            }
        ),
        stepsJson  = json.encodeToString(steps.map { StepDto(it.stepNumber, it.content, it.imageUrl) }),
        authorId   = authorId,
        cachedAt   = cachedAt
    )

fun RecipeCacheEntity.toDomain(): Recipe =
    Recipe(
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
        tags        = json.decodeFromString<List<TagDto>>(tagsJson).map { it.toDomain() },
        ingredients = json.decodeFromString<List<RecipeIngredientDto>>(ingredientsJson).map { it.toDomain() },
        steps       = json.decodeFromString<List<StepDto>>(stepsJson).map { it.toDomain() },
        authorId    = authorId
    )

fun SearchHistoryEntity.toDomain() = SearchHistoryEntry(
    id        = id,
    query     = query,
    timestamp = timestamp
)
