package com.example.foodapp.domain.repository

import com.example.foodapp.data.local.FavouriteRecipe
import com.example.foodapp.domain.model.Ingredient
import com.example.foodapp.domain.model.Nutrition
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface FoodRepository {

    suspend fun getRecipes(fetchFromRemote: Boolean): Flow<Resource<List<Recipe>>>
    suspend fun getRecipeDetails(id: String): Resource<Recipe>
    suspend fun getNutritionsById(id: String): Resource<Nutrition>

    //favourites
    suspend fun addFavourite(favouriteRecipe: FavouriteRecipe)
    suspend fun deleteFavourite(favouriteRecipe: FavouriteRecipe)
    suspend fun getFavourites(): Flow<List<Recipe>>

    //search
    suspend fun searchRecipes(
        query: String?,
        cuisine: String?,
        diet: String?,
        ingredients: String?,
        type: String?,
        minCalories: String?,
        maxCalories: String?,
    ): Flow<Resource<List<Recipe>>>

    suspend fun searchIngredients(query: String?): Flow<Resource<List<Ingredient>>>

    //daily meal
    suspend fun searchDailyMeal(
        minFat: Int? = null,
        maxFat: Int? = null,
        minCarb: Int? = null,
        maxCarbs: Int? = null,
        minProtein: Int? = null,
        maxProtein: Int? = null,
        minCalories: Int,
        maxCalories: Int,
    ): Resource<Recipe>


}