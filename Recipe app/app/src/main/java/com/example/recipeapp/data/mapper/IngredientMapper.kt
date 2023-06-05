package com.example.foodapp.data.mapper

import com.example.foodapp.data.remote.dto.IngredientDto
import com.example.foodapp.domain.model.Ingredient

fun IngredientDto.toIngredient(): Ingredient{
    return Ingredient(
        name = name,
        image = image
    )
}