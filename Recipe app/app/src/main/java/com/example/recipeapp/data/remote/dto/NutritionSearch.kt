package com.example.foodapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class NutritionSearch(
    @SerializedName("nutrients")
    val nutrients: List<Nutrient>
)