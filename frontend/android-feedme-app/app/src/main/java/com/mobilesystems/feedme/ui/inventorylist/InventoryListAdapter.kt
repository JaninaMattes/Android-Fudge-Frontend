package com.mobilesystems.feedme.ui.inventorylist

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.ui.common.utils.getTimeDiff
import com.mobilesystems.feedme.domain.model.Product
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Tutorial: https://developer.android.com/guide/topics/ui/layout/recyclerview
 *          https://developersbreach.com/navigation-with-architecture-components-android/
 */
class InventoryListAdapter(
    private val context: Context,
    private val dataSet: List<Product>?,
    private val itemClickListener: ProductAdapterClickListener
) : RecyclerView.Adapter<InventoryListAdapter.ProductViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ProductViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        val cardView: CardView
        val imageView: CircleImageView
        val productName: TextView
        val productQuantity: TextView
        val productExpiration: TextView
        val productExpirationDate: TextView

        init {
            // bind views by view id
            cardView = view.findViewById(R.id.inventory_card_view)
            imageView = view.findViewById(R.id.product_image)
            productName = view.findViewById(R.id.product_name)
            productQuantity = view.findViewById(R.id.product_quantity)
            productExpiration = view.findViewById(R.id.product_expiration_status)
            productExpirationDate = view.findViewById(R.id.product_expiration_date)


            // initialize clicklistener and pass clicked product for listitem position
            cardView.setOnClickListener{ v ->
                if (dataSet != null) {
                    itemClickListener.passData(dataSet[bindingAdapterPosition], v)
                }
            }
        }
    }

    interface ProductAdapterClickListener {
        fun passData(product: Product, itemView: View)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Create a view which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.inventory_item, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ProductViewHolder, position: Int) {

        if (dataSet != null) {
            // get selected product
            val currentItem = dataSet[position]
            // pass values to view items
            Picasso.get().load(currentItem.imageUrl).into(viewHolder.imageView)
            viewHolder.productName.text = currentItem.productName
            viewHolder.productQuantity.text = currentItem.quantity

            val expDays = getTimeDiff(currentItem.expirationDate)
            if(expDays <= 3){
                viewHolder.productExpiration.setTextColor(ContextCompat.getColor(context, R.color.bright_red_200))
                viewHolder.productExpirationDate.setTextColor(ContextCompat.getColor(context, R.color.bright_red_200))
            } else {
                viewHolder.productExpiration.setTextColor(ContextCompat.getColor(context, R.color.black))
                viewHolder.productExpirationDate.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            var productExpText = "-"

            if(expDays > 0){
                productExpText = "${expDays} Tagen"
            }
            else{
                productExpText = "Heute"
            }
            viewHolder.productExpiration.text = productExpText
            viewHolder.productExpirationDate.text = currentItem.expirationDate
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }

}