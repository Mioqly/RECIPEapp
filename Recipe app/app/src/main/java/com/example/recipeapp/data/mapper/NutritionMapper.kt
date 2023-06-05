package com.example.foodapp.data.mapper

import com.example.foodapp.data.remote.dto.NutritionDto
import com.example.foodapp.domain.model.Nutrition

fun NutritionDto.toNutrition() : Nutrition{
    return Nutrition(
        calories = calories,
        carbs = carbs,
        fat = fat,
        protein = protein
    )
}