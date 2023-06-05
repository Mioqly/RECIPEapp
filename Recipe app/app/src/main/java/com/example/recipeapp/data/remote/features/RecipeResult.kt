package com.example.foodapp.data.remote.features


import com.example.foodapp.data.remote.dto.RecipeDto
import com.google.gson.annotations.SerializedName

data class RecipeResult(
    @SerializedName("recipes")
    val recipes: List<RecipeDto>
)