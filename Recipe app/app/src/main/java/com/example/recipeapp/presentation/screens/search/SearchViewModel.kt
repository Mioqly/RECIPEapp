package com.example.foodapp.presentation.screens.search

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.domain.model.Ingredient
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.domain.repository.FoodRepository
import com.example.foodapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: FoodRepository
): ViewModel() {

    var recipes by mutableStateOf(emptyList<Recipe>())

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")
    private var searchJob: Job? = null

    //search states
    var searchQuery by mutableStateOf("")
    var diet by mutableStateOf("")
    var cuisine by mutableStateOf("")
    var type by mutableStateOf("")
    var ingredients = mutableStateListOf<String>()

    //filter temp states
    var cuisineTemp by mutableStateOf("")
    var dietTemp by mutableStateOf("")
    var typeTemp by mutableStateOf("")
    var ingredientsTemp = mutableStateListOf<String>()
    var ingredientQuery by mutableStateOf("")
    var ingredientsSuggestions by mutableStateOf(emptyList<Ingredient>())


    fun searchRecipes(){
        viewModelScope.launch {
            repository
                .searchRecipes(
                    query = searchQuery,
                    cuisine = if(cuisine.isNotEmpty()) cuisine.dropLast(1) else null,
                    diet = if(diet.isNotEmpty()) diet.dropLast(1) else null,
                    ingredients = if(ingredients.isNotEmpty()) ingredientsToText().dropLast(1) else null,
                    type = if(type.isNotEmpty()) type.dropLast(1) else null,
                    minCalories = null,
                    maxCalories = null
                )
                .collect { result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let{ recipesRes ->
                                recipes = recipesRes
                            }
                            isLoading = false
                        }
                        is Resource.Error -> {
                            error = result.message!!
                            isLoading = false
                        }
                        is Resource.Loading ->{
                            isLoading = result.isLoading
                        }
                    }
                }
        }
    }

    fun searchIngredients(query: String){
        ingredientQuery = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(1000)
            getIngredients()
        }
    }

    fun getIngredients(){
        viewModelScope.launch {
            repository
                .searchIngredients(
                    query = ingredientQuery,
                )
                .collect { result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let{ ingredients ->
                                ingredientsSuggestions = ingredients
                            }
                            isLoading = false
                        }
                        is Resource.Error -> {
                            error = result.message!!
                            isLoading = false
                        }
                        is Resource.Loading ->{
                            isLoading = result.isLoading
                        }
                    }
                }
        }
    }

    fun addIngredient(ingredient: String){
        ingredientsTemp.add(ingredient)
    }

    fun removeIngredient(ingredient: String){
        ingredientsTemp.remove(ingredient)
    }

    fun setTempFilters(){
        cuisineTemp = cuisine
        dietTemp = diet
        typeTemp = type
        ingredientsTemp = ingredients.toMutableStateList()
    }

    fun applyFilters(){
        cuisine = cuisineTemp
        diet = dietTemp
        type = typeTemp
        ingredients = ingredientsTemp.toMutableStateList()
    }

    fun clearFilters(){
        cuisine = ""
        diet = ""
        type = ""
        ingredients.clear()
    }

    //set filters to search
    fun changeQuery(query: String){
        searchQuery = query
    }

    fun changeCuisine(value: String){
        cuisine = value
    }

    fun changeDiet(value: String){
        diet = value
    }

    fun changeType(value: String){
        type = value
    }


    //update temps
    fun updateCuisineTemp(value: String){
        if(value.isEmpty())
            cuisineTemp = value
        else if(cuisineTemp.contains(value)){
            cuisineTemp = cuisineTemp.replace("$value,","")
        }
        else{
            cuisineTemp += "$value,"
        }
    }

    fun updateDietTemp(value: String){
        if(value.isEmpty())
            dietTemp = value
        else if(dietTemp.contains(value)){
            dietTemp = dietTemp.replace("$value,","")
        }
        else{
            dietTemp += "$value,"
        }
    }

    fun updateTypeTemp(value: String){
        if(value.isEmpty())
            typeTemp = value
        else if(typeTemp.contains(value)){
            typeTemp = typeTemp.replace("$value,","")
        }
        else{
            typeTemp += "$value,"
        }
    }

    private fun ingredientsToText(): String{
        var ingredientsText = ""
        ingredients.forEach {
            ingredientsText += "$it,"
        }
        ingredientsText
        return ingredientsText
    }

}