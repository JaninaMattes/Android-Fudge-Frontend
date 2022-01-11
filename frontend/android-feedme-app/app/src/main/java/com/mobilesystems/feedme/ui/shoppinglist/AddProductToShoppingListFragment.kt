package com.mobilesystems.feedme.ui.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobilesystems.feedme.databinding.ShoppingListAddProductFragmentBinding
import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.shopping_list_add_product_fragment.*

@AndroidEntryPoint
class AddProductToShoppingListFragment : Fragment(com.mobilesystems.feedme.R.layout.shopping_list_add_product_fragment) {

    // by delegates to main activity to preceive shared viewmodel
    private val sharedViewModel: SharedShoppingListViewModel by activityViewModels()

    private var _binding: ShoppingListAddProductFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for this fragment
        _binding = ShoppingListAddProductFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_enteraddproducttoshoppinglist.setOnClickListener {

            //add new product to sharedviewmodel
            val productname = binding.editTextAddnewproducttitle.text.toString()
            var productquantity = binding.editTextAddnewproductquantity.text.toString()

            // TODO: Similar to Login check for input error in seperate class
            if (productquantity.isEmpty()){
                productquantity = "1 Stück"
            }else if (productquantity.isDigitsOnly()){
                val quantity = productquantity.filter { it.isDigit() }
                productquantity = "$quantity Stück"
            }

            val labels : MutableList<Label> = arrayListOf() // empty placeholder list add correct labels later in inventory list
            product = Product(0, productname, "", labels, productquantity, "", "", "")
            sharedViewModel.addProductToCurrentShoppingList(product)

            //navigate to shoppinglist
            val action = AddProductToShoppingListFragmentDirections.actionAddProductToShoppingListFragmentToNavigationShoppingList()
            findNavController().navigate(action)
        }
    }

    companion object {
        fun newInstance() = AddProductToShoppingListFragment()
    }
}