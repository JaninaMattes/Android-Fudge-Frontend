package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.InventoryDataSourceImpl
import com.mobilesystems.feedme.data.datasource.ProductDataSourceImpl
import com.mobilesystems.feedme.domain.model.Product
import javax.inject.Inject

/**
 * https://developer.android.com/kotlin/flow
 * https://developer.android.com/topic/libraries/architecture/livedata
 */
class InventoryRepositoryImpl @Inject constructor(
    private val productDataSource: ProductDataSourceImpl,
    private val inventoryDataSource: InventoryDataSourceImpl) : InventoryRepository{

    // in-memory cache of the fetched objects
    var barcodeScanProduct: MutableLiveData<Product?> = MutableLiveData<Product?>()
    var inventoryList: MutableLiveData<List<Product>?> = MutableLiveData<List<Product>?>()
        private set

    // TODO: Put together and use ProductDataSource instead
    override suspend fun loadInventoryListProducts(userId: Int): MutableLiveData<List<Product>?> {
        // get all products for current user
        val result = inventoryDataSource.getAllProductsInInventoryList(userId)

        if (result is Response.Success) {
            inventoryList.postValue(result.data)
        }
        return inventoryList
    }

    override suspend fun removeProductFromInventory(userId: Int, product: Product) {
        inventoryDataSource.removeProductFromInventoryList(userId, product)
    }

    override suspend fun updateProductOnInventory(userId: Int, product: Product) {
        // get all products for current user
        productDataSource.updateProduct(product)
        loadInventoryListProducts(userId)
    }

    override suspend fun updateProductInventoryList(userId: Int, inventoryList: List<Product>) {
        inventoryDataSource.updateProductInventoryList(userId, inventoryList)
    }

    override suspend fun getBarcodeScanResult(
        result: String?
    ): MutableLiveData<Product?> {
        // get all products for current user
        val result = productDataSource.getProductFromBarcodeScanResult(result)

        if (result is Response.Success) {
            barcodeScanProduct.postValue(result.data)
        }
        return barcodeScanProduct
    }
}