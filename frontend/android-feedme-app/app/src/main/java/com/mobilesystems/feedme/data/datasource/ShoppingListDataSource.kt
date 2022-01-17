package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.domain.model.Product

interface ShoppingListDataSource{

    suspend fun loadAllProductsInCurrentShoppingList(): Response<List<Product>>?

    suspend fun loadAllProductsInOldShoppingList(): Response<List<Product>>?

    suspend fun addProductToCurrentShoppingList(userId: Int, product: Product)

    suspend fun addProductToOldShoppingList(userId: Int, product: Product)

    suspend fun updateCurrentShoppingList(userId: Int, shoppingList: List<Product>?)

    suspend fun updateOldShoppingList(userId: Int, shoppingList: List<Product>?)

    suspend fun removeProductFromOldShoppingList(userId: Int, product: Product)

    suspend fun removeProductFromCurrentShoppingList(userId: Int, product: Product)

    suspend fun loadSuggestedProductsForShoppingList(): Response<List<Product>>? // Future feature

}