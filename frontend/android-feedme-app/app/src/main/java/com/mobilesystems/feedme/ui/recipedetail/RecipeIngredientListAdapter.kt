package com.mobilesystems.feedme.ui.recipedetail

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.ui.recipes.SharedRecipesViewModel

/**
 * Tutorial: https://developer.android.com/guide/topics/ui/layout/recyclerview
 *          https://developersbreach.com/navigation-with-architecture-components-android/
 */
class RecipeIngredientListAdapter(
    private val context: Context,
    private val sharedViewModel: SharedRecipesViewModel,
    private val dataSet: List<Product>?
) : RecyclerView.Adapter<RecipeIngredientListAdapter.IngredientViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class IngredientViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        val ingredientNameTextView: TextView
        val ingredientQuantityTextView: TextView
        val ingredientAvailableIcon: ImageView
        val ingredientAvailableTextView: TextView

        init {
            // bind views by view id
            ingredientAvailableIcon = view.findViewById(R.id.ingredient_available_icon)
            ingredientNameTextView = view.findViewById(R.id.text_ingredient_name)
            ingredientQuantityTextView = view.findViewById(R.id.text_ingredient_quantity)
            ingredientAvailableTextView = view.findViewById(R.id.text_ingredient_availability)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        // Create a view which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_ingredient_item, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: IngredientViewHolder, position: Int) {

        if (dataSet != null) {
            // get selected product
            val currentItem = dataSet[position]
            if(sharedViewModel.availableIngredients.value?.contains(currentItem) == true){
                val color = ContextCompat.getColor(context, R.color.red_200)
                viewHolder.ingredientAvailableIcon.setImageResource(R.mipmap.ic_available_icon)
                viewHolder.ingredientAvailableIcon.visibility = ImageView.VISIBLE
                viewHolder.ingredientAvailableTextView.setTextColor(color)
                viewHolder.ingredientAvailableTextView.text = "vorhanden"
            }else{
                val color = ContextCompat.getColor(context, R.color.light_grey)
                viewHolder.ingredientAvailableIcon.visibility = ImageView.GONE
                viewHolder.ingredientAvailableTextView.setTextColor(color)
                viewHolder.ingredientAvailableTextView.text = "nicht vorhanden"
            }
            // pass values to view items
            viewHolder.ingredientNameTextView.text = currentItem.productName
            viewHolder.ingredientQuantityTextView.text = currentItem.quantity

            // TODO define availablilty and exchange icons + colour
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }

}