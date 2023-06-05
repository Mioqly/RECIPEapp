package com.example.foodapp.data.mapper

import com.example.foodapp.data.local.FavouriteRecipe
import com.example.foodapp.data.local.RecipeEntity
import com.example.foodapp.data.remote.dto.DailyMealDto
import com.example.foodapp.data.remote.dto.RecipeDto
import com.example.foodapp.data.remote.dto.SearchDto
import com.example.foodapp.domain.model.Nutrition
import com.example.foodapp.domain.model.Recipe

fun RecipeDto.toRecipe(): Recipe{
    return Recipe(
        id = id,
        aggregateLikes = aggregateLikes,
        analyzedInstructions = analyzedInstructions,
        cookingMinutes = cookingMinutes,
        dishTypes = if(dishTypes.isNotEmpty()) dishTypes[0] else null,
        extendedIngredients = extendedIngredients,
        glutenFree = glutenFree,
        image = image,
        instructions = instructions,
        preparationMinutes = preparationMinutes,
        readyInMinutes = readyInMinutes,
        summary = summary,
        title = title,
        vegan = vegan,
        vegetarian = vegetarian,
        veryHealthy = veryHealthy,
        veryPopular = veryPopular,
        servings = servings
    )
}

fun RecipeDto.toRecipeEntity(): RecipeEntity{
    return RecipeEntity(
        id = id,
        aggregateLikes = aggregateLikes,
        analyzedInstructions = analyzedInstructions,
        cookingMinutes = cookingMinutes,
        dishTypes = if(dishTypes.isNotEmpty()) dishTypes[0] else null,
        extendedIngredients = extendedIngredients,
        glutenFree = glutenFree,
        image = image,
        instructions = instructions,
        preparationMinutes = preparationMinutes,
        readyInMinutes = readyInMinutes,
        summary = summary,
        title = title,
        vegan = vegan,
        vegetarian = vegetarian,
        veryHealthy = veryHealthy,
        veryPopular = veryPopular,
        servings = servings
    )
}

fun RecipeEntity.toRecipe(): Recipe{
    return Recipe(
        id = id,
        aggregateLikes = aggregateLikes,
        analyzedInstructions = analyzedInstructions,
        cookingMinutes = cookingMinutes,
        dishTypes = dishTypes,
        extendedIngredients = extendedIngredients,
        glutenFree = glutenFree,
        image = image,
        instructions = instructions,
        preparationMinutes = preparationMinutes,
        readyInMinutes = readyInMinutes,
        summary = summary,
        title = title,
        vegan = vegan,
        vegetarian = vegetarian,
        veryHealthy = veryHealthy,
        veryPopular = veryPopular,
        servings = servings
    )
}

fun Recipe.toFavouriteRecipe(): FavouriteRecipe{
    return FavouriteRecipe(
        id = id,
        aggregateLikes = aggregateLikes,
        analyzedInstructions = analyzedInstructions,
        cookingMinutes = cookingMinutes,
        dishTypes = dishTypes,
        extendedIngredients = extendedIngredients,
        glutenFree = glutenFree,
        image = image,
        instructions = instructions,
        preparationMinutes = preparationMinutes,
        readyInMinutes = readyInMinutes,
        summary = summary,
        title = title,
        vegan = vegan,
        vegetarian = vegetarian,
        veryHealthy = veryHealthy,
        veryPopular = veryPopular,
        servings = servings,
        calories = nutrition?.calories?.filter { it.isDigit() }?.toInt(),
        fat = nutrition?.fat?.filter { it.isDigit() }?.toInt(),
        protein = nutrition?.protein?.filter { it.isDigit() }?.toInt(),
        carbs = nutrition?.carbs?.filter { it.isDigit() }?.toInt(),
    )
}

fun FavouriteRecipe.toRecipe(): Recipe{
    return Recipe(
        id = id,
        aggregateLikes = aggregateLikes,
        analyzedInstructions = analyzedInstructions,
        cookingMinutes = cookingMinutes,
        dishTypes = dishTypes,
        extendedIngredients = extendedIngredients,
        glutenFree = glutenFree,
        image = image,
        instructions = instructions,
        preparationMinutes = preparationMinutes,
        readyInMinutes = readyInMinutes,
        summary = summary,
        title = title,
        vegan = vegan,
        vegetarian = vegetarian,
        veryHealthy = veryHealthy,
        veryPopular = veryPopular,
        servings = servings,
        nutrition = Nutrition(
            calories = calories.toString(),
            fat = fat.toString(),
            protein = protein.toString(),
            carbs = carbs.toString()
        )
    )
}

fun SearchDto.toRecipe(): Recipe{
    return Recipe(
        id = id,
        image = image,
        title = title
    )
}

fun DailyMealDto.toRecipe(): Recipe{
    return Recipe(
        id = id,
        title = title,
        image = image,
        nutrition = Nutrition(
            calories = calories.toString(),
            fat = fat,
            protein = protein,
            carbs = carbs
        )
    )
}