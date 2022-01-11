package com.mobilesystems.feedme.ui.recipes

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.ui.common.utils.getLoggedInUser
import com.mobilesystems.feedme.data.repository.InventoryRepositoryImpl
import com.mobilesystems.feedme.data.repository.RecipeRepositoryImpl
import com.mobilesystems.feedme.data.repository.ShoppingListRepositoryImpl
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.domain.model.Recipe
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Tutorial Context in MVVM: https://stackoverflow.com/questions/51451819/how-to-get-context-in-android-mvvm-viewmodel/51452435
 */
@HiltViewModel
class SharedRecipesViewModel @Inject constructor(
    androidApplication: Application,
    private val recipeRepository: RecipeRepositoryImpl,
    private val inventoryRepository: InventoryRepositoryImpl,
    private val shoppingListRepository: ShoppingListRepositoryImpl) :
    BaseViewModel(androidApplication), BaseRecipeViewModel  {

    private var _recipeList = MutableLiveData<List<Recipe>?>()
    private var _inventoryList = MutableLiveData<List<Product>?>()
    private var _shoppingList = MutableLiveData<List<Product>?>()
    private var _selectedRecipe = MutableLiveData<Recipe?>()
    private var _selectedRecipeIngredients = MutableLiveData<List<Product>?>()
    private var _availableIngredients = MutableLiveData<List<Product>?>()
    private var _notAvailableIngredients = MutableLiveData<List<Product>?>()
    private var _currentUser = MutableLiveData<Int?>()

    val recipeList : LiveData<List<Recipe>?>
        get() = _recipeList

    val inventoryList : LiveData<List<Product>?>
        get() = _inventoryList

    val selectedRecipe : LiveData<Recipe?>
        get() = _selectedRecipe

    val shoppingList : MutableLiveData<List<Product>?>
        get() = _shoppingList

    val selectedRecipeIngredients : LiveData<List<Product>?>
        get() = _selectedRecipeIngredients

    val availableIngredients : LiveData<List<Product>?>
        get() = _availableIngredients

    val notAvailableIngredients : LiveData<List<Product>?>
        get() = _notAvailableIngredients

    val currentUser : LiveData<Int?>
        get() = _currentUser

    init {

        val context = getApplication<Application>().applicationContext
        getCurrentUser(context)

        if(recipeListHasNoValues()) {
            // preload all values
            loadMatchingRecipes()
        }

        if(inventoryListHasNoValues()){
            // preload all values
            loadInventoryList()
        }

        if(shoppingListHasNoValues()){
            // preload all values
            loadShoppingList()
        }

    }

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
            val userId = currentUser.value
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
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            val currentValues = shoppingList.value
            val currentNotValues = _notAvailableIngredients.value
            var tempList: MutableList<Product>? = ArrayList<Product>()
            tempList = currentValues as MutableList<Product>
            if (currentNotValues != null) {
                tempList.addAll(currentNotValues)
            }
            if(userId != null) {
                shoppingListRepository.updateCurrentShoppingList(userId, tempList)
            }
        }
    }

    override fun loadShoppingList() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            if(userId != null) {
                shoppingListRepository.loadCurrentShoppingListProducts(userId)
                _shoppingList = shoppingListRepository.currentShoppingListProducts
            }
        }
    }

    override fun loadInventoryList() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
            if(userId != null) {
                inventoryRepository.loadInventoryListProducts(userId)
                _inventoryList = inventoryRepository.inventoryList
            }
        }
    }

    override fun loadMatchingRecipes() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUser.value
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
        var ingList = selectedRecipeIngredients.value

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

    private fun getCurrentUser(context: Context): LiveData<Int?>{
        val result = getLoggedInUser(context)
        _currentUser.value = result
        return  currentUser
    }
}