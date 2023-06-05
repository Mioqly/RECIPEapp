package com.example.foodapp.presentation.screens.details

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.foodapp.R
import com.example.foodapp.data.remote.features.Equipment
import com.example.foodapp.data.remote.features.ExtendedIngredient
import com.example.foodapp.data.remote.features.Step
import com.example.foodapp.domain.model.Nutrition
import com.example.foodapp.domain.model.PieChartData
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.presentation.components.FavIcon
import com.example.foodapp.ui.theme.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.accompanist.pager.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun RecipeDetailsScreen(
    id: String,
    navigator: DestinationsNavigator,
    viewModel: DetailsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val recipe = viewModel.details
    val nutritions = viewModel.details.nutrition
    var isFavourite by remember { mutableStateOf(false) }
    isFavourite = viewModel.favouriteList.collectAsState().value.any { b -> b.id == recipe.id }

    if(viewModel.error == "" && !viewModel.isLoading){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = recipe.image ?: R.drawable.placeholder,
                        builder = {
                            placeholder(R.drawable.placeholder)
                        }
                    ),
                    contentDescription = "recipe image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
                FavIcon(
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp)
                        .size(40.dp)
                        .clip(shape = RoundedCornerShape(size = 24.dp))
                        .align(Alignment.TopEnd)
                        .background(Color.Black.copy(alpha = 0.3f)),
                    fav = isFavourite
                ){
                    if(isFavourite){
                        viewModel.deleteFavourite(recipe)
                        Toast.makeText(context, "Usunięto z ulubionych", Toast.LENGTH_SHORT).show()
                    }else{
                        viewModel.addFavourite(recipe)
                        Toast.makeText(context, "Dodano do ulubionych", Toast.LENGTH_SHORT).show()
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 8.dp, start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "arrow back",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navigator.popBackStack()
                            },
                        tint = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 16.dp)
                        .clip(shape = RoundedCornerShape(size = 24.dp))
                        .align(Alignment.BottomStart)
                        .background(Color.Black.copy(alpha = 0.3f))
                ) {
                    if(recipe.dishTypes != null){
                        Text(
                            text = recipe.dishTypes,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

            TabLayout(
                recipe = recipe,
                nutritions = nutritions
            )
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(
    recipe: Recipe,
    nutritions: Nutrition?
) {
    val pagerState = rememberPagerState(pageCount = 3)

    Column(
        modifier = Modifier.background(Color.White)
    ) {
        Tabs(pagerState = pagerState)
        TabsContent(pagerState = pagerState, recipe = recipe, nutritions = nutritions)
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        "Summary" to Icons.Default.Home,
        "Ingredients" to Icons.Default.ShoppingCart,
        "Steps" to Icons.Default.Settings
    )
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = GreenDarkPrimary,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = GreenDarkPrimary,
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    if(pagerState.currentPage == index){
                        Text(
                            list[index].first,
                            color = GreenDarkPrimary
                        )
                    }else{
                        Text(
                            list[index].first,
                            color = Color.Gray
                        )
                    }
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, recipe: Recipe, nutritions: Nutrition?) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> SummaryContent(recipe = recipe, nutritions = nutritions)
            1 -> IngredientsContent(recipe = recipe)
            2 -> StepsContent(recipe = recipe)
        }
    }
}

@Composable
fun StepsContent(
    recipe: Recipe
){
    var click by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        LazyColumn(){
            items(items = recipe.analyzedInstructions!![0].steps){ step ->
                StepItem(step = step)
            }
        }
    }
}

@Composable
fun StepItem(step: Step){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        var expandedContent by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${step.number}. ${step.step}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            if(step.equipment.isNotEmpty()){
                Row(
                    modifier = Modifier.clickable {
                        expandedContent = !expandedContent
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Equipment",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Icon(
                        imageVector = if(expandedContent) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "arrow",
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )
                }
                AnimatedVisibility(visible = expandedContent) {
                    LazyRow(){
                        items(items = step.equipment){ item ->
                            EquipmentItem(equipment = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EquipmentItem(
    equipment: Equipment
){
    Card(
        modifier = Modifier
            .padding(4.dp)
            .size(90.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val equipmentImage = "https://spoonacular.com/cdn/equipment_100x100/${equipment.image}"
            Image(
                painter = rememberImagePainter(
                    data = equipmentImage ?: R.drawable.placeholder,
                    builder = {
                        placeholder(R.drawable.placeholder)
                    }
                ),
                contentDescription = "equipment image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
            Text(
                text = equipment.name,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp),
                fontSize = 13.sp
            )
        }
    }
}


@Composable
fun IngredientsContent(
    recipe: Recipe
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ){
            items(items = recipe.extendedIngredients!!){ ingredient ->
                IngredientItem(ingredient)
            }
        }
    }
}

@Composable
fun IngredientItem(
    ingredient: ExtendedIngredient
){
    Card(
        modifier = Modifier
            .padding(4.dp)
            .height(130.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 16.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imagePath = "https://spoonacular.com/cdn/ingredients_100x100/${ingredient.image}"
            Image(
                painter = rememberImagePainter(
                    data = imagePath ?: R.drawable.placeholder,
                    builder = {
                        placeholder(R.drawable.placeholder)
                    }
                ),
                contentDescription = "ingredient image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(70.dp)
                    .padding(top = 4.dp)
            )
            Text(
                text = ingredient.original,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp),
                fontSize = 13.sp
            )
        }

    }
}

@Composable
fun SummaryContent(
    recipe: Recipe,
    nutritions: Nutrition?
){
    var showMore by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = recipe.title ?: "",
            fontSize = 30.sp,
            color = Color.Black,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold
        )
        InfoRow(recipe = recipe)
        HealthyRow(recipe = recipe)
        //VeganCard(recipe = recipe)
        Column(modifier = Modifier
            .animateContentSize(animationSpec = tween(100))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { showMore = !showMore }) {

            val summary = HtmlCompat.fromHtml(recipe.summary!! ,HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            if (showMore) {
                Text(
                    text = summary,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
                Text(
                    text = "Hide",
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = Color.Blue
                )
            } else {
                Text(
                    text = summary,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Show more",
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = Color.Blue
                )
            }
        }
        NutritionsSection(nutritions = nutritions)
    }
}

@Composable
fun NutritionsSection(
    nutritions: Nutrition?
){
    if(nutritions != null){
        val getPieChartData = listOf(
            PieChartData("Fat", nutritions.fat.filter { it.isDigit() }.toFloat()),
            PieChartData("Carbs", nutritions.carbs.filter { it.isDigit() }.toFloat()),
            PieChartData("Protein", nutritions.protein.filter { it.isDigit() }.toFloat()),
        )
        val calories = nutritions.calories.filter { it.isDigit() }.toInt()
        PieChart(nutritionValues = getPieChartData, calories = calories)
    }
}

@Composable
fun PieChart(
    nutritionValues: List<PieChartData>,
    calories: Int
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painterResource(id = R.drawable.kcal),
                    contentDescription = "kcal icon",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 2.dp)
                )
                Text(
                    text = "$calories kcal",
                    fontFamily = FontFamily.Default,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .size(250.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Crossfade(targetState = nutritionValues) { pieChartData ->
                    // on below line we are creating an
                    // android view for pie chart.
                    AndroidView(factory = { context ->
                        // on below line we are creating a pie chart
                        // and specifying layout params.
                        PieChart(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                // on below line we are specifying layout
                                // params as MATCH PARENT for height and width.
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            // on below line we are setting description
                            // enables for our pie chart.
                            this.description.isEnabled = false

                            // on below line we are setting draw hole
                            // to false not to draw hole in pie chart
                            this.isDrawHoleEnabled = false

                            // on below line we are enabling legend.
                            this.legend.isEnabled = true

                            // on below line we are specifying
                            // text size for our legend.
                            this.legend.textSize = 14F

                            // on below line we are specifying
                            // alignment for our legend.
                            this.legend.horizontalAlignment =
                                Legend.LegendHorizontalAlignment.CENTER

                            // on below line we are specifying entry label color as white.
                            this.setEntryLabelColor(resources.getColor(R.color.white))
                        }
                    },
                        // on below line we are specifying modifier
                        // for it and specifying padding to it.
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(5.dp), update = {
                            // on below line we are calling update pie chart
                            // method and passing pie chart and list of data.
                            updatePieChartWithData(it, pieChartData)
                        })
                }
            }
        }
    }
}

// on below line we are creating a update pie
// chart function to update data in pie chart.
fun updatePieChartWithData(
    // on below line we are creating a variable
    // for pie chart and data for our list of data.
    chart: PieChart,
    data: List<PieChartData>
) {
    // on below line we are creating
    // array list for the entries.
    val entries = ArrayList<PieEntry>()

    // on below line we are running for loop for
    // passing data from list into entries list.
    for (i in data.indices) {
        val item = data[i]
        entries.add(PieEntry(item.value ?: 0f, item.browserName ?: ""))
    }

    // on below line we are creating
    // a variable for pie data set.
    val ds = PieDataSet(entries, "")

    // on below line we are specifying color
    // int the array list from colors.
    ds.colors = arrayListOf(
        greenColor.toArgb(),
        blueColor.toArgb(),
        redColor.toArgb(),
    )
    // on below line we are specifying position for value
    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    // on below line we are specifying position for value inside the slice.
    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    // on below line we are specifying
    // slice space between two slices.
    ds.sliceSpace = 2f

    // on below line we are specifying text color
    //ds.valueTextColor = resources.getColor(R.color.white)
    ds.valueTextColor = Color.White.toArgb()

    // on below line we are specifying
    // text size for value.
    ds.valueTextSize = 18f

    // on below line we are specifying type face as bold.
    ds.valueTypeface = Typeface.DEFAULT_BOLD

    // on below line we are creating
    // a variable for pie data
    val d = PieData(ds)

    // on below line we are setting this
    // pie data in chart data.
    chart.data = d

    // on below line we are
    // calling invalidate in chart.
    chart.invalidate()
}


@Composable
fun HealthyRow(
    recipe: Recipe
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ){
        if(recipe.vegan!!){
            Card(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = GreenDarkPrimary,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "Vegan",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
        if(recipe.vegetarian!!){
            Card(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = GreenDarkPrimary,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "Vegetarian",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
        if(recipe.glutenFree!!){
            Card(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = GreenDarkPrimary,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "Gluten free",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun InfoRow(
    recipe: Recipe
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Default.Timer,
            contentDescription = "time icon",
            modifier = Modifier,
            tint = Color.Black
        )
        Text(
            text = "${recipe.readyInMinutes}m",
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
        )
        Divider(
            color = Color.Black,
            thickness = 2.dp,
            modifier = Modifier
                .width(2.dp)
                .height(20.dp)
        )
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "favourite icon",
            modifier = Modifier.padding(start = 8.dp),
            tint = Color.Black,
        )
        Text(
            text = "${recipe.aggregateLikes}",
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
        )
        Divider(
            color = Color.Black,
            thickness = 2.dp,
            modifier = Modifier
                .width(2.dp)
                .height(20.dp)
        )
        Icon(
            imageVector = Icons.Default.SoupKitchen,
            contentDescription = "servings icon",
            modifier = Modifier.padding(start = 8.dp),
            tint = Color.Black,
        )
        Text(
            text = "${recipe.servings} servings",
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}




