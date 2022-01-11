package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.placeholder.RecipePlaceholderContent
import com.mobilesystems.feedme.data.remote.FoodTrackerApi
import com.mobilesystems.feedme.di.AppModule
import com.mobilesystems.feedme.domain.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RecipeDataSourceImpl @Inject constructor(
    private val foodTrackerApi: FoodTrackerApi,
    @AppModule.DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : RecipeDataSource {

    override suspend fun getAllRecipesByUserId(userId: Int): Response<List<Recipe>>? {
        return try {

            // TODO: Remove placeholder data with network call to backend
            val fakeRecipeList = RecipePlaceholderContent.VARIOUS_ITEMS

            Response.Success(fakeRecipeList)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }

    override suspend fun getBestRatedRecipesByUserId(userId: Int): Response<List<Recipe>>? {
        return try {

            // TODO: Remove placeholder data with network call to backend
            var fakeRecipeList = RecipePlaceholderContent.VARIOUS_ITEMS
            var tempList = fakeRecipeList.filter { it.rating > 4}
            fakeRecipeList = tempList as MutableList<Recipe>

            Response.Success(fakeRecipeList)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }

    override suspend fun updateRatingByRecipeId(recipeId: Int, rating: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun addRecipeToFavoritesByUserId(userId: Int, recipeId: Int) {
        TODO("Not yet implemented")
    }
}