package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.domain.model.Recipe

interface RecipeRepository {

    suspend fun loadAllRecipesBasedOnInventory(userId: Int): MutableLiveData<List<Recipe>?>

    suspend fun loadBestRatedRecipesBasedOnInventory(userId: Int): MutableLiveData<List<Recipe>?>

    suspend fun rateRecipe(recipe: Recipe)

    suspend fun addRecipeToFavoriteList(userId: Int, recipeId: Int)
}