package com.example.foodapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class SearchDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("nutrition")
    val nutrition: NutritionSearch,
    @SerializedName("title")
    val title: String
)