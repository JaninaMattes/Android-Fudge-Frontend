package com.mobilesystems.feedme.data.datasource.placeholder

import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.domain.model.Recipe
import java.util.ArrayList
import java.util.HashMap

object RecipePlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val VARIOUS_ITEMS: MutableList<Recipe> = ArrayList()
    val ITEMS: MutableList<Recipe> = ArrayList()
    val ITEM_MAP: MutableMap<Int, Recipe> = HashMap()

    private val COUNT = 12

    init {
        // Add some samples
        createPlaceholderItems()
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createPlaceholderItem(i))
        }
    }

    private fun addItem(item: Recipe) {
        ITEMS.add(item)
        ITEM_MAP.put(item.recipeId, item)
    }

    private fun addVariousItems(item: Recipe) {
        VARIOUS_ITEMS.add(item)
    }

    private fun createPlaceholderItem(recipeId: Int): Recipe {
        // Create placeholder recipe
        val egg_labels : MutableList<Label> = arrayListOf()
        egg_labels.add(Label.EGGS)
        val milk_labels : MutableList<Label> = arrayListOf()
        milk_labels.add(Label.DAIRY)
        val water_labels : MutableList<Label> = arrayListOf()
        water_labels.add(Label.DRINK)
        val salt_labels : MutableList<Label> = arrayListOf()
        salt_labels.add(Label.SPICES)
        val suggar_labels : MutableList<Label> = arrayListOf()
        suggar_labels.add(Label.SUGGAR)
        val flour_labels : MutableList<Label> = arrayListOf()
        flour_labels.add(Label.CEREAL_PRODUCT)

        val ingredients : MutableList<Product> = arrayListOf()
        ingredients.add(0, Product(0, "Eier", "", egg_labels,"3 Stück", "", "", ""))
        ingredients.add(1, Product(1, "Milch", "", milk_labels,"200 ml", "", "", ""))
        ingredients.add(2, Product(2, "Wasser", "", water_labels,"250 ml", "", "", ""))
        ingredients.add(3, Product(3, "Salz", "", salt_labels,"3 Teelöffel", "", "", ""))
        ingredients.add(4, Product(4, "Zucker", "", suggar_labels,"350 gr", "", "", ""))
        ingredients.add(5, Product(5, "Grießmehl", "", flour_labels,"500 gr", "", "", ""))

        return Recipe(recipeId, "Tasty Buddha Bowl", "Vegan",
            "1024 kcal", "Everything you need to make this back-to-school season the easiest and most delicious one yet — from " +
                    "grocery lists, to dinner guides, to simple recipes your whole family will love.",
            4.5F, "simple","35 min", "2 portions", "The best Buddha bowls start with a base of rice or whole grains " +
                    "which can be served cold or warm. Top with your favorite plant-based protein (tofu, seitan, edamame, beans), add fresh greens and fresh or " +
                    "roasted veggies. Next, drizzle with flavorful dressing or sauce and sprinkle with seeds or nuts. Top with fresh herbs and microgreens.", ingredients,
            "https://images.ichkoche.at/data/image/variations/496x384/12/buddha-bowl-mit-lachs-und-buchweizennudeln-rezept-img-116146.jpg")
    }

    private fun createPlaceholderItems() {
        val egg_labels : MutableList<Label> = arrayListOf()
        egg_labels.add(Label.EGGS)
        val milk_labels : MutableList<Label> = arrayListOf()
        milk_labels.add(Label.DAIRY)
        val water_labels : MutableList<Label> = arrayListOf()
        water_labels.add(Label.DRINK)
        val salt_labels : MutableList<Label> = arrayListOf()
        salt_labels.add(Label.SPICES)
        val suggar_labels : MutableList<Label> = arrayListOf()
        suggar_labels.add(Label.SUGGAR)
        val flour_labels : MutableList<Label> = arrayListOf()
        flour_labels.add(Label.CEREAL_PRODUCT)

        val ingredients : MutableList<Product> = arrayListOf()
        ingredients.add(0, Product(0, "Eier", "", egg_labels,"3 Stück", "", "", ""))
        ingredients.add(1, Product(1, "Milch", "", milk_labels,"200 ml", "", "", ""))
        ingredients.add(2, Product(2, "Wasser", "", water_labels,"250 ml", "", "", ""))
        ingredients.add(3, Product(3, "Salz", "", salt_labels,"3 Teelöffel", "", "", ""))
        ingredients.add(4, Product(4, "Zucker", "", suggar_labels,"350 gr", "", "", ""))
        ingredients.add(5, Product(5, "Grießmehl", "", flour_labels,"500 gr", "", "", ""))


        addVariousItems(Recipe(0, "Tasty Buddha Bowl", "Vegan",
            "1024 kcal", "Everything you need to make this back-to-school season the easiest and most delicious one yet — from " +
                    "grocery lists, to dinner guides, to simple recipes your whole family will love.",
            4.5F, "simple","35 min", "2 portions", "The best Buddha bowls start with a base of rice or whole " +
                    "grains which can be served cold or warm. Top with your favorite plant-based protein (tofu, seitan, edamame, beans), add fresh greens " +
                    "and fresh or roasted veggies. Next, drizzle with flavorful dressing or sauce and sprinkle with seeds or nuts. Top with fresh herbs and microgreens.",
            ingredients, "https://images.ichkoche.at/data/image/variations/496x384/12/buddha-bowl-mit-lachs-und-buchweizennudeln-rezept-img-116146.jpg"))
        addVariousItems(Recipe(0, "Tasty Chocolate Cupcakes", "Vegan", "1024 kcal",
            "Everything you need to make this back-to-school season the easiest and most delicious one yet — from " +
                    "grocery lists, to dinner guides, to simple recipes your whole family will love.",
            4F, "simple","35 min", "2 portions", "These super moist chocolate cupcakes pack TONS " +
                    "of chocolate flavor in each cupcake wrapper! Made from simple everyday ingredients, this easy cupcake recipe will be your " +
                    "new favorite. For best results, use natural cocoa powder and buttermilk. These chocolate cupcakes taste completely over-the-top " +
                    "with chocolate buttercream!", ingredients,
            "https://cdn.pixabay.com/photo/2020/05/01/09/13/cupcakes-5116009_960_720.jpg"))
        addVariousItems(Recipe(0, "Mini Schokoladen Muffins", "Vegan", "1024 kcal",
            "Everything you need to make this back-to-school season the easiest and most delicious one yet — from " +
                    "grocery lists, to dinner guides, to simple recipes your whole family will love.",
            3.5F, "simple","35 min", "2 portions", "These super moist chocolate cupcakes " +
                    "pack TONS of chocolate flavor in each cupcake wrapper! Made from simple everyday ingredients, this easy cupcake " +
                    "recipe will be your new favorite. For best results, use natural cocoa powder and buttermilk. These chocolate cupcakes " +
                    "taste completely over-the-top with chocolate buttercream!", ingredients,
            "https://cdn.pixabay.com/photo/2016/06/12/15/03/cupcakes-1452178_960_720.jpg"))
        addVariousItems(Recipe(0, "Mini Erdbeer Törtchen", "Vegan", "1024 kcal",
            "Everything you need to make this back-to-school season the easiest and most delicious one yet — from " +
                    "grocery lists, to dinner guides, to simple recipes your whole family will love.",
            4.5F, "simple","35 min", "2 portions", "These super moist chocolate cupcakes " +
                    "pack TONS of chocolate flavor in each cupcake wrapper! Made from simple everyday ingredients, this easy cupcake " +
                    "recipe will be your new favorite. For best results, use natural cocoa powder and buttermilk. These chocolate cupcakes " +
                    "taste completely over-the-top with chocolate buttercream!", ingredients,
            "https://cdn.pixabay.com/photo/2018/03/24/16/08/cake-3257019_960_720.jpg"))
        addVariousItems(Recipe(0, "Französische Tarte Erdbeer", "Vegan", "1024 kcal",
            "Everything you need to make this back-to-school season the easiest and most delicious one yet — from " +
                    "grocery lists, to dinner guides, to simple recipes your whole family will love.",
            5.0F, "simple","35 min", "2 portions", "These super moist chocolate cupcakes " +
                    "pack TONS of chocolate flavor in each cupcake wrapper! Made from simple everyday ingredients, this easy cupcake " +
                    "recipe will be your new favorite. For best results, use natural cocoa powder and buttermilk. These chocolate cupcakes " +
                    "taste completely over-the-top with chocolate buttercream!", ingredients,
            "https://cdn.pixabay.com/photo/2018/02/03/06/45/cake-3127014_960_720.jpg"))

    }

}