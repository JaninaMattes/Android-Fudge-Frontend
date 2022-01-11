package com.mobilesystems.feedme.data.datasource.placeholder

import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import java.util.*
import kotlin.collections.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * TODO: Replace all uses of this class before publishing your app.
 */
object InventoryListPlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val VARIOUS_ITEMS: MutableList<Product> = ArrayList()
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
        VARIOUS_ITEMS.add(item)
    }

    private fun createPlaceholderItem(productId: Int): Product {

        val labels : MutableList<Label> = arrayListOf()
        labels.add(Label.FISH)
        labels.add(Label.FROZEN_MEAL)
        labels.add(Label.VEGETABLE)

        return Product(productId, "Vegane Pizza Hawaii", "22.02.2022", labels,"1 Package", "Kelly's Pizzas",
            "1024 kcal", "https://www.eat-this.org/wp-content/uploads/2020/06/eat_this_die_perfekte_vegane_pizza-22-500x500.jpg")
    }

    private fun createPlaceholderItems() {

        val labels : MutableList<Label> = arrayListOf()
        labels.add(Label.FROZEN_MEAL)
        labels.add(Label.SUGGAR)
        labels.add(Label.BAKERY)

        addVariousItems(Product(0, "Vegane Pizza Hawaii", "08.01.2022", labels,"1 Packung", "Kelly's Pizzas",
            "240 kcal", "https://www.eat-this.org/wp-content/uploads/2020/06/eat_this_die_perfekte_vegane_pizza-22-500x500.jpg"))
        addVariousItems(Product(1, "Donuts", "07.01.2022", labels,"1 Packung", "Kelly's Pizzas",
            "1024 kcal", "https://cdn.pixabay.com/photo/2020/06/23/09/06/donut-5331966_960_720.jpg"))
        addVariousItems(Product(2, "Paprika", "15.01.2022", labels,"1 Stück", "Kelly's Pizzas",
            "504 kcal", "https://cdn.pixabay.com/photo/2016/12/21/08/55/pepper-1922458_960_720.jpg"))
        addVariousItems(Product(3, "Eier", "20.01.2022", labels,"6 Stück", "Eiermann",
            "1024 kcal", "https://cdn.pixabay.com/photo/2018/09/14/10/35/eggs-3676707_960_720.jpg"))
        addVariousItems(Product(4, "Lachsfilet", "22.02.2022", labels,"1 Packung", "Kelly's Pizzas",
            "601 kcal", "https://cdn.pixabay.com/photo/2014/11/05/15/57/salmon-518032_960_720.jpg"))
        addVariousItems(Product(5, "Vegane Pasta", "25.01.2022", labels,"1 Stück", "Kelly's Pizzas",
            "1024 kcal", "https://cdn.pixabay.com/photo/2016/11/23/18/31/pasta-1854245_960_720.jpg"))
        addVariousItems(Product(6, "Brokkoli", "14.01.2022", labels,"4 Stück", "Kelly's Pizzas",
            "333 kcal", "https://cdn.pixabay.com/photo/2016/03/05/19/02/broccoli-1238250_960_720.jpg"))
        addVariousItems(Product(7, "Vegane Pizza Siciliana", "22.02.2022", labels,"1 Packung", "Kelly's Pizzas",
            "240 kcal", "https://cdn.pixabay.com/photo/2017/09/30/15/10/plate-2802332_960_720.jpg"))

    }

}