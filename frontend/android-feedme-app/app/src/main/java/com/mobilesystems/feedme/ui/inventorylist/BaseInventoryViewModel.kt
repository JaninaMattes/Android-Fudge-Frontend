package com.mobilesystems.feedme.ui.inventorylist

import androidx.lifecycle.LiveData
import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product

interface BaseInventoryViewModel {

    fun selectProduct(product: Product) // Select single product from inventory

    fun deleteProductByPosition(position: Int)

    fun getProductFromBarcodeScanResult(barcodeScanRes: String?)

    fun addProductFromBarcodeScanResultToInventory(product: Product)

    fun loadAllProductsOfInventoryList()

    fun loadSelectedProductTagList(): LiveData<List<Label>?> // helper function

    fun addProductToInventoryList(product: Product)

    fun updateProductInInventoryList(product: Product)

    fun deleteProductInInventoryList(product: Product)

    fun updateInventoryList()

}
