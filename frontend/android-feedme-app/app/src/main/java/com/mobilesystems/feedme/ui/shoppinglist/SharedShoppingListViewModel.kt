package com.mobilesystems.feedme.ui.shoppinglist

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.ui.common.utils.getLoggedInUser
import com.mobilesystems.feedme.data.repository.ShoppingListRepositoryImpl
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedShoppingListViewModel @Inject constructor(
    private val androidApplication: Application,
    private val shoppingListRepository: ShoppingListRepositoryImpl) :
    BaseViewModel(androidApplication), BaseShoppingListViewModel {

    // Store view data in MutableLiveData
    private var _newproduct = MutableLiveData<Product>()
    private var _currentShoppingList = MutableLiveData<List<Product>?>()
    private var _oldShoppingList = MutableLiveData<List<Product>?>()
    private var _selectedProduct = MutableLiveData<Product?>()
    private var _currentUser = MutableLiveData<Int?>()

    // Getter and setter
    val currentShoppingList : LiveData<List<Product>?>
        get() = _currentShoppingList

    val oldShoppingList : LiveData<List<Product>?>
        get() = _oldShoppingList

    val newProduct : LiveData<Product?>
        get() = _newproduct

    val selectedProduct : LiveData<Product?>
        get() = _selectedProduct

    val currentUser : LiveData<Int?>
        get() = _currentUser

    init {

        val context = getApplication<Application>().applicationContext
        getCurrentUser(context)

        if (currentShophasNoValues()) {
            // preload all values
            loadAllCurrentShoppingListProducts()
        }

        if (oldShophasNoValues()) {
            // preload all shoppinglist values
            loadAllOldShoppingListProducts()
        }

    }

    fun refresh(userId: Int) {
        // Make a call to the server after some delay for better user experience.
        loadAllCurrentShoppingListProducts()
        loadAllOldShoppingListProducts()
    }

    override fun loadAllCurrentShoppingListProducts() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            if (userId != null) {
                shoppingListRepository.loadCurrentShoppingListProducts(userId)
                _currentShoppingList = shoppingListRepository.currentShoppingListProducts
            }
        }
    }

    override fun loadAllOldShoppingListProducts() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            if (userId != null) {
                shoppingListRepository.loadOldShoppingListProducts(userId)
                _oldShoppingList = shoppingListRepository.oldShoppingListProducts
            }
        }
    }

    override fun saveCurrentState() {
        viewModelScope.launch {
            val userId = currentUser.value
            if (userId != null) {
                shoppingListRepository.updateCurrentShoppingList(userId, currentShoppingList.value)
                shoppingListRepository.updateOldShoppingList(userId, oldShoppingList.value)
            }
        }
    }

    override fun setProduct(product: Product) {
        // Set selected product from list
        _selectedProduct.value = product
    }

    override fun setNewProduct(product: Product){
        // Set newly created product
        _newproduct.value = product
    }

    override fun updateCurrentShoppingList(shoppingList: List<Product>?) {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            if (userId != null) {
                shoppingListRepository.updateCurrentShoppingList(userId, shoppingList)
            }
        }
    }

    override fun addProductToCurrentShoppingList(product: Product) {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            if (userId != null) {
                val currentItems = currentShoppingList.value
                var tempList: MutableList<Product> = ArrayList<Product>()
                // Check if currentlist is empty
                if (currentItems != null) {
                    tempList = currentItems as MutableList<Product>
                    // find duplicate items
                    val duplicateValue = tempList.filter { p -> p.productName == product.productName }
                    // duplicate values
                    if (duplicateValue.isNotEmpty()) {
                        for (i in duplicateValue.indices) {
                            if (duplicateValue[i].productName == product.productName) {
                                val newProduct = calculateNewProductAmount(
                                    duplicateValue[i],
                                    product,
                                    currentShoppingList = true
                                )
                                tempList =
                                    tempList.replace(
                                        duplicateValue[i],
                                        newProduct
                                    ) as MutableList<Product>// Replace with new product
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
                _currentShoppingList.value = tempList

                // Network call
                shoppingListRepository.updateCurrentShoppingList(userId, currentShoppingList.value)
            }
        }
    }

    override fun removeProductFromCurrentShoppingList(product: Product) {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            if (userId != null) {
                val currentValues = currentShoppingList.value
                var tempList: MutableList<Product> = ArrayList<Product>()
                if (currentValues != null) {
                    tempList = currentValues as MutableList<Product>
                    tempList.remove(product)
                }
                _currentShoppingList.value = tempList
                shoppingListRepository.updateCurrentShoppingList(userId, currentShoppingList.value)
            }
        }
    }

    override fun addProductToOldShoppingList(product: Product) {
        val currentItems = oldShoppingList.value
        var tempList: MutableList<Product> = ArrayList<Product>()
        // Remove product amount
        //val newProduct = createProductForOldShoppingList(product)
        // Check if currentlist is empty
        if (currentItems != null) {
            tempList = currentItems as MutableList<Product>
            // find duplicate items
            val duplicateValue = tempList.filter { p -> p.productName == product.productName }
            // duplicate values
            if (duplicateValue.isNotEmpty()) {
                for (i in duplicateValue.indices) {
                    if (duplicateValue[i].productName == product.productName) {
                        val newProduct = calculateNewProductAmount(
                            duplicateValue[i],
                            product,
                            currentShoppingList = false
                        )
                        tempList = tempList.replace(
                            duplicateValue[i],
                            newProduct
                        ) as MutableList<Product>// Replace with new product
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
        // Network call
        _oldShoppingList.value = tempList
        updateOldShoppingList(oldShoppingList.value)
    }

    override fun updateOldShoppingList(shoppingList: List<Product>?) {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            if (userId != null) {
                shoppingListRepository.updateOldShoppingList(userId, shoppingList)
            }
        }
        // Update all
        loadAllOldShoppingListProducts()
    }

    override fun removeProductFromOldShoppingList(product: Product) {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            if (userId != null) {
                val currentValues = oldShoppingList.value
                var tempList: MutableList<Product> = ArrayList<Product>()
                if (currentValues != null) {
                    tempList = currentValues as MutableList<Product>
                    tempList.remove(product)
                }
                _oldShoppingList.value = tempList
                shoppingListRepository.removeProductFromOldShoppingList(userId, product)
            }
        }
        // Update all
        loadAllOldShoppingListProducts()
    }

    private fun currentShophasNoValues(): Boolean{
        return _currentShoppingList.value.isNullOrEmpty()
    }

    private fun oldShophasNoValues(): Boolean{
        return _oldShoppingList.value.isNullOrEmpty()
    }

    private fun calculateNewProductAmount(product_one: Product, product_two: Product, currentShoppingList: Boolean): Product{
        val newProduct: Product
        var amountType = product_one.quantity.filter { it.isLetter() }
        if (amountType.isEmpty()) {
            amountType = "Stück"
        }

        if(currentShoppingList) {
            val amountOne = product_one.quantity.filter { it.isDigit() }
            val amountTwo = product_two.quantity.filter { it.isDigit() }
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
        } else {
            newProduct = product_one.copy(
                productId = product_one.productId,
                productName = product_one.productName,
                expirationDate = "-",
                labels = product_one.labels,
                quantity = "1 $amountType",
                manufacturer = "-",
                nutritionValue = product_one.nutritionValue,
                imageUrl = product_one.imageUrl
            )
        }
        return newProduct
    }

    private fun createProductForOldShoppingList(product: Product): Product{
        var newProduct: Product
        var amountType = product.quantity.filter { it.isLetter() }
        if (amountType.isEmpty()) {
            amountType = "Stück"
        }
        newProduct = product.copy(
            productId = product.productId,
            productName = product.productName,
            expirationDate = "-",
            labels = product.labels,
            quantity = "0 $amountType",
            manufacturer = "-",
            nutritionValue = product.nutritionValue,
            imageUrl = product.imageUrl
        )
        return newProduct
    }

    private fun <Product> List<Product>.replace(old: Product, new: Product) = map { if (it == old) new else it }

    private fun getCurrentUser(context: Context): LiveData<Int?>{
        val result = getLoggedInUser(context)
        _currentUser.value = result
        return  currentUser
    }
}
