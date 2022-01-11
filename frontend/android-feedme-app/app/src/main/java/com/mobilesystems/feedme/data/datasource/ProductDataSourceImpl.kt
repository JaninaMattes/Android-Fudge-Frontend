package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.remote.FoodTrackerApi
import com.mobilesystems.feedme.di.AppModule
import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ProductDataSourceImpl @Inject constructor(
    private val foodTrackerApi: FoodTrackerApi,
    @AppModule.DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : ProductDataSource{

    override suspend fun getProductById(productId: Int): Response<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(product: Product) {
        // TODO: Update product via network call
    }

    override suspend fun deleteProduct(productId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getProductFromBarcodeScanResult(result: String?): Response<Product> {
        return try {

            // TODO Implement with real network call
            val labels : MutableList<Label> = arrayListOf()
            val fakeProduct = Product(0, "Testprodukt", "15.01.2022",
                labels, "", "Nestle", "1024kcal",
                "https://cdn.pixabay.com/photo/2020/06/23/09/06/donut-5331966_960_720.jpg")
            Response.Success(fakeProduct)
        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }
}