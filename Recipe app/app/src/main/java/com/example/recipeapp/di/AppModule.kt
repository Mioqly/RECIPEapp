package com.example.foodapp.di

import android.app.Application
import androidx.room.Room
import com.example.foodapp.data.json.JsonParser
import com.example.foodapp.data.local.Converters
import com.example.foodapp.data.local.FoodDatabase
import com.example.foodapp.data.remote.FoodApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFoodApi(): FoodApi {
        return Retrofit.Builder()
            .baseUrl(FoodApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideFoodDatabase(
        app: Application
    ): FoodDatabase {
        return Room.databaseBuilder(
            app,
            FoodDatabase::class.java,
            "fooddb.db"
        )
        .fallbackToDestructiveMigration()
        .addTypeConverter(Converters())
        .build()
    }
}