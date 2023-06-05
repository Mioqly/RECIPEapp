package com.example.foodapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class IngredientDto(
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
)