package com.example.foodapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.util.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("Delete from recipes")
    suspend fun clearRecipes()

    @Query("Select * from recipes")
    suspend fun getAllRecipes(): List<RecipeEntity>

    @Query("Select * from recipes where id=:id")
    suspend fun getRecipe(id: Int): RecipeEntity
}