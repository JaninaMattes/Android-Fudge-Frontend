package com.mobilesystems.feedme.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.Product

class SearchListAdapter (
    private val context: Context?,
    private val dataSet: List<String>,
    private val itemClickListener: SearchListAdapter.ProductAdapterClickListener
    ) : RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {


        inner class SearchListViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            private var view: View = v

            val searchListItemName: TextView
            val searchListLocation: TextView

            init {
                // bind views by view id
                searchListItemName = itemView.findViewById(R.id.search_title)
                searchListLocation = itemView.findViewById(R.id.search_location)
            }
        }


        interface ProductAdapterClickListener {
            fun passData(product: Product, itemView: View)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListAdapter.SearchListViewHolder {
        // Create a view which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.inventory_item, parent, false)
        return SearchListViewHolder(itemView)
    }

        override fun onBindViewHolder(searchListViewHolder: SearchListAdapter.SearchListViewHolder, position: Int) {
            if(dataSet.isNotEmpty()){
                searchListViewHolder.searchListItemName.text
            }

        }

        override fun getItemCount(): Int {
            return dataSet?.size ?: 0
        }

}