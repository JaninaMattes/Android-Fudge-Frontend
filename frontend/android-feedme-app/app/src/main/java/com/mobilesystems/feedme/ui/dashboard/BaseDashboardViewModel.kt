package com.mobilesystems.feedme.ui.dashboard

import androidx.lifecycle.LiveData
import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.domain.model.Recipe


interface BaseDashboardViewModel {

    //dashboard
    fun loadExpiringProducts()

    fun loadNumberOneRecipes()

    fun loadLoggedInUser()

    //recipes
    fun selectedRecipe(recipe: Recipe)

    fun updateRecipe(recipe: Recipe) // Rating of recipe

    fun addRecipeToFavorites(recipeId: Int) // For future

    fun loadInventoryList()

    fun loadMatchingRecipes()

    fun loadShoppingList()

    fun exportUnavailableIngredientsToShoppingList()

    fun removeRecipeByPosition(position: Int) // Needs discussion - Can be removed

    fun loadAllRecipeIngredients(): LiveData<List<Product>?> // Helper function

    fun loadAllUnAvailableIngredients(): LiveData<List<Product>?> // Helper function

    fun loadAllAvailableIngredients(): LiveData<List<Product>?> // Helper function

    //inventory list
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