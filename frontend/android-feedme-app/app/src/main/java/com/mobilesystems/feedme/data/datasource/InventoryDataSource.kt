package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.domain.model.Product

interface InventoryDataSource {

    suspend fun getAllProductsInInventoryList(userId: Int): Response<List<Product>>?

    suspend fun updateProductInventoryList(userId: Int, inventoryList: List<Product>?)

    suspend fun removeProductFromInventoryList(userId: Int, product: Product)

    suspend fun updateCurrentShoppingList(userId: Int, currentShoppingList: List<Product>)
}