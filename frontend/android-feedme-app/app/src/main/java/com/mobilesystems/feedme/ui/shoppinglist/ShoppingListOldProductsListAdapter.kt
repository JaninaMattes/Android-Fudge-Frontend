package com.mobilesystems.feedme.ui.shoppinglist


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.ui.shoppinglist.ShoppingListOldProductsListAdapter.ShoppingListOldProductsViewHolder

class ShoppingListOldProductsListAdapter(
    private val context: Context?,
    private val dataSet: List<Product>?,
    private val itemClickListener: ShoppingListOldProductsListAdapter.ProductAdapterClickListener
) : RecyclerView.Adapter<ShoppingListOldProductsViewHolder>() {


    inner class ShoppingListOldProductsViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        val cardView: CardView
        val shoppingListColour: FrameLayout
        val shoppingListIcon: ImageView
        val shoppingListItemName: TextView

        init {
            cardView = view.findViewById(R.id.card_view_old_shopping_list_item)
            // bind views by view id
            shoppingListColour = itemView.findViewById(R.id.frame_layout_old_shopping_list_item_colour)
            shoppingListIcon= itemView.findViewById(R.id.old_shopping_item_food_icon)
            shoppingListItemName = itemView.findViewById(R.id.old_shopping_list_item_name)

            // initialize clicklistener and pass clicked product for listitem position
            cardView.setOnClickListener{ v ->
                if (dataSet != null) {
                    if(context != null) {
                        shoppingListColour.setBackgroundColor(
                            ContextCompat.getColor(context, R.color.bright_red_200))
                    }
                    itemClickListener.passData(dataSet[position], v)
                }
            }
        }
    }


    interface ProductAdapterClickListener {
        fun passData(product: Product, itemView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListOldProductsViewHolder {
        // Create a view which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_list_old_item, parent, false)
        return ShoppingListOldProductsViewHolder(itemView)
    }

    override fun onBindViewHolder(shoppingListViewHolder: ShoppingListOldProductsViewHolder, position: Int) {
        if(dataSet != null){
            val currentItem = dataSet[position]
            // place colour and correct icon programmatically
            if(context != null) {
                shoppingListViewHolder.shoppingListColour.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.light_grey))
                shoppingListViewHolder.shoppingListIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.mipmap.ic_food_dummy))
            }
            shoppingListViewHolder.shoppingListItemName.text = currentItem.productName
        }

    }

    override fun getItemCount(): Int {
    return dataSet?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        var itemId: Long = 0
        if (dataSet != null) {
            // get selected product
            itemId = dataSet[position].productId.toLong()
        }
        return itemId
    }

}