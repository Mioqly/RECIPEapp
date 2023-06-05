package com.example.foodapp.presentation.screens.daily

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.domain.model.Ingredient
import com.example.foodapp.domain.model.Recipe
import com.example.foodapp.domain.repository.FoodRepository
import com.example.foodapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val repository: FoodRepository
): ViewModel() {

    var breakfast by mutableStateOf<Recipe?>(null)
    var lunch by mutableStateOf<Recipe?>(null)
    var dinner by mutableStateOf<Recipe?>(null)

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")

    var manualValues by mutableStateOf(false)
    var kcalValue by mutableStateOf(2500f)
    var fats by mutableStateOf(25)
    var carbs by mutableStateOf(55)
    var proteins by mutableStateOf(20)
    var currentUnit by mutableStateOf("%")

    var valid by mutableStateOf(true)

    fun checkMacroSum(){
        valid = if(currentUnit == "%"){
            fats + carbs + proteins == 100
        }else{
            //przeliczanie z kcal na gramy i odwrotnie powoduje niedokładnosc dlatego biorę akceptowalny zakres 15 kcal
            val kcalSum = fats * 9 + carbs * 4 + proteins * 4
            kcalSum >= kcalValue - 15 && kcalSum <= kcalValue + 15
        }
    }

    fun changeDefaultMacro(){
        if(currentUnit == "%"){
            fats = 25
            carbs = 55
            proteins = 20
        }else{
            fats = (kcalValue * 0.25 / 9).toInt()
            carbs = (kcalValue * 0.55 / 4).toInt()
            proteins = (kcalValue * 0.2 / 4).toInt()
        }
    }

    fun getMeals(){
        viewModelScope.launch {
            isLoading = true
            val breakfastResult = async {
                val kcalMeal = (kcalValue * 0.3).toInt()
                repository.searchDailyMeal(
                    minCalories = kcalMeal - 30,
                    maxCalories = kcalMeal + 30)
            }
            val lunchResult = async {
                val kcalMeal = (kcalValue * 0.45).toInt()
                repository.searchDailyMeal(
                    minCalories = kcalMeal - 30,
                    maxCalories = kcalMeal + 30
                )
            }
            val dinnerResult = async {
                val kcalMeal = (kcalValue * 0.25).toInt()
                repository.searchDailyMeal(
                    minCalories = kcalMeal - 30,
                    maxCalories = kcalMeal + 30
                )
            }

            when(val result = breakfastResult.await()){
                is Resource.Success ->{
                    result.data?.let{ recipe ->
                        breakfast = recipe
                    }
                    isLoading = false
                }
                is Resource.Error -> {
                    error = result.message!!
                    isLoading = false
                }
            }

            when(val result = lunchResult.await()){
                is Resource.Success ->{
                    result.data?.let{ recipe ->
                        lunch = recipe
                    }
                    isLoading = false
                }
                is Resource.Error -> {
                    error = result.message!!
                    isLoading = false
                }
            }

            when(val result = dinnerResult.await()){
                is Resource.Success ->{
                    result.data?.let{ recipe ->
                        dinner = recipe
                    }
                    isLoading = false
                }
                is Resource.Error -> {
                    error = result.message!!
                    isLoading = false
                }
            }
        }
    }

}