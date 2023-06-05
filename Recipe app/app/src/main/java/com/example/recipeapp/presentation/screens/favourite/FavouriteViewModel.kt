package com.example.foodapp.presentation.screens.favourite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.mapper.toFavouriteRecipe
import com.example.foodapp.data.mapper.toRecipe
import com.example.foodapp.domain.model.Nutrition
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.domain.repository.FoodRepository
import com.example.foodapp.util.Resource
import com.github.mikephil.charting.utils.Utils.init
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var favourites by mutableStateOf(emptyList<Recipe>())
    var error by mutableStateOf("")

    init {
        getFavourites()
    }

    private fun getFavourites(){
        viewModelScope.launch {
            repository.getFavourites()
                .distinctUntilChanged()
                .collect { favList ->
                    favourites = favList
                }
        }
    }
}