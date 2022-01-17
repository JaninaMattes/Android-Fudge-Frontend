package com.mobilesystems.feedme.domain.model

abstract class ShoppingList(
    open val shoppingListId: Int,
    open val shoppingListProducts: List<Product>? = null
)
