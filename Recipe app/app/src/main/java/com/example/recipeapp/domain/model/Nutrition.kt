package com.example.foodapp.domain.model

import com.google.gson.annotations.SerializedName

data class Nutrition(
    val calories: String = "",
    val carbs: String = "",
    val fat: String = "",
    val protein: String = ""
)
