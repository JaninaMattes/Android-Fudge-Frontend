package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.placeholder.ShoppingListPlaceholderContent
import com.mobilesystems.feedme.data.remote.FoodTrackerApi
import com.mobilesystems.feedme.di.AppModule
import com.mobilesystems.feedme.domain.model.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ShoppingListDataSourceImpl @Inject constructor(
    private val foodTrackerApi: FoodTrackerApi,
    @AppModule.DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : ShoppingListDataSource{

    private var fakeCurrentShoppingList: MutableList<Product>
    private var fakeOldShoppingList: MutableList<Product>

    init {
        fakeCurrentShoppingList = ShoppingListPlaceholderContent.VARIOUS_ITEMS_CURRENT_SHOP
        fakeOldShoppingList = ShoppingListPlaceholderContent.VARIOUS_ITEMS_OLD_SHOP
    }

    override suspend fun loadAllProductsInCurrentShoppingList(): Response<List<Product>>? {
        return try {
            // TODO: Remove placeholder data with network call to backend
            Response.Success(fakeCurrentShoppingList)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }

    override suspend fun loadAllProductsInOldShoppingList(): Response<List<Product>>? {
        return try {
            // TODO: Remove placeholder data with network call to backend
            Response.Success(fakeOldShoppingList)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }

    override suspend fun addProductToCurrentShoppingList(userId: Int, product: Product) {
            // TODO: Remove placeholder data with network call to backend
            fakeCurrentShoppingList.add(product)
    }

    override suspend fun addProductToOldShoppingList(userId: Int, product: Product) {
            // TODO: Remove placeholder data with network call to backend
            fakeOldShoppingList.add(product)
    }

    override suspend fun updateCurrentShoppingList(userId: Int, shoppingList: List<Product>?) {
        // TODO: Pass to database
        fakeCurrentShoppingList = shoppingList as MutableList<Product>
    }

    override suspend fun updateOldShoppingList(userId: Int, shoppingList: List<Product>?) {
        // TODO: Pass to database
        fakeOldShoppingList = shoppingList as MutableList<Product>
    }

    override suspend fun removeProductFromOldShoppingList(userId: Int, product: Product) {
        // TODO: Pass to database
        fakeOldShoppingList.remove(product)
    }

    override suspend fun removeProductFromCurrentShoppingList(userId: Int, product: Product){
        // TODO: Pass to database
        fakeCurrentShoppingList.remove(product)
    }

    override suspend fun loadSuggestedProductsForShoppingList(): Response<List<Product>>? {
        // Helper function for future features
        return try {
            // TODO: Remove placeholder data with network call to backend
            fakeOldShoppingList = ShoppingListPlaceholderContent.VARIOUS_ITEMS_OLD_SHOP
            Response.Success(fakeCurrentShoppingList)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }
}