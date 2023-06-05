package com.example.foodapp.presentation.screens.favourite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodapp.presentation.components.BottomNavigationBar
import com.example.foodapp.presentation.components.RecipeItem
import com.example.foodapp.presentation.screens.destinations.RecipeDetailsScreenDestination
import com.example.foodapp.presentation.screens.home.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun FavouritesScreen(
    navigator: DestinationsNavigator,
    viewModel: FavouriteViewModel = hiltViewModel()
){
    val recipes = viewModel.favourites

    Scaffold(
        bottomBar = {
            BottomNavigationBar("favourite", navigator)
        }
    ) { paddingValues ->
        if(viewModel.error == ""){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if(recipes.isNotEmpty()){
                    LazyVerticalGrid(columns = GridCells.Fixed(2)){
                        items(items = recipes){ recipe ->
                            RecipeItem(recipe = recipe, fav = true){ id ->
                                navigator.navigate(
                                    RecipeDetailsScreenDestination(id)
                                )
                            }
                        }
                    }
                }else{
                    Text(text = "You have not added favourite recipes.")
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if(viewModel.isLoading){
                CircularProgressIndicator()
            } else if (viewModel.error != ""){
                Text(
                    text = viewModel.error,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }


}