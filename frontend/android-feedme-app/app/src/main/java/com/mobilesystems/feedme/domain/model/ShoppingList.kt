package com.mobilesystems.feedme.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShoppingList(
    val shoppingListId: Int,
    val lastUsedProducts: List<Product>? = null,
    val currentShoppingList: List<Product>? = null
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShoppingList

        if (shoppingListId != other.shoppingListId) return false
        if (lastUsedProducts != other.lastUsedProducts) return false
        if (currentShoppingList != other.currentShoppingList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = shoppingListId
        result = 31 * result + (lastUsedProducts?.hashCode() ?: 0)
        result = 31 * result + (currentShoppingList?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ShoppingList(shoppingListId=$shoppingListId, " +
                "lastUsedProducts=$lastUsedProducts, currentShoppingList=$currentShoppingList)"
    }


}
