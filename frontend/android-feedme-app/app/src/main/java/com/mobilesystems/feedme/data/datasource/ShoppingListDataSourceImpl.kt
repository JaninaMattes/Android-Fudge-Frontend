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

    private lateinit var fakeCurrentShoppingList: MutableList<Product>
    private lateinit var fakeOldShoppingList: MutableList<Product>

    override suspend fun loadAllProductsInCurrentShoppingList(): Response<List<Product>>? {
        return try {

            // TODO: Remove placeholder data with network call to backend
            fakeCurrentShoppingList = ShoppingListPlaceholderContent.VARIOUS_ITEMS_CURRENT_SHOP

            Response.Success(fakeCurrentShoppingList)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }

    override suspend fun loadAllProductsInOldShoppingList(): Response<List<Product>>? {
        return try {

            // TODO: Remove placeholder data with network call to backend
            fakeOldShoppingList = ShoppingListPlaceholderContent.VARIOUS_ITEMS_OLD_SHOP

            Response.Success(fakeCurrentShoppingList)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }

    override suspend fun updateProductInCurrentShoppingList(shoppingList: List<Product>?) {
        // TODO: Pass to database
        fakeCurrentShoppingList = shoppingList as MutableList<Product>
    }

    override suspend fun updateProductInOldShoppingList(shoppingList: List<Product>?) {
        // TODO: Pass to database
        fakeOldShoppingList = shoppingList as MutableList<Product>
    }

    override suspend fun loadSuggestedProductsForShoppingList(): Response<List<Product>>? {
        return try {

            // TODO: Remove placeholder data with network call to backend
            fakeOldShoppingList = ShoppingListPlaceholderContent.VARIOUS_ITEMS_OLD_SHOP

            Response.Success(fakeCurrentShoppingList)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }
}