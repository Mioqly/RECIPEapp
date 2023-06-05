package com.example.foodapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DailyMealDto(
    @SerializedName("calories")
    val calories: Int,
    @SerializedName("carbs")
    val carbs: String,
    @SerializedName("fat")
    val fat: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("protein")
    val protein: String,
    @SerializedName("title")
    val title: String
)