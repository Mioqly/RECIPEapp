package com.example.foodapp.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.foodapp.data.remote.features.AnalyzedInstruction
import com.example.foodapp.data.remote.features.ExtendedIngredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun toInstructionsJson(instructions: List<AnalyzedInstruction>) : String {
        return Gson().toJson(
            instructions,
            object : TypeToken<ArrayList<AnalyzedInstruction>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromInstructionsJson(json: String): List<AnalyzedInstruction>{
        return Gson().fromJson<ArrayList<AnalyzedInstruction>>(
            json,
            object: TypeToken<ArrayList<AnalyzedInstruction>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toIngredientsJson(ingredients: List<ExtendedIngredient>) : String {
        return Gson().toJson(
            ingredients,
            object : TypeToken<ArrayList<ExtendedIngredient>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromIngredientsJson(json: String): List<ExtendedIngredient>{
        return Gson().fromJson<ArrayList<ExtendedIngredient>>(
            json,
            object: TypeToken<ArrayList<ExtendedIngredient>>(){}.type
        ) ?: emptyList()
    }
}