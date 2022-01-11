package com.mobilesystems.feedme.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.FoodType

class UserFoodPrefListAdapter(private val dataSet: List<FoodType>?
) : RecyclerView.Adapter<UserFoodPrefListAdapter.UserTagListViewHolder>() {

    private lateinit var imageUrl: String

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class UserTagListViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        val cardView: CardView
        val tagNameTextView: TextView

        init {
            // bind views by view id
            cardView = view.findViewById(R.id.product_tag_card_view)
            tagNameTextView = view.findViewWithTag(R.id.text_product_tag_label)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTagListViewHolder {
        // Create a view which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_detail_tag_item, parent, false)
        return UserTagListViewHolder(itemView)
    }

    override fun onBindViewHolder(userTagViewHolder: UserTagListViewHolder, position: Int) {
        if (dataSet != null) {
            // get selected product
            val currentItem = dataSet[position]
            // pass values to view items
            userTagViewHolder.tagNameTextView.text = currentItem.label
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }
}