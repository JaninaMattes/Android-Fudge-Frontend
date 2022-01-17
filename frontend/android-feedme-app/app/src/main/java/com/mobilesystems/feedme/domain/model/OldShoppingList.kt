package com.mobilesystems.feedme.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OldShoppingList(
    override val shoppingListId: Int,
    override val shoppingListProducts: List<Product>? = null,
) : ShoppingList(shoppingListId, shoppingListProducts), Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OldShoppingList

        if (shoppingListId != other.shoppingListId) return false
        if (shoppingListProducts != other.shoppingListProducts) return false

        return true
    }

    override fun hashCode(): Int {
        var result = shoppingListId
        result = 31 * result + (shoppingListProducts?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "OldShoppingList(shoppingListId=$shoppingListId, shoppingListProducts=$shoppingListProducts)"
    }

}
