package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.domain.model.Product

interface ShoppingListRepository {

    suspend fun suggestProductsForShoppingList(userId: Int): MutableLiveData<List<Product>?>

    suspend fun loadOldShoppingListProducts(userId: Int): MutableLiveData<List<Product>?>

    suspend fun loadCurrentShoppingListProducts(userId: Int): MutableLiveData<List<Product>?>

    suspend fun updateCurrentShoppingList(userId: Int, currentShoppingList: List<Product>?)

    suspend fun removeProductFromCurrentShoppingList(userId: Int, product: Product)

    suspend fun addNewProductToCurrentShoppingList(userId: Int, product: Product) // new created product

    suspend fun updateOldShoppingList(userId: Int, oldShoppingList: List<Product>?)

    suspend fun removeProductFromOldShoppingList(userId: Int, product: Product)

    suspend fun addProductToOldShoppingList(userId: Int, product: Product) // new created product
}