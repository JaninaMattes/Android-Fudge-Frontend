package com.mobilesystems.feedme.ui.productdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.databinding.ProductDetailFragmentBinding
import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.ui.dashboard.SharedDashboardViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing the detail view of the product inventory list.
 */

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private val sharedViewModel: SharedDashboardViewModel by activityViewModels()

    //view binding
    private var _binding: ProductDetailFragmentBinding? = null
    private val binding get() = _binding!!

    // content on view
    private var productId: Int = 0
    private lateinit var productImg: String

    private lateinit var product: Product

    //enum label
    private var labelList: MutableList<Label>? = null

    // This property is only valid between onCreateView and onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflate Layout for this fragment
        _binding = ProductDetailFragmentBinding.inflate(inflater, container, false)

        // Elements
        val productImageview: ImageView = binding.productDetailImage
        val productNameTextView: TextView = binding.textProductName
        val productExpirationTextView: TextView = binding.textProductDetailExpiration
        val productQuantityTextView: TextView = binding.textProductDetailQuantity
        val productNutritionTextView: TextView = binding.textProductDetailNutrition
        val productManufacturerTextView: TextView = binding.textProductDetailManufacturer

        //set editTexts non-editable
        productExpirationTextView.isEnabled = false
        productQuantityTextView.isEnabled = false
        productNutritionTextView.isEnabled = false
        productManufacturerTextView.isEnabled = false

        // Create the observer which updates the UI.
        val productObserver = Observer<Product?> { product : Product? ->
            if (product != null) {
                // retrieve id
                productId = product.productId
                productImg = product.imageUrl

                // use picasso to locally store image
                Picasso.get().load(productImg).into(productImageview)
                productNameTextView.text =product.productName
                productExpirationTextView.text = product.expirationDate
                productNutritionTextView.text = product.nutritionValue
                productQuantityTextView.text = product.quantity
                productManufacturerTextView.text = product.manufacturer
                labelList = product.labels
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        sharedViewModel.selectedProduct.observe(viewLifecycleOwner, productObserver)

        binding.btnEditProductDetail.setOnClickListener{
            // TODO  Set Label editable

            //set EditButtons visibility gone
            binding.btnEditProductDetail.visibility = View.GONE

            //set save and cancel button visible
            binding.btnSaveProduct.visibility = View.VISIBLE
            binding.btnCancelSaveProduct.visibility = View.VISIBLE

            //set editText editable
            productExpirationTextView.isEnabled = true
            productQuantityTextView.isEnabled = true
            productNutritionTextView.isEnabled = true
            productManufacturerTextView.isEnabled = true
        }

        binding.btnCancelSaveProduct.setOnClickListener{
            //set EditButtons visibility visible
            binding.btnEditProductDetail.visibility = View.VISIBLE

            //set save and cancel button gone
            binding.btnSaveProduct.visibility = View.GONE
            binding.btnCancelSaveProduct.visibility = View.GONE

            //set editText editable
            productExpirationTextView.isEnabled = false
            productQuantityTextView.isEnabled = false
            productNutritionTextView.isEnabled = false
            productManufacturerTextView.isEnabled = false
        }

        binding.btnSaveProduct.setOnClickListener{

            val productname = productNameTextView.text.toString()// TODO: Productname cannot be empty!!
            var productquantity = productQuantityTextView.text.toString()
            var productExpirationDate = productExpirationTextView.text.toString()
            var productManufacturer = productManufacturerTextView.text.toString()
            var productNutrition = productNutritionTextView.text.toString()

           //TODO: Check product expiration
            // Check product quantity
            if(productquantity.isEmpty()){
                productquantity = "1 Stück"
            }else if (productquantity.isDigitsOnly()){
                val quantity = productquantity.filter { it.isDigit() }
                productquantity = "$quantity Stück"
            }
            // Check product manufacturer
            if(productManufacturer.isEmpty()){
                productManufacturer = "-"
            }
            // Check product nutrition value
            if(productNutrition.isEmpty()){
                productNutrition = "-"
            }else if (productNutrition.isDigitsOnly()){
                val quantity = productNutrition.filter { it.isDigit() }
                productNutrition = "$quantity kcal"
            }

            // TODO: Replace imageURL with images from database or photography
            product = Product(productId, productname, productExpirationDate, labelList, productquantity, productManufacturer,
                productNutrition, productImg)

            sharedViewModel.updateProductInInventoryList(product)

            //navigate to inventory list
            val action = ProductFragmentDirections.actionProductFragmentToNavigationInventorylist2()
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Add ingredient list as child fragment
        addChildFragment()
    }

    private fun addChildFragment(){
        // nest child fragment into parent fragment
        // https://developer.android.com/about/versions/android-4.2#NestedFragments
        val productTagListFragment = ProductTagListFragment()

        childFragmentManager.beginTransaction().apply {
            add(R.id.product_label_list_fragment, productTagListFragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // sharedViewModel.updateProduct()
    }

    companion object {
        const val TAG = "ProductFragment"
        fun newInstance() = ProductFragment()
    }
}