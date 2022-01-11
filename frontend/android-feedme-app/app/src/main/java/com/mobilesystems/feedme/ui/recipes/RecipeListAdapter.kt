package com.mobilesystems.feedme.ui.recipes

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.Recipe
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Tutorial: https://developer.android.com/guide/topics/ui/layout/recyclerview
 *          https://developersbreach.com/navigation-with-architecture-components-android/
 */
class RecipeListAdapter(
    private val dataSet: List<Recipe>?,
    private val itemClickListener: RecipeAdapterClickListener
) : RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>() {

    interface RecipeAdapterClickListener {
        fun passData(recipe: Recipe, itemView: View)
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class RecipeViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        val cardView: CardView
        val recipeImageView: CircleImageView
        val recipeLabel: TextView
        val recipeName: TextView
        val ratingBar: RatingBar
        val recipeRating: TextView
        val cookingDifficulty: TextView

        init {
            // Define click listener for the ViewHolder's View.
            cardView = view.findViewById(R.id.recipe_card_view)
            recipeImageView = view.findViewById(R.id.recipe_image)
            recipeLabel = view.findViewById(R.id.recipe_label)
            recipeName = view.findViewById(R.id.recipe_name)
            cookingDifficulty = view.findViewById(R.id.cooking_difficulty)
            ratingBar = view.findViewById(R.id.rating_bar)
            recipeRating = view.findViewById(R.id.text_rating)

            // initialize clicklistener and pass clicked product for listitem position
            cardView.setOnClickListener{ v ->
                if (dataSet != null) {
                    itemClickListener.passData(dataSet[bindingAdapterPosition], v)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        // Create a view which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)

        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: RecipeViewHolder, position: Int) {
        if (dataSet != null) {
            // get selected product
            val currentItem = dataSet[position]
            Picasso.get().load(currentItem.imageUrl).into(viewHolder.recipeImageView)
            viewHolder.recipeName.text = currentItem.recipeName
            viewHolder.recipeLabel.text = currentItem.recipeLabel
            viewHolder.cookingDifficulty.text = currentItem.difficulty
            viewHolder.ratingBar.rating = currentItem.rating
            viewHolder.recipeRating.text = "${currentItem.rating}"
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }
    
}