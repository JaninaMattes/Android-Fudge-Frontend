package com.mobilesystems.feedme.ui.shoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.Product

class ShoppingListCurrentProductsGridAdapter(
    private val context: Context?,
    private val dataSet: List<Product>?,
    private val itemClickListener: ShoppingListCurrentProductsGridAdapter.ProductAdapterClickListener
) : BaseAdapter() {

    interface ProductAdapterClickListener {
        fun passData(product: Product, itemView: View)
    }

    override fun getView(position:Int, convertView: View?, parent: ViewGroup?): View{
        // Inflate the custom view
        val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = inflater.inflate(R.layout.shopping_list_current_item,null)

        val cardView: CardView = itemView.findViewById(R.id.card_view_shopping_list_item)
        val shoppingListColour: FrameLayout = itemView.findViewById(R.id.shoppinglist_item_colour)
        val shoppingListIcon: ImageView = itemView.findViewById(R.id.shopping_item_food_icon)
        val shoppingListItemName: TextView = itemView.findViewById(R.id.shopping_list_item_name)
        val shoppingListItemQuantity: TextView = itemView.findViewById(R.id.shopping_list_item_quantity)

        if(dataSet != null){
            val currentItem = dataSet[position]
            // place colour and correct icon programmatically
            if(context != null) {
                shoppingListColour.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.theme_creme_50
                ))
                shoppingListIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_food_dummy))
            }
            shoppingListItemName.text = currentItem.productName
            shoppingListItemQuantity.text = currentItem.quantity
        }

        // initialize clicklistener and pass clicked product for listitem position
        cardView.setOnClickListener{ v ->
            if (dataSet != null) {
                itemClickListener.passData(dataSet[position], v)
            }
        }

        return  itemView
    }

    override fun getItem(position: Int): Any {
        // TODO: Correct this workaround with dummy data
        var currentItem = Product(0, "", "", mutableListOf(),
            "", "", "", "")
        if (dataSet != null) {
            // get selected product
            currentItem = dataSet[position]
        }
        return currentItem
    }

    override fun getItemId(position: Int): Long {
        var itemId: Long = 0
        if (dataSet != null) {
            // get selected product
            itemId = dataSet[position].productId.toLong()
        }
        return itemId
    }

    override fun getCount(): Int {
        return dataSet?.size ?: 0
    }
}