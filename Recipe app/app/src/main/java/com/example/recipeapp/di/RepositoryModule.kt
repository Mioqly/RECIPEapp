package com.example.foodapp.di

import com.example.foodapp.data.json.GsonParser
import com.example.foodapp.data.json.JsonParser
import com.example.foodapp.data.remote.features.AnalyzedInstruction
import com.example.foodapp.data.repository.FoodRepositoryImpl
import com.example.foodapp.domain.repository.FoodRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFoodRepository(
        foodRepositoryImpl: FoodRepositoryImpl
    ): FoodRepository

//    @Binds
//    @Singleton
//    abstract fun bindJsonParser(
//        gsonParser: GsonParser
//    ): JsonParser
}