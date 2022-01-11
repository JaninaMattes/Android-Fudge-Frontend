package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.ShoppingListDataSourceImpl
import com.mobilesystems.feedme.domain.model.Product
import javax.inject.Inject


class ShoppingListRepositoryImpl @Inject constructor(private val dataSourceImpl: ShoppingListDataSourceImpl)
    : ShoppingListRepository {

    // in-memory cache of the fetched objects
    var currentShoppingListProducts: MutableLiveData<List<Product>?> = MutableLiveData<List<Product>?>()
        private set
    var oldShoppingListProducts: MutableLiveData<List<Product>?> = MutableLiveData<List<Product>?>()
        private set
    var suggestedShoppingListProducts: MutableLiveData<List<Product>?> = MutableLiveData<List<Product>?>()
        private set

    override suspend fun suggestProductsForShoppingList(userId: Int): MutableLiveData<List<Product>?> {
        val result = dataSourceImpl.loadSuggestedProductsForShoppingList()

        if (result is Response.Success) {
            suggestedShoppingListProducts.postValue(result.data)
        }
        return suggestedShoppingListProducts
    }

    override suspend fun loadOldShoppingListProducts(userId: Int): MutableLiveData<List<Product>?> {
        // get current shoppinglist for current user
        val result = dataSourceImpl.loadAllProductsInOldShoppingList()

        if (result is Response.Success) {
            oldShoppingListProducts.postValue(result.data)
        }
        return oldShoppingListProducts
    }

    override suspend fun loadCurrentShoppingListProducts(userId: Int): MutableLiveData<List<Product>?> {
        // get current shoppinglist for current user
        val result = dataSourceImpl.loadAllProductsInCurrentShoppingList()

        if (result is Response.Success) {
            currentShoppingListProducts.postValue(result.data)
        }
        return currentShoppingListProducts
    }

    override suspend fun updateCurrentShoppingList(userId: Int, shoppingList: List<Product>?) {
        currentShoppingListProducts.postValue(shoppingList)
        dataSourceImpl.updateProductInCurrentShoppingList(shoppingList)
    }

    override suspend fun removeProductFromCurrentShoppingList(userId: Int, product: Product) {
        val currentItems = currentShoppingListProducts.value
        var tempList = mutableListOf<Product>()
        if(currentItems != null){
            tempList = currentItems as MutableList<Product>
            tempList.remove(product)
            currentShoppingListProducts.value = tempList
        }
        dataSourceImpl.updateProductInCurrentShoppingList(currentShoppingListProducts.value)
    }

    override suspend fun addNewProductToCurrentShoppingList(userId: Int, product: Product) {
        val currentItems = currentShoppingListProducts.value
        var tempList = mutableListOf<Product>()
        if(currentItems != null){
            tempList = currentItems as MutableList<Product>
            tempList.add(product)
            currentShoppingListProducts.value = tempList
        }
        dataSourceImpl.updateProductInCurrentShoppingList(currentShoppingListProducts.value)
    }

    override suspend fun updateOldShoppingList(userId: Int, oldShoppingList: List<Product>?) {
        oldShoppingListProducts.postValue(oldShoppingList)
        dataSourceImpl.updateProductInOldShoppingList(oldShoppingList)
    }

    override suspend fun removeProductFromOldShoppingList(userId: Int, product: Product) {
        val currentItems = oldShoppingListProducts.value
        var tempList = mutableListOf<Product>()
        if(currentItems != null){
            tempList = currentItems as MutableList<Product>
            tempList.remove(product)
            oldShoppingListProducts.value = tempList
        }
        dataSourceImpl.updateProductInOldShoppingList(oldShoppingListProducts.value)
    }

    override suspend fun addProductToOldShoppingList(userId: Int, product: Product) {
        val currentItems = oldShoppingListProducts.value
        var tempList: MutableList<Product>
        if(currentItems != null){
            tempList = currentItems as MutableList<Product>
            tempList.add(product)
            oldShoppingListProducts.value = tempList
        }
        dataSourceImpl.updateProductInOldShoppingList(oldShoppingListProducts.value)
    }

}