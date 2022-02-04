package com.mobilesystems.feedme.ui.recipedetail

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.databinding.RecipeIngredientItemBinding
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.ui.common.utils.capitalizeWords
import com.mobilesystems.feedme.ui.common.utils.containsSubstring
import com.mobilesystems.feedme.ui.recipes.SharedRecipesViewModel
import java.util.*

/**
 * Tutorial: https://developer.android.com/guide/topics/ui/layout/recyclerview
 *          https://developersbreach.com/navigation-with-architecture-components-android/
 */
class RecipeIngredientListAdapter(
    private val context: Context,
    private val sharedViewModel: SharedRecipesViewModel,
    private val dataSet: List<Product>?
) : RecyclerView.Adapter<RecipeIngredientListAdapter.IngredientViewHolder>() {

    private var _itemBinding: RecipeIngredientItemBinding? = null
    private val itemBinding get() = _itemBinding!!

    /**
     * Provide a reference to the type of views that are used
     * (custom ViewHolder).
     */
    inner class IngredientViewHolder(itemBinding: RecipeIngredientItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val ingredientNameTextView: TextView = itemBinding.textIngredientName
        val ingredientQuantityTextView: TextView = itemBinding.textIngredientQuantity
        val ingredientAvailableIcon: ImageView = itemBinding.ingredientAvailableIcon
        val ingredientAvailableTextView: TextView = itemBinding.textIngredientAvailability
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        // Create a view which defines the UI of the list item
        _itemBinding = RecipeIngredientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: IngredientViewHolder, position: Int) {

        if (dataSet != null) {
            // get selected product
            val currentItem = dataSet[position]
            if(sharedViewModel.isProductAvailable(sharedViewModel.availableIngredients.value, currentItem)){
                val color = ContextCompat.getColor(context, R.color.red_200)
                viewHolder.ingredientAvailableIcon.setImageResource(R.mipmap.ic_available_icon)
                viewHolder.ingredientAvailableIcon.visibility = ImageView.VISIBLE
                viewHolder.ingredientAvailableTextView.setTextColor(color)
                viewHolder.ingredientAvailableTextView.text = context.getString(R.string.Available)
            }else{
                val color = ContextCompat.getColor(context, R.color.light_grey)
                viewHolder.ingredientAvailableIcon.visibility = ImageView.GONE
                viewHolder.ingredientAvailableTextView.setTextColor(color)
                viewHolder.ingredientAvailableTextView.text = context.getString(R.string.NotAvailable)
            }
            // pass values to view items
            viewHolder.ingredientNameTextView.text = currentItem.productName
            viewHolder.ingredientQuantityTextView.text = currentItem.quantity
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }
}