package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.domain.model.Product

interface ShoppingListDataSource{

    suspend fun loadAllProductsInCurrentShoppingList(): Response<List<Product>>?

    suspend fun loadAllProductsInOldShoppingList(): Response<List<Product>>?

    suspend fun updateProductInCurrentShoppingList(shoppingList: List<Product>?)

    suspend fun updateProductInOldShoppingList(shoppingList: List<Product>?)

    suspend fun loadSuggestedProductsForShoppingList(): Response<List<Product>>?

}