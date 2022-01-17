package com.mobilesystems.feedme.data.repository

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.InventoryDataSourceImpl
import com.mobilesystems.feedme.data.datasource.RecipeDataSourceImpl
import com.mobilesystems.feedme.data.datasource.UserDataSourceImpl
import com.mobilesystems.feedme.data.response.UserResponse
import com.mobilesystems.feedme.domain.model.FoodType
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.domain.model.Recipe
import com.mobilesystems.feedme.domain.model.User
import com.mobilesystems.feedme.ui.common.utils.convertUserResponse
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val inventoryDataSource: InventoryDataSourceImpl,
    private val recipeDataSourceImpl: RecipeDataSourceImpl,
    private val userDataSourceImpl: UserDataSourceImpl) : DashboardRepository {

    // in-memory cache of the fetched objects
    var expiringProductList: MutableLiveData<List<Product>?> = MutableLiveData<List<Product>?>()
        private set
    var noOneRecipeList: MutableLiveData<List<Recipe>?> = MutableLiveData<List<Recipe>?>()
        private set
    var loggedInUser: MutableLiveData<User?> = MutableLiveData<User?>()

    override suspend fun getCurrentLoggedInUser(userId: Int): MutableLiveData<User?> {
        // get currently logged in user
        var user: User? = null
        val result = userDataSourceImpl.getUserById(userId)
        val userResponse = result.data
        if(userResponse != null) {
            user = convertUserResponse(userResponse)
        }
        loggedInUser.postValue(user)
        return loggedInUser
    }

    override suspend fun getNumberOneRecipes(userId: Int): MutableLiveData<List<Recipe>?> {
        // TODO get only number one Recipes
        val result = recipeDataSourceImpl.getAllRecipesByUserId(userId)

        if (result is Response.Success) {
            noOneRecipeList.postValue(result.data)
        }
        return noOneRecipeList
    }

    override suspend fun getAllExpiringProducts(userId: Int): MutableLiveData<List<Product>?> {
        // get all products for current user
        // TODO get expiring Products
        val result = inventoryDataSource.getAllProductsInInventoryList(userId)

        if (result is Response.Success) {
            expiringProductList.postValue(result.data)
        }
        return expiringProductList
    }

    override suspend fun getProductsAroundMe(usrId: Int): MutableLiveData<Map<Location, Product>?> {
        // For future features
        TODO("Not yet implemented")
    }
}