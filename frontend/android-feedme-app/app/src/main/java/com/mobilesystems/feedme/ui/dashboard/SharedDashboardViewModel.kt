package com.mobilesystems.feedme.ui.dashboard

import android.app.Application
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.ui.common.utils.getTimeDiff
import com.mobilesystems.feedme.data.repository.DashboardRepositoryImpl
import com.mobilesystems.feedme.data.repository.InventoryRepositoryImpl
import com.mobilesystems.feedme.data.repository.RecipeRepositoryImpl
import com.mobilesystems.feedme.data.repository.ShoppingListRepositoryImpl
import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.domain.model.Recipe
import com.mobilesystems.feedme.domain.model.User
import com.mobilesystems.feedme.ui.authentication.AuthResult
import com.mobilesystems.feedme.ui.common.utils.getLoggedInUser
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class SharedDashboardViewModel @Inject constructor(
    androidApplication : Application,
    private val dashboardRepository: DashboardRepositoryImpl,
    private val recipeRepository: RecipeRepositoryImpl,
    private val inventoryRepository: InventoryRepositoryImpl,
    private val shoppingListRepository: ShoppingListRepositoryImpl) :
    BaseViewModel(androidApplication), BaseDashboardViewModel{

    //dashboard
    private var _expProductsList = MutableLiveData<List<Product>?>()
    private var _noOneRecipesList = MutableLiveData<List<Recipe>?>()
    private var _selectedProduct = MutableLiveData<Product?>()
    private var _loggedInUser = MutableLiveData<User?>()
    private var _currentUserId = MutableLiveData<Int?>()

    //recipes
    private var _recipeList = MutableLiveData<List<Recipe>?>()
    private var _inventoryList = MutableLiveData<List<Product>?>()
    private var _shoppingList = MutableLiveData<List<Product>?>()
    private var _selectedRecipe = MutableLiveData<Recipe?>()
    private var _selectedRecipeIngredients = MutableLiveData<List<Product>?>()
    private var _availableIngredients = MutableLiveData<List<Product>?>()
    private var _notAvailableIngredients = MutableLiveData<List<Product>?>()

    //inventory list
    private var _barcodeScanProduct = MutableLiveData<Product?>()
    private var _selectedTagList = MutableLiveData<List<Label>?>()
    private var _allProductLabels = MutableLiveData<List<String>>()

    //dashboard
    val expProductList : LiveData<List<Product>?>
        get() = _expProductsList

    val noOneRecipesList : LiveData<List<Recipe>?>
        get() = _noOneRecipesList

    val selectedProduct : LiveData<Product?>
        get() = _selectedProduct

    val loggedInUser : LiveData<User?>
        get() = _loggedInUser

    val currentUserId : LiveData<Int?>
        get() = this._currentUserId

    //recipes
    val recipeList : LiveData<List<Recipe>?>
        get() = _recipeList

    val inventoryList : LiveData<List<Product>?>
        get() = _inventoryList

    val selectedRecipe : LiveData<Recipe?>
        get() = _selectedRecipe

    val shoppingList : LiveData<List<Product>?>
        get() = _shoppingList

    val selectedRecipeIngredients : LiveData<List<Product>?>
        get() = _selectedRecipeIngredients

    val availableIngredients : LiveData<List<Product>?>
        get() = _availableIngredients

    val notAvailableIngredients : LiveData<List<Product>?>
        get() = _notAvailableIngredients

    //inventory list
    val barcodeScanProduct : LiveData<Product?>
        get() = _barcodeScanProduct

    val selectedProductTagList : LiveData<List<Label>?>
        get() = _selectedTagList

    val allProductLabels : LiveData<List<String>>
        get() = _allProductLabels

    init {

        // Get user id
        val context = getApplication<Application>().applicationContext
        getLoggedInUserId(context)

        //dashboard
        // Preload user
        loadLoggedInUser()

        // dashboard
        if (productsHasNoValues()) {
            // preload all values
            loadExpiringProducts()
        }
        if (recipesHasNoValues()) {
            // preload all values
            loadNumberOneRecipes()
        }

        //recipes
        if(recipeListHasNoValues()) {
            // preload all values
            loadMatchingRecipes()
        }

        // inventory
        if(inventoryListHasNoValues()){
            // preload all values
            loadInventoryList()
        }

        // shoppinglist
        if(shoppingListHasNoValues()){
            // preload all values
            loadShoppingList()
        }

        //inventory list
        if (inventoryHasNoValues()) {
            // preload all values
            loadAllProductsOfInventoryList()

            filterListByExpirationDate()
        }
    }

    //dashboard

    override fun loadExpiringProducts() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = this@SharedDashboardViewModel.currentUserId.value
            if(userId != null) {
                dashboardRepository.getAllExpiringProducts(userId)
                _expProductsList = dashboardRepository.expiringProductList

                // filter only for next exp products
                filterNextExpiringProducts(_expProductsList.value) // TODO: Check why always null
            }
        }
    }

    override fun loadNumberOneRecipes() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUserId.value
            if(userId != null) {
                dashboardRepository.getNumberOneRecipes(userId)
                _noOneRecipesList = dashboardRepository.noOneRecipeList
            }
        }
    }

    override fun loadLoggedInUser() {
        viewModelScope.launch {
            val userId = currentUserId.value
            if(userId != null) {
                try {
                    dashboardRepository.getCurrentLoggedInUser(userId)
                    _loggedInUser = dashboardRepository.loggedInUser
                }catch (error: Throwable){
                    // Notify view login attempt failed
                    Log.e("Dashboard", "error during login $error")
                }
            }
        }
    }

    private fun productsHasNoValues(): Boolean{
        return _expProductsList.value.isNullOrEmpty()
    }

    private fun recipesHasNoValues(): Boolean{
        return _noOneRecipesList.value.isNullOrEmpty()
    }

    private fun filterNextExpiringProducts(products: List<Product>?): Unit {
        var tempList = mutableListOf<Product>()
        if(products != null){
            for(item in products){
                val expDays = getTimeDiff(item.expirationDate)
                if (expDays <= 3) {
                    tempList.add(item)
                }
            }
            _expProductsList.value = tempList
        }
    }

    private fun getLoggedInUserId(context: Context): LiveData<Int?>{
        val result = getLoggedInUser(context)
        _currentUserId.value = result?.userId
        if(result?.userId != null){
            // get loggedin user from shared preferences
                // tloaded from backend
            _loggedInUser.value = User(
                userId = result.userId,
                firstName = result.firstName,
                lastName = result.lastName,
                email = result.email,
                password = "-")
        }
        return  currentUserId
    }

    //recipes

    override fun selectedRecipe(recipe: Recipe){
        // selected recipe from list
        _selectedRecipe.value = recipe
        loadAllRecipeIngredients()
    }

    override fun updateRecipe(recipe: Recipe) {
        // Update if rating of recipe has changed
        viewModelScope.launch {
            _selectedRecipe.value = recipe
            recipeRepository.rateRecipe(recipe)
            addRecipeToFavorites(recipe.recipeId)
        }
    }

    override fun addRecipeToFavorites(recipeId: Int) {
        // For future features
        viewModelScope.launch {
            val userId = this@SharedDashboardViewModel.currentUserId.value
            if(userId != null) {
                recipeRepository.addRecipeToFavoriteList(userId, recipeId)
            }
        }
    }

    override fun loadAllRecipeIngredients(): LiveData<List<Product>?> {
        // Helper function
        _selectedRecipeIngredients.value = selectedRecipe.value?.ingredients
        // filter the ingredients which are currently unavailable
        filterAvailableAndUnavailableIngredients()
        return selectedRecipeIngredients
    }

    override fun loadAllUnAvailableIngredients(): LiveData<List<Product>?> {
        // Helper function
        return notAvailableIngredients
    }

    override fun loadAllAvailableIngredients(): LiveData<List<Product>?> {
        // Helper function
        return availableIngredients
    }

    override fun exportUnavailableIngredientsToShoppingList() {
        val userId = currentUserId.value
        val currentNotValues = _notAvailableIngredients.value
        val tempList = findDuplicateShoppingProducts(currentNotValues)
        _shoppingList.value = tempList
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            if (userId != null) {
                Log.d("Shoppinglistfehler", tempList.toString())
                shoppingListRepository.updateCurrentShoppingList(userId, tempList)
            }
        }
        //update all
        loadShoppingList()
    }

    private fun findDuplicateShoppingProducts(currentNotValues: List<Product>?): MutableList<Product>{
        val currentValues = shoppingList.value
        var tempList: MutableList<Product> 
        tempList = currentValues as MutableList<Product>
        // Check if ingredientlist is empty
        if(currentNotValues != null) {
            //check if current shoppinglist ist empty
            if (currentValues != null) {
                // for each ingredient find duplicate products
                currentNotValues?.forEach {
                    val duplicateValue = tempList.filter { p -> p.productName == it.productName }
                    //if ingredient is on shoppinglist, replace with newamount
                    if (duplicateValue.isNotEmpty()) {
                        for (i in duplicateValue.indices) {
                            if (duplicateValue[i].productName == it.productName) {
                                val newProduct = calculateNewAmount(
                                    duplicateValue[i],
                                    it
                                )
                                tempList = tempList.replace(
                                    duplicateValue[i],
                                    newProduct
                                ) as MutableList<Product>// Replace with new product
                            }
                        }
                    } //if ingredient is not on shoppinglist, add
                    else {
                        tempList.add(it)
                    }
                }
            } //if shoppinglist is empty add all ingredients
            else {
                tempList.addAll(currentNotValues)
            }
        }
        return tempList
    }

    override fun loadShoppingList() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = this@SharedDashboardViewModel.currentUserId.value
            if(userId != null) {
                shoppingListRepository.loadCurrentShoppingListProducts(userId)
                _shoppingList = shoppingListRepository.currentShoppingListProducts
            }
        }
    }

    override fun loadInventoryList() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = this@SharedDashboardViewModel.currentUserId.value
            if(userId != null) {
                inventoryRepository.loadInventoryListProducts(userId)
                _inventoryList = inventoryRepository.inventoryList
            }
        }
    }

    override fun loadMatchingRecipes() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = this@SharedDashboardViewModel.currentUserId.value
            if(userId != null) {
                recipeRepository.loadAllRecipesBasedOnInventory(userId)
                _recipeList = recipeRepository.recipeList
                filterListByRating()
            }
        }
    }

    override fun removeRecipeByPosition(position: Int) {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val currentValues = recipeList.value
            var tempList: MutableList<Recipe> = ArrayList<Recipe>()
            if (currentValues != null) {
                tempList = currentValues as MutableList<Recipe>
                tempList.removeAt(position)
            }
            _recipeList.value = tempList
            _recipeList = recipeRepository.recipeList
        }
    }

    private fun recipeListHasNoValues(): Boolean{
        return _recipeList.value.isNullOrEmpty()
    }

    private fun inventoryListHasNoValues(): Boolean{
        return _inventoryList.value.isNullOrEmpty()
    }

    private fun shoppingListHasNoValues(): Boolean{
        return _shoppingList.value.isNullOrEmpty()
    }

    private fun isProductAvailable(product: Product): Boolean {
        val tempList = inventoryList.value
        var available = false
        if(tempList != null){
            if(tempList.any { it.productName == product.productName }){
                available = true
            }
        }
        return available
    }

    private fun filterAvailableAndUnavailableIngredients(){
        // TODO: Discussion needed, Move this app logic to backend? Or to Use Cases?
        var tempListAvailable = arrayListOf<Product>()
        var tempListUnAvailable = arrayListOf<Product>()
        var ingList: List<Product>? = selectedRecipeIngredients.value

        ingList?.forEach {
            if (isProductAvailable(it)) {
                tempListAvailable.add(it)
            } else {
                tempListUnAvailable.add(it)
            }
            _availableIngredients.value = tempListAvailable
            _notAvailableIngredients.value = tempListUnAvailable
        }
    }

    private fun filterListByRating(): LiveData<List<Recipe>?>{
        var tempList = recipeList.value
        if(tempList != null) {
            _recipeList.value = tempList.sortedBy { it.rating }
        }
        return recipeList
    }

    //inventory list

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
            val userId = this@SharedDashboardViewModel.currentUserId.value
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
            val userId = this@SharedDashboardViewModel.currentUserId.value
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
            val userId = this@SharedDashboardViewModel.currentUserId.value
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
            val userId = this@SharedDashboardViewModel.currentUserId.value
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
            val userId = this@SharedDashboardViewModel.currentUserId.value
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
}