package com.mobilesystems.feedme.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.ui.common.utils.getTimeDiff
import com.mobilesystems.feedme.domain.model.Product

class DashboardExpiringProductListAdapter (
    private val context: Context,
    private val dataSet: List<Product>?,
    private val itemClickListener: DashboardExpiringProductListAdapter.ExpiringProductsAdapterClickListener
) : RecyclerView.Adapter<DashboardExpiringProductListAdapter.ProductViewHolder>() {

    private lateinit var imageUrl: String

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ProductViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        val cardView: CardView
        val productNameTextView: TextView
        val productExpirationDate: TextView

        init {
            cardView = view.findViewById(R.id.expiring_product_item_card_view)
            productNameTextView = itemView.findViewById(R.id.text_expiring_product_name)
            productExpirationDate = itemView.findViewById(R.id.text_expiring_product_date)

            // initialize clicklistener and pass clicked product for listitem position
            cardView.setOnClickListener{ v ->
                if (dataSet != null) {
                    itemClickListener.passData(dataSet[bindingAdapterPosition], v)
                }
            }
        }
    }

    interface ExpiringProductsAdapterClickListener {
        fun passData(recipe: Product, itemView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Create a view which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_expiring_product_item, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(productViewHolder: ProductViewHolder, position: Int) {
        if (dataSet != null) {
            // get selected product
            val currentItem = dataSet[position]
            // pass values to view items
            imageUrl = currentItem.imageUrl
            // Calculate difference between current date to expiration date
            val expDays = getTimeDiff(currentItem.expirationDate)
            if(expDays <= 3){
                productViewHolder.productExpirationDate.setTextColor(ContextCompat.getColor(context, R.color.bright_red_200))
            } else {
                productViewHolder.productExpirationDate.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            productViewHolder.productExpirationDate.text = "$expDays Tagen"
            productViewHolder.productNameTextView.text = currentItem.productName
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }
}