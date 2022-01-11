package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.domain.model.Product

interface ProductDataSource {

    suspend fun getProductById(productId: Int): Response<Product>

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(productId: Int)

    suspend fun getProductFromBarcodeScanResult(result: String?): Response<Product>
}