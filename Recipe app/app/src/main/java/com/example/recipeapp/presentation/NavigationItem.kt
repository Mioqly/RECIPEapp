package com.example.foodapp.presentation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.foodapp.R

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object Search : NavigationItem("search", Icons.Default.Search, "Search")
    object Home : NavigationItem("home", Icons.Default.Home, "Home")
    object Favourite : NavigationItem("favourite", Icons.Default.Favorite, "Favourite")
    object Daily : NavigationItem("daily", Icons.Default.Fastfood, "Daily meals")
}
