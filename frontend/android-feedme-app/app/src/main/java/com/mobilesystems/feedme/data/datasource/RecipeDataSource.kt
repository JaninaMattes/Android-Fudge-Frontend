package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.domain.model.Recipe

interface RecipeDataSource {

    suspend fun getAllRecipesByUserId(userId: Int): Response<List<Recipe>>?

    suspend fun getBestRatedRecipesByUserId(userId: Int): Response<List<Recipe>>?

    suspend fun updateRatingByRecipeId(recipeId: Int, rating: Float)

    suspend fun addRecipeToFavoritesByUserId(userId: Int, recipeId: Int)
}