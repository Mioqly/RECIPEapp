package com.example.foodapp.data.remote

import com.example.foodapp.data.remote.dto.*
import com.example.foodapp.data.remote.features.RecipeResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {

    @GET("/recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 20,
        @Query("apiKey") apiKey: String = API_KEY
    ): RecipeResult

    @GET("/recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") id: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): RecipeDto

    @GET("/recipes/{id}/nutritionWidget.json")
    suspend fun getNutritions(
        @Path("id") id: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): NutritionDto

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String?,
        @Query("cuisine") cuisine: String?,
        @Query("diet") diet: String?,
        @Query("includeIngredients") ingredients: String?,
        @Query("type") type: String?,
        @Query("minCalories") minCalories: String?,
        @Query("maxCalories") maxCalories: String?,
        @Query("number") number: Int = 20,
        @Query("apiKey") apiKey: String = API_KEY
    ): SearchResult

    @GET("/food/ingredients/autocomplete")
    suspend fun searchAutoCompleteIngredients(
        @Query("query") query: String?,
        @Query("number") number: Int = 4,
        @Query("apiKey") apiKey: String = API_KEY
    ): IngredientsResult

    @GET("/recipes/findByNutrients")
    suspend fun searchDailyMeal(
        @Query("number") number: Int = 1,
        @Query("minFat") minFat: Int? = null,
        @Query("maxFat") maxFat: Int? = null,
        @Query("minCarbs") minCarbs: Int? = null,
        @Query("maxCarbs") maxCarbs: Int? = null,
        @Query("minProtein") minProteins: Int? = null,
        @Query("maxProtein") maxProteins: Int? = null,
        @Query("minCalories") minCalories: Int,
        @Query("maxCalories") maxCalories: Int,
        @Query("random") random: Boolean = true,
        @Query("apiKey") apiKey: String = API_KEY
    ): ArrayList<DailyMealDto>

    companion object{
        const val API_KEY = "21f5fd9b12dd47a99fa8da7956ffa7d0"
        const val BASE_URL = "https://api.spoonacular.com"
    }
}