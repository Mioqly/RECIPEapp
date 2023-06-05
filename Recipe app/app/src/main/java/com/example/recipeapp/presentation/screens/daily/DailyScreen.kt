package com.example.foodapp.presentation.screens.daily

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.foodapp.R
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.presentation.components.*
import com.example.foodapp.presentation.screens.destinations.RecipeDetailsScreenDestination
import com.example.foodapp.ui.theme.FilterColor
import com.example.foodapp.ui.theme.GreenDarkPrimary
import com.example.foodapp.ui.theme.GreenPrimary
import com.example.foodapp.ui.theme.MyGray
import com.example.foodapp.util.Constants
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel

@Destination
@Composable
fun DailyScreen(
    navigator: DestinationsNavigator,
    viewModel: DailyViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar("daily", navigator)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                buildAnnotatedString {
                    append("Select your daily ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = GreenDarkPrimary)) {
                        append("kcal ")
                    }
                    append("goal")
                },
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 23.sp
            )
            CaloriesSlider(
                viewModel = viewModel,
                decreaseKcal = {
                    if(viewModel.kcalValue > 1200)
                        viewModel.kcalValue -= 100
                    viewModel.changeDefaultMacro()
                },
                increaseKcal = {
                    if(viewModel.kcalValue < 5000)
                        viewModel.kcalValue += 100
                    viewModel.changeDefaultMacro()
                }
            ){
                viewModel.kcalValue = it
                viewModel.changeDefaultMacro()
            }
            MacronutrientsSection(viewModel = viewModel){
                viewModel.manualValues = !viewModel.manualValues
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Meals:",
                    style = MaterialTheme.typography.h5
                )
                if(viewModel.breakfast == null){
                    Button(
                        onClick = { viewModel.getMeals() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = GreenPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(35.dp)
                    ) {
                        Text(text = "Search", fontSize = 14.sp)
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }else{
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh icon",
                        tint = GreenPrimary,
                        modifier = Modifier
                            .size(35.dp)
                            .clickable {
                                viewModel.getMeals()
                            }
                    )
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            if(viewModel.isLoading){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }else{
                MealItem(
                    meal = viewModel.breakfast,
                    label = "Breakfast",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    navigator = navigator
                )
                MealItem(
                    meal = viewModel.lunch,
                    label = "Lunch",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    navigator = navigator
                )
                MealItem(
                    meal = viewModel.dinner,
                    label = "Dinner",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    navigator = navigator
                )
            }
        }
    }
}

@Composable
fun MealItem(
    meal: Recipe?,
    label: String,
    modifier: Modifier,
    navigator: DestinationsNavigator
){
    Row(
        modifier = modifier
    ){
        if(meal != null){
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if(meal.id != null){
                        Row(modifier = Modifier.clickable {
                            navigator.navigate(RecipeDetailsScreenDestination(meal.id.toString()))
                        }) {
                            Image(
                                painter = rememberImagePainter(
                                    data = meal.image ?: R.drawable.placeholder,
                                    builder = {
                                        placeholder(R.drawable.placeholder)
                                    }
                                ),
                                contentDescription = "recipe image",
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(150.dp),
                                //.clip(shape = RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Column() {
                                Text(
                                    text = label,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp, end = 4.dp),
                                    textAlign = TextAlign.End,
                                    style = MaterialTheme.typography.caption
                                )
                                Text(
                                    text = meal.title!!,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    fontSize = 17.sp,
                                    modifier = Modifier.padding(start = 4.dp),
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "fats: ${meal.nutrition!!.fat}",
                                    fontSize = 13.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 4.dp, top = 8.dp)
                                )
                                Text(
                                    text = "carbs: ${meal.nutrition.carbs}",
                                    fontSize = 13.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 4.dp)
                                )
                                Row() {
                                    Text(
                                        text = "proteins: ${meal.nutrition.protein}",
                                        fontSize = 13.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .padding(start = 4.dp)
                                    )
                                    Image(
                                        painterResource(id = R.drawable.kcal),
                                        contentDescription = "kcal icon",
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                    Text(
                                        text = "${meal.nutrition.calories} kcal",
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                                    )
                                }
                            }
                        }
                    }else{
                        Text(
                            text = label,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Meal not found",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun CaloriesSlider(
    viewModel: DailyViewModel,
    increaseKcal: () -> Unit,
    decreaseKcal: () -> Unit,
    onChange: (Float) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundIconButton(
                imageVector = Icons.Default.Remove,
                backgroundColor = MyGray,
                onClick = { decreaseKcal() }
            )
            Text(
                text = "${viewModel.kcalValue.toInt()}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            RoundIconButton(
                imageVector = Icons.Default.Add,
                backgroundColor = MyGray,
                onClick = { increaseKcal() }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Image(
                painterResource(id = R.drawable.healthy_food),
                contentDescription = "healthy icon",
                modifier = Modifier
                    .size(40.dp),
                colorFilter = ColorFilter.tint(GreenPrimary)
            )
            Slider(
                value = viewModel.kcalValue,
                onValueChange = {
                    onChange(it)
                },
                valueRange = 1200f..5000f,
                steps = 37,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .weight(1f)
            )
            Image(
                painterResource(id = R.drawable.fast_food),
                contentDescription = "fast food icon",
                modifier = Modifier
                    .size(45.dp)
                    .padding(bottom = 8.dp),
                colorFilter = ColorFilter.tint(Color.Red)
            )
        }
        
    }
}

@Composable
fun MacronutrientsSection(
    viewModel: DailyViewModel,
    changeManual: () -> Unit
){
    Card(
        modifier = Modifier.padding(top = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = viewModel.manualValues,
                    onCheckedChange = {changeManual()},
                    modifier = Modifier
                        .size(15.dp)
                        .padding(start = 8.dp)
                )
                Text(
                    text = "Set manually nutritions",
                    modifier = Modifier.padding(start = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Unit:",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    MultiToggleButton(currentSelection = viewModel.currentUnit, toggleStates = Constants.DAILY_UNITS){
                        viewModel.currentUnit = it
                        viewModel.changeDefaultMacro()
                        viewModel.checkMacroSum()
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row() {
                MacroSetting(
                    modifier = Modifier.weight(1f),
                    enabled = viewModel.manualValues,
                    unit = viewModel.currentUnit,
                    macroLabel = "Fats",
                    macroValue = viewModel.fats.toString(),
                    changeMacro = {
                        viewModel.fats = if(it.isNotEmpty())
                            it.toInt()
                        else
                            0
                        viewModel.checkMacroSum()
                    }
                )
                MacroSetting(
                    modifier = Modifier.weight(1f),
                    enabled = viewModel.manualValues,
                    unit = viewModel.currentUnit,
                    macroLabel = "Carbs",
                    macroValue = viewModel.carbs.toString(),
                    changeMacro = {
                        viewModel.carbs = if(it.isNotEmpty())
                            it.toInt()
                        else
                            0
                        viewModel.checkMacroSum()
                    }
                )
                MacroSetting(
                    modifier = Modifier,
                    enabled = viewModel.manualValues,
                    unit = viewModel.currentUnit,
                    macroLabel = "Protein",
                    macroValue = viewModel.proteins.toString(),
                    changeMacro = {
                        viewModel.proteins = if(it.isNotEmpty())
                            it.toInt()
                        else
                            0
                        viewModel.checkMacroSum()
                    }
                )
            }
            if(!viewModel.valid){
                val goal = if(viewModel.currentUnit == "%") "100%" else "${viewModel.kcalValue.toInt()} g"
                Text(
                    text = "Sum of macronutrients must be equal $goal",
                    fontSize = 13.sp,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun MacroSetting(
    modifier: Modifier,
    unit: String,
    enabled: Boolean,
    macroLabel: String,
    macroValue: String,
    changeMacro: (String) -> Unit
){
    Column(
        modifier = modifier
    ) {
        Row() {
            Text(text = "$macroLabel: ")
            if(enabled){
                BasicTextField(modifier = Modifier
                    .background(
                        color = MyGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(top = 2.dp)
                    .height(25.dp)
                    .width(55.dp),
                    value = macroValue,
                    onValueChange = {
                        changeMacro(it)
                    },
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colors.primary),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }else{
                Text(
                    text = macroValue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(text = unit, modifier = Modifier.padding(start = 2.dp))
        }
    }
}
