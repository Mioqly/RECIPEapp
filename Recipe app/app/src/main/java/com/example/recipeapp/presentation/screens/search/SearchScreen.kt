package com.example.foodapp.presentation.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.foodapp.R
import com.example.foodapp.domain.model.Ingredient
import com.example.foodapp.presentation.components.BottomNavigationBar
import com.example.foodapp.presentation.components.RecipeItem
import com.example.foodapp.presentation.screens.destinations.RecipeDetailsScreenDestination
import com.example.foodapp.ui.theme.FilterColor
import com.example.foodapp.ui.theme.GreenDarkPrimary
import com.example.foodapp.ui.theme.OpenSans
import com.example.foodapp.ui.theme.RobotoSlab
import com.example.foodapp.util.Constants
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun SearchScreen(
    navigator: DestinationsNavigator,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery = viewModel.searchQuery
    val recipes = viewModel.recipes

    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar("search", navigator)
        }
    ) {
        if(!showFilters){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = it)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        viewModel.changeQuery(it)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    placeholder = {
                        Text(text = "Search...",)
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardActions = KeyboardActions{
                        viewModel.searchRecipes()
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                LazyRow() {
                    item {
                        Button(
                            onClick = { showFilters = !showFilters },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(start = 16.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
                                .height(30.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = FilterColor, contentColor = Black),
                            border = BorderStroke(width = 1.dp, color = Black.copy(alpha = 0.3f)),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(
                                    text = "Filters",
                                    fontSize = 12.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.FilterAlt,
                                    contentDescription = "filter icon",
                                    modifier = Modifier
                                        //.size(20.dp)
                                        .padding(start = 4.dp)
                                )
                            }
                        }
                    }
                    if(viewModel.ingredients.isNotEmpty()) {
                        item {
                            FilterButton(title = "Ingredients") {
                                viewModel.ingredients.clear()
                                viewModel.searchRecipes()
                            }
                        }
                    }
                    if(viewModel.cuisine.isNotEmpty()) {
                        item {
                            FilterButton(title = "Cuisines") {
                                viewModel.changeCuisine("")
                                viewModel.searchRecipes()
                            }
                        }
                    }
                    if(viewModel.diet.isNotEmpty()) {
                        item {
                            FilterButton(title = "Diets") {
                                viewModel.changeDiet("")
                                viewModel.searchRecipes()
                            }
                        }
                    }
                    if(viewModel.type.isNotEmpty()) {
                        item {
                            FilterButton(title = "Types") {
                                viewModel.changeType("")
                                viewModel.searchRecipes()
                            }
                        }
                    }
                }
                if(viewModel.isLoading){
                    LinearProgressIndicator(modifier = Modifier.then(Modifier.fillMaxWidth()))
                }
                else{
                    LazyVerticalGrid(columns = GridCells.Fixed(2)){
                        items(items = recipes){ recipe ->
                            RecipeItem(recipe = recipe, fav = null){ id ->
                                navigator.navigate(
                                    RecipeDetailsScreenDestination(id)
                                )
                            }
                        }
                    }
                }
            }
        }
        AnimatedVisibility(visible = showFilters) {
            FilterWindow(viewModel = viewModel){
                showFilters = false
                viewModel.ingredientQuery = ""
                viewModel.ingredientsTemp.clear()
            }
        }
    }
}

@Composable
fun FilterButton(
    title: String,
    onClick: () -> Unit
){
    Button(
        onClick = { },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
            .height(35.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = FilterColor,
            contentColor = Black
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Black.copy(alpha = 0.3f)
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = title,
                fontSize = 12.sp
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "filter icon",
                modifier = Modifier
                    //.size(20.dp)
                    .padding(start = 4.dp)
                    .clickable {
                        onClick()
                    }
            )
        }
    }
}

@Composable
fun FilterWindow(
    viewModel: SearchViewModel,
    closeWindow: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FilterHeader(closeWindow)
            FilterContent(viewModel = viewModel)
            FilterButtons(viewModel = viewModel, closeWindow = closeWindow)
        }
    }
}

@Composable
private fun FilterButtons(
    viewModel: SearchViewModel,
    closeWindow: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Button(
            onClick = {
                viewModel.clearFilters()
                closeWindow()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = LightGray,
                contentColor = White
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .weight(1f)
                .padding(start = 32.dp, end = 8.dp)
        ) {
            Text(
                text = "Clear",
                fontSize = 21.sp
            )
        }
        Button(
            onClick = {
                viewModel.applyFilters()
                closeWindow()
                viewModel.searchRecipes()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = GreenDarkPrimary,
                contentColor = White
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 32.dp)
        ) {
            Text(
                text = "Apply",
                fontSize = 21.sp
            )
        }
    }
}

@Composable
private fun FilterHeader(closeWindow: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Set filters",
            fontFamily = RobotoSlab,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp)
                .weight(1f)
        )
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "close icon",
            modifier = Modifier
                .clickable {
                    closeWindow()
                }
                .padding(top = 16.dp, end = 16.dp)
        )
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        thickness = 1.dp,
        color = LightGray.copy(alpha = 0.8f)
    )
}

@Composable
fun FilterContent(viewModel: SearchViewModel){

    viewModel.setTempFilters()

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxHeight(0.8f)
    ){
        item {
            var size by remember {
                mutableStateOf(0)
            }
            size = viewModel.ingredientsTemp.size
            if(size == 0){
                Text(
                    text = "Ingredients: 0",
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = OpenSans
                )
            }else{
                Text(
                    text = "Ingredients: $size",
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = OpenSans
                )
            }
            LazyRow{
                items(items = viewModel.ingredientsTemp){ ingredient ->
                    FilterButton(title = ingredient) {
                        viewModel.removeIngredient(ingredient)
                    }
                }
            }
            AutoCompleteSearchTextField(viewModel)
        }
        item {
            Text(
                text = "Cuisine",
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = OpenSans
            )
        }
        items(items = Constants.CUISINES){ cuisine ->
            var checkedItem by remember {
                mutableStateOf(false)
            }
            checkedItem = viewModel.cuisineTemp.contains(cuisine)
            SelectRow(value = cuisine, checked = checkedItem){
                viewModel.updateCuisineTemp(cuisine)
            }
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                thickness = 1.dp,
                color = LightGray.copy(alpha = 0.8f)
            )
        }

        //diet
        item {
            Text(
                text = "Diet",
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = OpenSans
            )
        }
        items(items = Constants.DIETS){ diet ->
            var checkedItem by remember {
                mutableStateOf(false)
            }
            checkedItem = viewModel.dietTemp.contains(diet)
            SelectRow(value = diet, checked = checkedItem){
                viewModel.updateDietTemp(diet)
            }
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                thickness = 1.dp,
                color = LightGray.copy(alpha = 0.8f)
            )
        }

        //types
        item {
            Text(
                text = "Meal type",
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = OpenSans
            )
        }
        items(items = Constants.TYPES){ type ->
            var checkedItem by remember {
                mutableStateOf(false)
            }
            checkedItem = viewModel.typeTemp.contains(type)
            SelectRow(value = type, checked = checkedItem){
                viewModel.updateTypeTemp(type)
            }
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                thickness = 1.dp,
                color = LightGray.copy(alpha = 0.8f)
            )
        }
    }
}


@Composable
fun SelectRow(
    value: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheck: () -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = value,
            fontSize = 18.sp,
            fontFamily = OpenSans
        )
        Checkbox(
            modifier = Modifier
                .size(15.dp)
                .padding(end = 16.dp),
            checked = checked,
            onCheckedChange = {
                onCheck()
        })
    }
}

@Composable
fun AutoCompleteSearchTextField(
    viewModel: SearchViewModel
){
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.8.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(15.dp)
                    ),
                value = viewModel.ingredientQuery,
                onValueChange = {
                    viewModel.searchIngredients(it)
                    expanded = true
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                textStyle = TextStyle(
                    color = Black,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = if(!expanded)Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                            contentDescription = "arrow",
                            tint = Color.Black
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = "Type ingredient name...",
                        color = LightGray
                    )
                }
            )
        }

        AnimatedVisibility(visible = expanded) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 5.dp),
//                    .width(textFieldSize.width.dp),
                elevation = 15.dp,
                shape = RoundedCornerShape(10.dp)
            ) {

                LazyColumn(
                    modifier = Modifier.heightIn(max = 220.dp),
                ) {

                    if (viewModel.ingredientQuery.isNotEmpty()) {
                        items(items = viewModel.ingredientsSuggestions, key = {it.name}) {
                            IngredientItem(ingredient = it) { title ->
                                viewModel.addIngredient(title)
                                expanded = false
                            }
                        }
                    }
                }

            }
        }

    }
}

@Composable
fun IngredientItem(
    ingredient: Ingredient,
    onSelect: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(ingredient.name)
            }
            .padding(10.dp)
    ) {
        Text(
            text = ingredient.name,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        val imagePath = "https://spoonacular.com/cdn/ingredients_100x100/${ingredient.image}"
        Image(
            painter = rememberImagePainter(
                data = imagePath,
                builder = {
                    placeholder(R.drawable.placeholder)
                }
            ),
            contentDescription = "ingredient image",
            modifier = Modifier
                .size(35.dp)
        )
    }

}