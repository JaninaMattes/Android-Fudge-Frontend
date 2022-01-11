package com.mobilesystems.feedme.ui.dashboard

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.ui.common.utils.getLoggedInUser
import com.mobilesystems.feedme.ui.common.utils.getTimeDiff
import com.mobilesystems.feedme.data.repository.DashboardRepositoryImpl
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.domain.model.Recipe
import com.mobilesystems.feedme.domain.model.User
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedDashboardViewModel @Inject constructor(
    private val androidApplication : Application,
    private val dashboardRepository: DashboardRepositoryImpl) :
    BaseViewModel(androidApplication), BaseDashboardViewModel{

    private var _expProductsList = MutableLiveData<List<Product>?>()
    private var _noOneRecipesList = MutableLiveData<List<Recipe>?>()
    private var _selectedRecipe = MutableLiveData<Recipe?>()
    private var _selectedProduct = MutableLiveData<Product?>()
    private var _loggedInUser = MutableLiveData<User?>()
    private var _currentUserId = MutableLiveData<Int?>()

    val expProductList : LiveData<List<Product>?>
        get() = _expProductsList

    val noOneRecipesList : LiveData<List<Recipe>?>
        get() = _noOneRecipesList

    val selectedRecipe : LiveData<Recipe?>
        get() = _selectedRecipe

    val selectedProduct : LiveData<Product?>
        get() = _selectedProduct

    val loggedInUser : LiveData<User?>
        get() = _loggedInUser

    val currentUserId : LiveData<Int?>
        get() = _currentUserId

    init {

        // Get user id
        val context = getApplication<Application>().applicationContext
        getCurrentUser(context)

        // Preload user
        loadLoggedInUser()

        if (productsHasNoValues()) {
            // preload all values
            loadExpiringProducts()
        }
        if (recipesHasNoValues()) {
            // preload all values
            loadNumberOneRecipes()
        }

    }

    override fun loadExpiringProducts() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val userId = currentUserId.value
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
                dashboardRepository.getCurrentLoggedInUser(userId)
                _loggedInUser = dashboardRepository.loggedInUser
            }
        }
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun selectRecipe(recipe: Recipe) {
        _selectedRecipe.value = recipe
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

    private fun getCurrentUser(context: Context): LiveData<Int?>{
        val result = getLoggedInUser(context)
        _currentUserId.value = result
        return  currentUserId
    }
}