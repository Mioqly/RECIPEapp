package com.example.foodapp.presentation.components

import android.view.MotionEvent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.foodapp.R
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.presentation.NavigationItem
import com.example.foodapp.presentation.screens.destinations.*
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import java.lang.Math.PI
import kotlin.math.*

@Composable
fun RecipeItem(
    recipe: Recipe,
    fav: Boolean?,
    onClick: (String) -> Unit
){
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(190.dp)
            .clickable {
                onClick(recipe.id.toString())
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 16.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = recipe.image ?: R.drawable.placeholder,
                        builder = {
                            placeholder(R.drawable.placeholder)
                        }
                    ),
                    contentDescription = "recipe image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                if(fav != null){
                    FavIcon(
                        modifier = Modifier
                            .padding(top = 4.dp, end = 4.dp)
                            .size(30.dp)
                            .clip(shape = RoundedCornerShape(size = 24.dp))
                            .align(Alignment.TopEnd)
                            .background(Color.Black.copy(alpha = 0.3f)),
                        fav = fav
                    ){}
                }
                if(recipe.readyInMinutes != null){
                    Box(
                        modifier = Modifier
                            .padding(bottom = 4.dp, start = 4.dp)
                            .width(50.dp)
                            .height(20.dp)
                            .clip(shape = RoundedCornerShape(size = 8.dp))
                            .align(Alignment.BottomStart)
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "time icon",
                                modifier = Modifier
                                    .size(15.dp),
                                tint = Color.White
                            )
                            Text(
                                text = "${recipe.readyInMinutes}m",
                                fontSize = 13.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Row(){
                Text(
                    text = recipe.title!!,
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
                if(recipe.aggregateLikes != null){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "likes icon",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = recipe.aggregateLikes.toString(),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    route: String,
    navigator: DestinationsNavigator
) {
    val items = listOf(
        NavigationItem.Daily,
        NavigationItem.Search,
        NavigationItem.Home,
        NavigationItem.Favourite
    )
    BottomNavigation(
        backgroundColor = Color(0xFF388E3C),
        contentColor = Color.White
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = null) },
                label = { Text(text = item.title) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.6f),
                alwaysShowLabel = true,
                selected = item.route == route,
                onClick = {
                    when(item.route){
                        "home" ->{
                            navigator.navigate(
                                HomeScreenDestination()
                            )
                        }
                        "favourite" ->{
                            navigator.navigate(
                                FavouritesScreenDestination()
                            )
                        }
                        "search" ->{
                            navigator.navigate(
                                SearchScreenDestination()
                            )
                        }
                        "daily" -> {
                            navigator.navigate(
                                DailyScreenDestination()
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FavIcon(
    modifier: Modifier,
    fav: Boolean,
    onClick: () -> Unit
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if(fav)Icons.Default.Star else Icons.Default.StarBorder,
            tint = if(fav) Color.Yellow else Color.Black,
            contentDescription = "favourite icon",
            modifier = Modifier
                .clickable {
                    onClick()
                }
        )
    }
}

@Composable
fun MultiToggleButton(
    currentSelection: String,
    toggleStates: List<String>,
    onToggleChange: (String) -> Unit
) {
    val selectedTint = MaterialTheme.colors.primary
    val unselectedTint = Color.Unspecified

    Row(modifier = Modifier
        .height(27.dp)
        .border(BorderStroke(1.dp, Color.LightGray))) {
        toggleStates.forEachIndexed { index, toggleState ->
            val isSelected = currentSelection.lowercase() == toggleState.lowercase()
            val backgroundTint = if (isSelected) selectedTint else unselectedTint
            val textColor = if (isSelected) Color.White else Color.Unspecified

            if (index != 0) {
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }

            Row(
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight()
                    .background(backgroundTint)
                    .padding(horizontal = 8.dp)
                    .toggleable(
                        value = isSelected,
                        enabled = true,
                        onValueChange = { selected ->
                            if (selected) {
                                onToggleChange(toggleState)
                            }
                        }),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = CenterVertically
            ) {
                Text(
                    toggleState,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.Black.copy(alpha = 0.8F),
    backgroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = 4.dp)
{
    Card(modifier = modifier.padding(4.dp)
        .clickable { onClick.invoke() }.then(Modifier.size(32.dp)),
        shape = CircleShape,
        backgroundColor = backgroundColor,
        elevation = elevation
    ){

        Icon(
            imageVector = imageVector,
            contentDescription = "plus or minus icon",
            tint = tint)
    }

}