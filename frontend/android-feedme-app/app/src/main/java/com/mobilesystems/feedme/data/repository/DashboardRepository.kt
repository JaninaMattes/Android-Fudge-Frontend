package com.mobilesystems.feedme.data.repository

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.domain.model.Recipe
import com.mobilesystems.feedme.domain.model.User

interface DashboardRepository {

    suspend fun getCurrentLoggedInUser(userId: Int): MutableLiveData<User?>

    suspend fun getNumberOneRecipes(userId: Int): MutableLiveData<List<Recipe>?>

    suspend fun getAllExpiringProducts(userId: Int): MutableLiveData<List<Product>?>

    suspend fun updateCurrentShoppingList(userId: Int, currentShoppingList: List<Product>): Unit

    // Update single recipe rating
    suspend fun updateNumberOneRecipeProducts(userId: Int, recipe: Recipe): Unit

    // Remove a product from the list
    suspend fun removeExpiringProduct(userId: Int, product: Product): Unit

    // TODO: Future integration of map
    suspend fun getProductsAroundMe(usrId: Int): MutableLiveData<Map<Location, Product>?>
}