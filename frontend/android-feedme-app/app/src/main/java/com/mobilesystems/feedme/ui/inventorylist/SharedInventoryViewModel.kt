package com.mobilesystems.feedme.ui.inventorylist

import android.app.Application
import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.data.repository.InventoryRepositoryImpl
import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.ui.authentication.LoggedInUser
import com.mobilesystems.feedme.ui.common.utils.getLoggedInUser
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * SharedViewModel to propagate shared Data between Fragments.
 *
 * https://developer.android.com/codelabs/basic-android-kotlin-training-shared-viewmodel#4
 */

@HiltViewModel
class SharedInventoryViewModel @Inject constructor(
    androidApplication : Application,
    private val inventoryRepository: InventoryRepositoryImpl) : BaseViewModel(androidApplication), BaseInventoryViewModel {

    private var _inventoryList = MutableLiveData<List<Product>?>()
    private var _barcodeScanProduct = MutableLiveData<Product?>()
    private var _selectedProduct = MutableLiveData<Product?>()
    private var _selectedTagList = MutableLiveData<List<Label>?>()
    private var _allProductLabels = MutableLiveData<List<String>>()
    private var _currentUser = MutableLiveData<LoggedInUser?>()

    val inventoryList : LiveData<List<Product>?>
        get() = _inventoryList

    val barcodeScanProduct : LiveData<Product?>
        get() = _barcodeScanProduct

    val selectedProduct : LiveData<Product?>
        get() = _selectedProduct

    val selectedProductTagList : LiveData<List<Label>?>
        get() = _selectedTagList

    val allProductLabels : LiveData<List<String>>
        get() = _allProductLabels

    val currentUser : LiveData<LoggedInUser?>
        get() = _currentUser

    init {

        val context = getApplication<Application>().applicationContext
        getCurrentUser(context)

        if (inventoryHasNoValues()) {
            // preload all values
            loadAllProductsOfInventoryList()

            filterListByExpirationDate()
        }
    }

    override fun selectProduct(product: Product){
        _selectedProduct.value = product
        // get tag list of the selected product
        loadSelectedProductTagList()
    }

    override fun deleteProductByPosition(position: Int) {
        val currentValues = inventoryList.value
        var tempList: MutableList<Product> = ArrayList<Product>()
        if (currentValues != null) {
            tempList = currentValues as MutableList<Product>
            tempList.removeAt(position)
        }
        _inventoryList.value = tempList
        _inventoryList = inventoryRepository.inventoryList

        updateInventoryList()
    }

    override fun getProductFromBarcodeScanResult(barcodeScanRes: String?) {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            inventoryRepository.getBarcodeScanResult(barcodeScanRes)
            _barcodeScanProduct = inventoryRepository.barcodeScanProduct
        }
    }

    override fun addProductFromBarcodeScanResultToInventory(product: Product) {
        // Add newly created product
        addProductToInventoryList(product)
    }

    override fun updateProductInInventoryList(product: Product) {
        // Update the values of a product that already exists in inventory
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value?.userId
            val currentValues = inventoryList.value
            var tempList: MutableList<Product> = ArrayList<Product>()
            if (currentValues != null) {
                tempList = currentValues as MutableList<Product>
                tempList.forEachIndexed { index, oldProduct ->
                    if (oldProduct.productId == product.productId) {
                        tempList[index] = product
                    }
                }
            }
            _inventoryList.value = tempList
            // update backend
            if(userId != null) {
                inventoryRepository.updateProductOnInventory(userId, product)
            }
        }
    }

    override fun addProductToInventoryList(product: Product) {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value?.userId
            val tempList = findDuplicateProducts(product)
            _inventoryList.value = tempList
            // Network call
            if(userId != null) {
                inventoryRepository.updateProductInventoryList(userId, tempList)
            }
        }
    }

    private fun findDuplicateProducts(product: Product): MutableList<Product> {
        val currentItems = inventoryList.value
        var tempList: MutableList<Product> = ArrayList<Product>()
        // Check if currentlist is empty
        if (currentItems != null) {
            tempList = currentItems as MutableList<Product>
            // find duplicate items
            val duplicateValue = tempList.filter { p -> p.productName == product.productName }
            // iterate over duplicate values (best case only 1)
            if (duplicateValue.isNotEmpty()) {
                for (i in duplicateValue.indices) {
                    if (duplicateValue[i].productName == product.productName) {
                        val newProduct = calculateNewAmount(duplicateValue[i], product)
                        tempList = tempList.replace(duplicateValue[i], newProduct) as MutableList<Product>// Replace with new product
                    }
                }
                // no duplicate values
            } else {
                tempList.add(product)
            }
            // empty list
        } else {
            tempList.add(product)
        }
        return tempList
    }

    private fun calculateNewAmount(product_one: Product, product_two: Product): Product{
        var newProduct: Product
        val amountOne = product_one.quantity.filter { it.isDigit() }
        val amountTwo = product_two.quantity.filter { it.isDigit() }
        var amountType = product_one.quantity.filter { it.isLetter() }
        if (amountType.isEmpty()){
            amountType = "Stück"
        }

        val newAmount = amountOne.toInt() + amountTwo.toInt()

        newProduct = product_one.copy(
            productId = product_one.productId,
            productName = product_one.productName,
            expirationDate = product_one.expirationDate,
            labels = product_one.labels,
            quantity = "$newAmount $amountType",
            manufacturer = product_one.manufacturer,
            nutritionValue = product_one.nutritionValue,
            imageUrl = product_one.imageUrl
        )

        return newProduct
    }

    override fun deleteProductInInventoryList(product: Product) {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value?.userId
            val currentValues = inventoryList.value
            var tempList: MutableList<Product> = ArrayList<Product>()
            if (currentValues != null) {
                tempList = currentValues as MutableList<Product>
                tempList.remove(product)
            }
            _inventoryList.value = tempList
            // Network call
            if(userId != null) {
                inventoryRepository.removeProductFromInventory(userId, product)
            }
        }
    }

    override fun loadAllProductsOfInventoryList() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value?.userId
            if(userId != null) {
                inventoryRepository.loadInventoryListProducts(userId)
                _inventoryList = inventoryRepository.inventoryList
            }
        }
    }

    override fun loadSelectedProductTagList(): LiveData<List<Label>?> {
        // Helper function to load all tags from a product
        val labels = selectedProduct.value?.labels
        _selectedTagList.value = labels
        return selectedProductTagList
    }

    override fun updateInventoryList() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value?.userId
            val currentValues = inventoryList.value
            var tempList: MutableList<Product> = ArrayList<Product>()
            if (currentValues != null) {
                tempList = currentValues as MutableList<Product>
            }
            if (userId != null) {
                inventoryRepository.updateProductInventoryList(userId, tempList)
            }
        }
    }

    // Only for the dropdown menu
    fun loadAllProductLabels(): LiveData<List<String>> {
        val labels = Label.values()
        val tempList: MutableList<String> = mutableListOf()
        labels.forEach {
            tempList.add(it.label)
        }
        _allProductLabels.value = tempList.sortedBy { it }
        return allProductLabels
    }

    private fun inventoryHasNoValues(): Boolean{
        return _inventoryList.value.isNullOrEmpty()
    }

    private fun isProductInInventory(product: Product): Boolean {
        inventoryList.value?.forEach { p ->
            if (p.productName == product.productName) {
                return true
            }
        }
        return false
    }

    private fun <Product> List<Product>.replace(old: Product, new: Product) = map { if (it == old) new else it }

    private fun filterListByExpirationDate(): LiveData<List<Product>?>{
        val currentValues = inventoryList.value
        var tempList: MutableList<Product> = ArrayList<Product>()
        if(currentValues != null) {
            tempList = currentValues as MutableList<Product>
            tempList.sortedBy { convertStringToDate(it.expirationDate) }
            _inventoryList.value = tempList
        }
        return inventoryList
    }

    private fun convertStringToDate(dateStr: String): Date {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        return sdf.parse(dateStr)
    }

    private fun getCurrentUser(context: Context): LiveData<LoggedInUser?>{
        val result = getLoggedInUser(context)
        _currentUser.value = result
        return  currentUser
    }
}