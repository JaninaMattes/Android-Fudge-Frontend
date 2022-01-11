package com.mobilesystems.feedme.ui.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilesystems.feedme.domain.model.Product
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.activityViewModels
import com.mobilesystems.feedme.databinding.ShoppingListCurrentFragmentBinding
import com.mobilesystems.feedme.ui.common.listener.GestureListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingListCurrentProductsGridFragment: Fragment() {

    private var detector: GestureDetectorCompat? = null

    // delegate to main activity so that ViewModel is preserved
    private val sharedViewModel: SharedShoppingListViewModel by activityViewModels()
    private lateinit var productListGridView: GridView
    private lateinit var adapter: ShoppingListCurrentProductsGridAdapter

    //view binding
    private var _binding: ShoppingListCurrentFragmentBinding? = null
    private val binding get() = _binding!!

    private val listener = object: ShoppingListCurrentProductsGridAdapter.ProductAdapterClickListener {

        override fun passData(product: Product, itemView: View) {
            // pass data and navigate to product detail view
            sharedViewModel.removeProductFromCurrentShoppingList(product)
            sharedViewModel.addProductToOldShoppingList(product)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = activity?.applicationContext
        detector = GestureDetectorCompat(context, GestureListener())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate layout for this fragment
        _binding = ShoppingListCurrentFragmentBinding.inflate(inflater, container, false)
        // Setup recycler view
        productListGridView = binding.shoppingListCurrentGridView

        //productListGridView.setOnItemLongClickListener { adapterView, view, i, l ->
        //}

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create the observer which updates the UI.
        val productListObserver = Observer<List<Product>?> { productList: List<Product>? ->
            // Update the UI and update the adapter for recycelerview
            if (productList != null) {
                val context = activity?.applicationContext
                adapter = ShoppingListCurrentProductsGridAdapter(context, productList, listener)
                productListGridView.adapter = adapter

                // Configure the grid view
                productListGridView.numColumns = 3
                productListGridView.horizontalSpacing = 15
                productListGridView.verticalSpacing = 15
                productListGridView.stretchMode = GridView.STRETCH_COLUMN_WIDTH
            }
        }
        // update adapter after data is loaded
        sharedViewModel.currentShoppingList.observe(viewLifecycleOwner, productListObserver)
    }

    companion object {
        fun newInstance() = ShoppingListCurrentProductsGridFragment()
    }
}