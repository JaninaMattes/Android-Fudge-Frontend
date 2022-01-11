package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.RecipeDataSourceImpl
import com.mobilesystems.feedme.domain.model.Recipe
import javax.inject.Inject

/**
 * https://developer.android.com/kotlin/flow
 * https://developer.android.com/topic/libraries/architecture/livedata
 */

class RecipeRepositoryImpl @Inject constructor(private val dataSourceImpl: RecipeDataSourceImpl) : RecipeRepository {

    // in-memory cache of the fetched objects
    var recipeList: MutableLiveData<List<Recipe>?> = MutableLiveData<List<Recipe>?>()
        private set

    override suspend fun loadAllRecipesBasedOnInventory(userId: Int): MutableLiveData<List<Recipe>?> {
        // get all products for current user
        val result = dataSourceImpl.getAllRecipesByUserId(userId)

        if (result is Response.Success) {
            if (result != null) {
                recipeList.postValue(result.data)
            }
        }
        return recipeList
    }

    override suspend fun loadBestRatedRecipesBasedOnInventory(userId: Int): MutableLiveData<List<Recipe>?> {
        // get all products for current user
        val result = dataSourceImpl.getBestRatedRecipesByUserId(userId)

        if (result is Response.Success) {
            if (result != null) {
                recipeList.postValue(result.data)
            }
        }
        return recipeList
    }

    override suspend fun rateRecipe(recipe: Recipe) {
        dataSourceImpl.updateRatingByRecipeId(recipe.recipeId, recipe.rating)
    }

    override suspend fun addRecipeToFavoriteList(userId: Int, recipeId: Int) {
        // Implement in future
        dataSourceImpl.addRecipeToFavoritesByUserId(userId, recipeId)
    }

}

