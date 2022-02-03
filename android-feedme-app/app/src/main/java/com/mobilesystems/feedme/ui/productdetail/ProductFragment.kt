package com.mobilesystems.feedme.ui.productdetail

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.databinding.ProductDetailFragmentBinding
import com.mobilesystems.feedme.domain.model.Image
import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.ui.inventorylist.AddProductToInventoryFragment
import com.mobilesystems.feedme.ui.inventorylist.SharedInventoryViewModel
import com.mobilesystems.feedme.ui.profile.UserProfileFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import java.lang.Exception

/**
 * A fragment representing the detail view of the product inventory list.
 */

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private val sharedViewModel: SharedInventoryViewModel by activityViewModels()

    //view binding
    private var _binding: ProductDetailFragmentBinding? = null
    private val binding get() = _binding!!

    //safeargs
    private val args: ProductFragmentArgs by navArgs()

    // content on view
    private var productImageBitmap: Bitmap? = null
    private var productId: Int = 0
    private var productImg: Image? = null

    private lateinit var productImageview: ImageView
    private lateinit var productExpirationTextView: TextView
    private lateinit var productQuantityTextView: TextView
    private lateinit var productNutritionTextView: TextView
    private lateinit var productManufacturerTextView: TextView

    //enum product label
    private var labelList: MutableList<Label>? = null

    // This property is only valid between onCreateView and onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // inflate Layout for this fragment
        _binding = ProductDetailFragmentBinding.inflate(inflater, container, false)

        // Elements
        val productNameTextView: TextView = binding.textProductName
        productImageview = binding.productDetailImage
        productExpirationTextView = binding.textProductDetailExpiration
        productQuantityTextView = binding.textProductDetailQuantity
        productNutritionTextView = binding.textProductDetailNutrition
        productManufacturerTextView = binding.textProductDetailManufacturer

        //set editTexts non-editable
        setEditTextEnabled(false)
        setImageViewExchangeable(true)

        //get argument safargs
        val product: Product = args.product
        productId = product.productId
        productNameTextView.text = product.productName
        productQuantityTextView.text = product.quantity
        productExpirationTextView.text = product.expirationDate
        productNutritionTextView.text = product.nutritionValue
        productManufacturerTextView.text = product.manufacturer
        // set values
        labelList = product.labels
        productImg = product.productImage
        val imageUrl = productImg?.imageUrl ?:
            "https://cdn.pixabay.com/photo/2017/06/06/22/37/italian-cuisine-2378729_960_720.jpg"
        productImageBitmap = productImg?.bitmap
        if(productImageBitmap != null){
            productImageview.setImageBitmap(productImageBitmap)
        }else {
            Picasso.get().load(imageUrl).into(productImageview)
        }
        sharedViewModel.selectProduct(product)

        binding.btnEditProductDetail.setOnClickListener{
            //set EditButtons visibility gone
            binding.btnEditProductDetail.visibility = View.GONE

            //set save and cancel button visible
            binding.btnSaveProduct.visibility = View.VISIBLE
            binding.btnCancelSaveProduct.visibility = View.VISIBLE

            //set editText editable
            setEditTextEnabled(true)
            //setImageViewExchangeable(true)
        }

        binding.btnCancelSaveProduct.setOnClickListener{
            //set EditButtons visibility visible
            binding.btnEditProductDetail.visibility = View.VISIBLE
            //set save and cancel button gone
            binding.btnSaveProduct.visibility = View.GONE
            binding.btnCancelSaveProduct.visibility = View.GONE
            //set editText editable
            setEditTextEnabled(false)
            //setImageViewExchangeable(false)
        }

        binding.btnSaveProduct.setOnClickListener{

            val productname = productNameTextView.text.toString()// TODO: Productname cannot be empty!!
            var productquantity = productQuantityTextView.text.toString()
            val productExpirationDate = productExpirationTextView.text.toString()
            var productManufacturer = productManufacturerTextView.text.toString()
            var productNutrition = productNutritionTextView.text.toString()
            // check quantity
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
            // create new image instance with new values
            val productImage = createUpdateImage(productImg, productImageBitmap)
            // create new product instance
            val product = Product(
                productId, productname, productExpirationDate, labelList,
                productquantity, productManufacturer,
                productNutrition, productImage)

            sharedViewModel.updateProductOnInventory(product)

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

    override fun onPause() {
        super.onPause()
        // free memory on native heap
        productImageBitmap = null
        productImg = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Gallery picker to select custom image
        if (requestCode == AddProductToInventoryFragment.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            productImageBitmap = loadImageFromGallery(data)
        }
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

    private fun setImageViewExchangeable(boolean: Boolean){
        productImageview.isClickable = boolean
        if(boolean) {
            productImageview.setOnClickListener {
                // Image picking from Gallery
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                // code for crop image to reduce size
                i.putExtra("crop", "true")
                i.putExtra("aspectX", 80)
                i.putExtra("aspectY", 80)
                i.putExtra("outputX", 156)
                i.putExtra("outputY", 256)

                try {
                    i.putExtra("return-data", true)
                    startActivityForResult(
                        Intent.createChooser(i, "Select Picture"),
                        AddProductToInventoryFragment.IMAGE_REQUEST_CODE
                    )
                } catch (ex: ActivityNotFoundException) {
                    ex.printStackTrace()
                    val context = activity?.applicationContext
                    Toast.makeText(context, "Gallery picker failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadImageFromGallery(data: Intent?) : Bitmap? {
        try {
            //profileImageView.setImageURI(data?.data)
            val uri = data?.data
            if(uri != null) {
                val inputStream: InputStream? = activity?.contentResolver?.openInputStream(uri)
                productImageBitmap = BitmapFactory.decodeStream(inputStream)
                val newProductImage = createUpdateImage(productImg, productImageBitmap)
                sharedViewModel.updateImage(newProductImage)
                Log.d(TAG, "Set image from gallery.")
            } else {
                Log.d(TAG, "URI is null.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val context = activity?.applicationContext
            Toast.makeText(context,"Gallery picker failed", Toast.LENGTH_SHORT).show()
        }

        return productImageBitmap
    }

    private fun createUpdateImage(img: Image?, bitmap: Bitmap?): Image{
        // Create new user object with correct image to pass to backend
        val newImg = Image(
                imageId = img?.imageId ?: 0,
                imageName = img?.imageName ?: "Produktbild",
                imageUrl = img?.imageUrl ?: "https://cdn.pixabay.com/photo/2017/06/06/22/37/italian-cuisine-2378729_960_720.jpg",
                bitmap = productImageBitmap ?: img?.bitmap // place new image from gallery if available
            )
        Log.d(UserProfileFragment.TAG, "Create new product image.")
        productImg = newImg
        return newImg
    }

    private fun setEditTextEnabled(boolean: Boolean) {
        productExpirationTextView.isEnabled = boolean
        productQuantityTextView.isEnabled = boolean
        productNutritionTextView.isEnabled = boolean
        productManufacturerTextView.isEnabled = boolean
    }

    companion object {
        const val TAG = "ProductFragment"
        fun newInstance() = ProductFragment()
    }
}