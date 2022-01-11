package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.placeholder.InventoryListPlaceholderContent
import com.mobilesystems.feedme.data.remote.FoodTrackerApi
import com.mobilesystems.feedme.di.AppModule
import com.mobilesystems.feedme.domain.model.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class InventoryDataSourceImpl @Inject constructor(
    private val foodTrackerApi: FoodTrackerApi,
    @AppModule.DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : InventoryDataSource {

    private lateinit var fakeInventoryList: MutableList<Product>

    // TODO: Pass token for authentification
    override suspend fun getAllProductsInInventoryList(userId: Int): Response<List<Product>>? {
        return try {

            // TODO: Remove placeholder data with network call to backend
            fakeInventoryList = InventoryListPlaceholderContent.VARIOUS_ITEMS
            //foodTrackerApi.getAllProductsInInventoryList(userId)
            Response.Success(fakeInventoryList)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }

    override suspend fun updateCurrentShoppingList(
        userId: Int,
        currentShoppingList: List<Product>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProductInventoryList(userId: Int, inventoryList: List<Product>?) {
        // TODO: Persist current state in backend database
        if (inventoryList != null) {
            fakeInventoryList = inventoryList.toMutableList()
        }
    }

    override suspend fun removeProductFromInventoryList(userId: Int, product: Product) {
        // TODO: Remove placeholder data with network call to backend
        try {
            fakeInventoryList.remove(product)
            Response.Success(fakeInventoryList)
        } catch (e: Throwable)
        {
            Response.Error("IOException")
        }
    }

}