package com.mobilesystems.feedme.data.datasource.placeholder

import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import java.util.HashMap

object ShoppingListPlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val VARIOUS_ITEMS_CURRENT_SHOP: MutableList<Product> = ArrayList()
    val VARIOUS_ITEMS_OLD_SHOP: MutableList<Product> = ArrayList()
    val ITEMS: MutableList<Product> = ArrayList()
    val ITEM_MAP: MutableMap<Int, Product> = HashMap()

    private val COUNT = 12

    init {
        // Add some samples
        createPlaceholderItems()
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createPlaceholderItem(i))
        }
    }

    private fun addItem(item: Product) {
        ITEMS.add(item)
        ITEM_MAP.put(item.productId, item)
    }

    private fun addVariousItems(item: Product) {
        VARIOUS_ITEMS_CURRENT_SHOP.add(item)
        VARIOUS_ITEMS_OLD_SHOP.add(item)
    }

    private fun createPlaceholderItem(productId: Int): Product {

        val labels : MutableList<Label> = arrayListOf()

        return Product(0, "Käse", "", labels,"1 Packung", "",
            "", "")
    }

    private fun createPlaceholderItems() {

        val labels : MutableList<Label> = arrayListOf()

        addVariousItems(
            Product(0, "Käse", "", labels,"1 Packung", "",
            "", "")
        )
        addVariousItems(
            Product(1, "Wurst", "", labels,"1 Packung", "",
            "", "")
        )
        addVariousItems(
            Product(2, "Brötchen", "", labels,"10 Stück", "",
                "", "")
        )
        addVariousItems(
            Product(3, "Milch", "", labels,"2 Packungen", "",
                "", "")
        )

    }

}