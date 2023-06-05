package com.example.foodapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("results")
    val recipes: List<SearchDto>
)