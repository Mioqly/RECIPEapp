package com.example.foodapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodapp.data.local.RecipeEntity

@Database(
    entities = [RecipeEntity::class, FavouriteRecipe::class],
    version = 5
)
@TypeConverters(Converters::class)
abstract class FoodDatabase: RoomDatabase() {

    abstract val recipeDao: RecipeDao
    abstract val favouriteDao: FavouriteDao
}