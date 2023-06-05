package com.example.foodapp.data.local

import androidx.room.Entity
import com.example.foodapp.data.remote.features.Measures
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ingredients")
data class IngredientEntity(
    val aisle: String,
    val amount: Double,
    val consistency: String,
    val id: Int,
    val image: String,
    val measures: Measures,
    val meta: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("nameClean")
    val nameClean: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("originalName")
    val originalName: String,
    @SerializedName("unit")
    val unit: String
)
