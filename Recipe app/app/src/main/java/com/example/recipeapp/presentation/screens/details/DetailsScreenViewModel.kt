package com.example.foodapp.presentation.screens.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.mapper.toFavouriteRecipe
import com.example.foodapp.domain.model.Nutrition
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.domain.repository.FoodRepository
import com.example.foodapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var details by mutableStateOf(Recipe())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")
    private val _favouriteList = MutableStateFlow<List<Recipe>>(emptyList())
    val favouriteList = _favouriteList.asStateFlow()

    init {
        getRecipeDetails()
        getFavourites()
    }

    private fun getRecipeDetails(){
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            isLoading = true
            val recipeDetails = async { repository.getRecipeDetails(id) }
            val nutritionsResult = async {repository.getNutritionsById(id) }
            when(val result = recipeDetails.await()){
                is Resource.Success ->{
                    result.data?.let{ recipeDetails ->
                        details = recipeDetails
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

            when(val result = nutritionsResult.await()){
                is Resource.Success ->{
                    result.data?.let{ result ->
                        details = details.copy(nutrition = result)
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

    private fun getFavourites(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavourites()
                .distinctUntilChanged()
                .collect { favourites ->
                    _favouriteList.value = favourites
                }
        }
    }

    fun addFavourite(recipe: Recipe) = viewModelScope.launch { repository.addFavourite(recipe.toFavouriteRecipe())}
    fun deleteFavourite(recipe: Recipe) = viewModelScope.launch { repository.deleteFavourite(recipe.toFavouriteRecipe())}
}