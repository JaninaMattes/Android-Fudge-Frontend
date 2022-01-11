package com.mobilesystems.feedme.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.Recipe
import com.squareup.picasso.Picasso

class DashboardRecipeListAdapter (
    private val dataSet: List<Recipe>?,
    private val itemClickListener: DashboardRecipeListAdapter.RecipeAdapterClickListener
) : RecyclerView.Adapter<DashboardRecipeListAdapter.RecipeListViewHolder>() {

    private lateinit var imageUrl: String

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class RecipeListViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        val cardView: CardView
        val recipeImageView: ImageView
        val recipeTypeLabelTextView: TextView
        val recipeNameTextView: TextView
        val recipeRatingBar: RatingBar
        val recipeCookingDifficulty: TextView

        init {
            cardView = view.findViewById(R.id.no_one_recipe_item_card_view)
            // bind views by view id
            recipeImageView = view.findViewById(R.id.no_one_recipe_image)
            recipeTypeLabelTextView = view.findViewById(R.id.no_one_recipe_label)
            recipeNameTextView = view.findViewById(R.id.no_one_recipe_name)
            recipeRatingBar = view.findViewById(R.id.no_one_recipe_rating_bar)
            recipeCookingDifficulty = view.findViewById(R.id.no_one_recipe_cooking_difficulty)

            // initialize clicklistener and pass clicked product for listitem position
            cardView.setOnClickListener{ v ->
                if (dataSet != null) {
                    itemClickListener.passData(dataSet[bindingAdapterPosition], v)
                }
            }
        }
    }

    interface RecipeAdapterClickListener {
        fun passData(recipe: Recipe, itemView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        // Create a view which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_number_one_recipe_item, parent, false)
        return RecipeListViewHolder(itemView)
    }

    override fun onBindViewHolder(recipeViewHolder: RecipeListViewHolder, position: Int) {
        if (dataSet != null) {
            // get selected product
            val currentItem = dataSet[position]
            // pass values to view items
            imageUrl = currentItem.imageUrl
            Picasso.get().load(imageUrl).into(recipeViewHolder.recipeImageView)
            recipeViewHolder.recipeCookingDifficulty.text = currentItem.difficulty
            recipeViewHolder.recipeRatingBar.rating = currentItem.rating
            recipeViewHolder.recipeNameTextView.text = currentItem.recipeName
            recipeViewHolder.recipeTypeLabelTextView.text = currentItem.recipeLabel
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }
}