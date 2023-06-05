package com.example.foodapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert
    suspend fun insert(favouriteRecipe: FavouriteRecipe)

    @Delete
    suspend fun delete(favouriteRecipe: FavouriteRecipe)

    @Query("Select * from favourites")
    fun getFavourites(): Flow<List<FavouriteRecipe>>

    @Query("Select * from favourites where id=:id")
    suspend fun getFavouriteRecipe(id: Int): FavouriteRecipe
}