package com.example.foodapp.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.domain.repository.FoodRepository
import com.example.foodapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FoodRepository
): ViewModel() {

    var isLoading by mutableStateOf(false)
    var isRefreshing by mutableStateOf(false)
    var error by mutableStateOf("")
    var recipes by mutableStateOf(emptyList<Recipe>())

    private val _favouriteList = MutableStateFlow<List<Recipe>>(emptyList())
    val favouriteList = _favouriteList.asStateFlow()

    init {
        getRecipes(fetchFromRemote = false)
        getFavourites()
    }
    fun refreshRecipes(){
        getRecipes(fetchFromRemote = true)
    }

    private fun getRecipes(fetchFromRemote: Boolean){
        viewModelScope.launch {
            repository
                .getRecipes(fetchFromRemote = fetchFromRemote)
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

    private fun getFavourites(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavourites()
                .distinctUntilChanged()
                .collect { favList ->
                    _favouriteList.value = favList
                }
        }
    }
}