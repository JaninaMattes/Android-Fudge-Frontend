package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.domain.model.Product

interface ProductRepository {

    suspend fun getProductFromBarcodeScan(scanResult: String): MutableLiveData<Product?>

    suspend fun getProduct(productId: Int): MutableLiveData<Product?>

    suspend fun updateProduct(product: Product): Unit

    suspend fun deleteProduct(productId: Int): Unit
}