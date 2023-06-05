package com.example.foodapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class NutritionDto(
    @SerializedName("calories")
    val calories: String,
    @SerializedName("carbs")
    val carbs: String,
    @SerializedName("fat")
    val fat: String,
    @SerializedName("protein")
    val protein: String
)