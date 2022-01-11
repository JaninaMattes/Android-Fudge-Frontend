package com.mobilesystems.feedme.ui.productdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.Label

class ProductTagListAdapter(
    private val dataSet: List<Label>?
) : RecyclerView.Adapter<ProductTagListAdapter.ProductTagViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ProductTagViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        val productTagLabelTextView: TextView

        init{
            productTagLabelTextView = view.findViewById(R.id.text_product_tag_label)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductTagViewHolder {
        // Create a view which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_detail_tag_item, parent, false)
        return ProductTagViewHolder(itemView)
    }

    override fun onBindViewHolder(tagViewHolder: ProductTagViewHolder, position: Int) {
        if (dataSet != null) {
            // get selected product
            val currentItem = dataSet[position]
            // pass values to view items
            tagViewHolder.productTagLabelTextView.text = currentItem.label
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }

}