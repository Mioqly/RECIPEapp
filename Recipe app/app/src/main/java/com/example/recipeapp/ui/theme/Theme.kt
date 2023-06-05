package com.example.foodapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = GreenPrimary,
    primaryVariant = GreenDarkPrimary,
    secondary = GreenAccent
)

private val LightColorPalette = lightColors(
    primary = GreenPrimary,
    primaryVariant = GreenDarkPrimary,
    secondary = GreenAccent

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun FoodAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

    val systemUiController = rememberSystemUiController()
    if(darkTheme){
        systemUiController.setStatusBarColor(
            color = Color(0xFF388E3C)
        )
    }else{
        systemUiController.setStatusBarColor(
            color = Color(0xFF388E3C)
        )
    }
}