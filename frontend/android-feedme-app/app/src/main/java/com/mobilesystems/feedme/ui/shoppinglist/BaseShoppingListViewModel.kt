package com.mobilesystems.feedme.ui.shoppinglist

import com.mobilesystems.feedme.domain.model.Product

interface BaseShoppingListViewModel {

    fun setProduct(product: Product) // Helper function

    fun setNewProduct(product: Product) // Helper function

    fun updateCurrentShoppingList(shoppingList: List<Product>?)

    fun addProductToCurrentShoppingList(product: Product)

    fun removeProductFromCurrentShoppingList(product: Product)

    fun addProductToOldShoppingList(product: Product)

    fun updateOldShoppingList(shoppingList: List<Product>?)

    fun removeProductFromOldShoppingList(product: Product)

    fun loadAllCurrentShoppingListProducts()

    fun loadAllOldShoppingListProducts()

    fun saveCurrentState()

}