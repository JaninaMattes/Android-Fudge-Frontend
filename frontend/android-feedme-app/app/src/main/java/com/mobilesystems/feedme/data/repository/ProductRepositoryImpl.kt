package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.ProductDataSourceImpl
import com.mobilesystems.feedme.domain.model.Product
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val dataSource: ProductDataSourceImpl) : ProductRepository {

    // in-memory cache of the loggedInUser object
    var product: MutableLiveData<Product?> = MutableLiveData<Product?>()
        private set

    override suspend fun getProductFromBarcodeScan(scanResult: String): MutableLiveData<Product?> {
        // get product from barcode scan
        val result = dataSource.getProductFromBarcodeScanResult(scanResult)

        if (result is Response.Success) {
            product.postValue(result.data)
        }
        return product
    }

    override suspend fun getProduct(productId: Int): MutableLiveData<Product?> {

        val result = dataSource.getProductById(productId)

        if (result is Response.Success) {
            product.postValue(result.data)
        }
        return product
    }

    override suspend fun updateProduct(product: Product) {
        dataSource.updateProduct(product)
    }

    override suspend fun deleteProduct(productId: Int) {
        dataSource.deleteProduct(productId)
    }
}