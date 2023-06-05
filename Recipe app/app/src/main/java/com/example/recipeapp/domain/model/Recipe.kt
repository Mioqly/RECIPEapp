package com.example.foodapp.domain.model

import com.example.foodapp.data.remote.features.AnalyzedInstruction
import com.example.foodapp.data.remote.features.ExtendedIngredient

data class Recipe(
    val id: Int? = null,
    val aggregateLikes: Int? = null,
    val analyzedInstructions: List<AnalyzedInstruction>? = null,
    val cookingMinutes: Int? = null,
    val dishTypes: String? = null,
    val extendedIngredients: List<ExtendedIngredient>? = null,
    val glutenFree: Boolean? = null,
    val image: String? = null,
    val instructions: String? = null,
    val preparationMinutes: Int? = null,
    val readyInMinutes: Int? = null,
    val summary: String? = null,
    val title: String? = null,
    val vegan: Boolean? = null,
    val vegetarian: Boolean? = null,
    val veryHealthy: Boolean? = null,
    val veryPopular: Boolean? = null,
    val servings: Int? = null,
    val nutrition: Nutrition? = null
)
