package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.domain.model.Product

interface InventoryRepository {

    suspend fun loadInventoryListProducts(userId: Int): MutableLiveData<List<Product>?>

    suspend fun removeProductFromInventory(userId: Int, product: Product): Unit

    suspend fun updateProductOnInventory(userId: Int, product: Product): Unit

    suspend fun updateProductInventoryList(userId: Int, mutableList: List<Product>): Unit

    suspend fun getBarcodeScanResult(result: String?): MutableLiveData<Product?>
}