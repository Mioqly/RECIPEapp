package com.example.foodapp.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodapp.presentation.components.BottomNavigationBar
import com.example.foodapp.presentation.components.RecipeItem
import com.example.foodapp.presentation.screens.destinations.RecipeDetailsScreenDestination
import com.example.foodapp.presentation.screens.details.RecipeDetailsScreen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
){
    val recipes = viewModel.recipes
    val favourites = viewModel.favouriteList.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.isRefreshing
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar("home", navigator)
        }
    ) {
        if(viewModel.error == ""){
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.refreshRecipes()
                }
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    LazyVerticalGrid(columns = GridCells.Fixed(2)){
                        items(items = recipes){ recipe ->
                            var isFav by remember { mutableStateOf(false) }
                            isFav = favourites.value.any { r -> r.id == recipe.id }
                            RecipeItem(recipe = recipe, fav = isFav){ id ->
                                navigator.navigate(
                                    RecipeDetailsScreenDestination(id)
                                )
                            }
                        }
                    }

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