package com.example.foodapp.data.repository

import com.example.foodapp.data.local.FavouriteRecipe
import com.example.foodapp.data.local.FoodDatabase
import com.example.foodapp.data.mapper.toIngredient
import com.example.foodapp.data.mapper.toNutrition
import com.example.foodapp.data.mapper.toRecipe
import com.example.foodapp.data.mapper.toRecipeEntity
import com.example.foodapp.data.remote.FoodApi
import com.example.foodapp.domain.model.Ingredient
import com.example.foodapp.domain.model.Nutrition
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.domain.repository.FoodRepository
import com.example.foodapp.util.Resource
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val api: FoodApi,
    private val db: FoodDatabase
): FoodRepository {

    private val recipeDao = db.recipeDao
    private val favouriteDao = db.favouriteDao

    override suspend fun getRecipes(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Recipe>>> {
        return flow{
            emit(Resource.Loading(isLoading = true))
            val localRecipes = recipeDao.getAllRecipes()
            emit(Resource.Success(
                data = localRecipes.map { it.toRecipe() }
            ))

            val isDbEmpty = localRecipes.isEmpty()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache){
                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            val remoteResult = try{
               api.getRandomRecipes()
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load data"))
                null
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load data"))
                null
            }

            remoteResult?.let{ result ->
                recipeDao.clearRecipes()
                recipeDao.insertRecipes(result.recipes.map {
                    it.toRecipeEntity()
                })
                emit(Resource.Success(data = recipeDao
                    .getAllRecipes()
                    .map { it.toRecipe() }
                ))
                emit(Resource.Loading(isLoading = false))
            }
        }
    }

    override suspend fun getRecipeDetails(id: String): Resource<Recipe> {
        return try {
            val localRecipe = recipeDao.getRecipe(id.toInt())
            if(localRecipe != null){
                Resource.Success(localRecipe.toRecipe())
            }else{
                val result = api.getRecipeDetails(id)
                Resource.Success(result.toRecipe())
            }
        }catch (e: IOException){
            e.printStackTrace()
            Resource.Error(message = "Couldn't load details. ${e.message}")
        }catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(message = "Couldn't load details. ${e.message}")
        }
    }

    override suspend fun getNutritionsById(id: String): Resource<Nutrition> {
        return try {
            val result = api.getNutritions(id)
            Resource.Success(result.toNutrition())
        }catch (e: IOException){
            e.printStackTrace()
            Resource.Error(message = "Couldn't load nutritions. ${e.message}")
        }catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(message = "Couldn't load nutritions. ${e.message}")
        }
    }

    //favourites
    override suspend fun addFavourite(favouriteRecipe: FavouriteRecipe) = favouriteDao.insert(favouriteRecipe)
    override suspend fun deleteFavourite(favouriteRecipe: FavouriteRecipe) = favouriteDao.delete(favouriteRecipe)
    override suspend fun getFavourites(): Flow<List<Recipe>>{
        return flow{
            favouriteDao.getFavourites().collect{ fav ->
                emit(fav.map { it.toRecipe() })
            }
        }
    }

    override suspend fun searchRecipes(
        query: String?,
        cuisine: String?,
        diet: String?,
        ingredients: String?,
        type: String?,
        minCalories: String?,
        maxCalories: String?
    ): Flow<Resource<List<Recipe>>> {
        return flow{
            emit(Resource.Loading(isLoading = true))
            val remoteResult = try{
                api.searchRecipes(
                    query = query,
                    cuisine = cuisine,
                    diet = diet,
                    ingredients = ingredients,
                    type = type,
                    minCalories = minCalories,
                    maxCalories = maxCalories
                )
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't search data"))
                null
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't search data"))
                null
            }

            remoteResult?.let{ result ->
                emit(Resource.Success(data = result.recipes
                    .map { it.toRecipe() }
                ))
                emit(Resource.Loading(isLoading = false))
            }
        }
    }

    override suspend fun searchIngredients(query: String?): Flow<Resource<List<Ingredient>>> {
        return flow{
            emit(Resource.Loading(isLoading = true))
            val remoteResult = try{
                api.searchAutoCompleteIngredients(
                    query = query
                )
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't search ingredients"))
                null
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't search ingredients"))
                null
            }

            remoteResult?.let{ result ->
                emit(Resource.Success(data = result
                    .map { it.toIngredient() }
                ))
                emit(Resource.Loading(isLoading = false))
            }
        }
    }

    override suspend fun searchDailyMeal(
        minFat: Int?,
        maxFat: Int?,
        minCarb: Int?,
        maxCarbs: Int?,
        minProtein: Int?,
        maxProtein: Int?,
        minCalories: Int,
        maxCalories: Int
    ): Resource<Recipe> {
        return try {
            val dailyMealResult = api.searchDailyMeal(
                minCalories = minCalories,
                maxCalories = maxCalories
            )
            if(dailyMealResult.isNotEmpty()){
                Resource.Success(dailyMealResult[0].toRecipe())
            }else{
                Resource.Success(Recipe())
            }
        }catch (e: IOException){
            e.printStackTrace()
            Resource.Error(message = "Couldn't load details. ${e.message}")
        }catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(message = "Couldn't load details. ${e.message}")
        }
    }
}