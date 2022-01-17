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

    // TODO: Future integration of a map to bring in social component
    suspend fun getProductsAroundMe(usrId: Int): MutableLiveData<Map<Location, Product>?>
}