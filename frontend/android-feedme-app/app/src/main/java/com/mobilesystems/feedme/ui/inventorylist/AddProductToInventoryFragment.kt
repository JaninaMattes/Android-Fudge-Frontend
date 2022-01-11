package com.mobilesystems.feedme.ui.inventorylist

import android.app.AlertDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.mobilesystems.feedme.databinding.InventoryAddProductFragmentBinding
import com.mobilesystems.feedme.domain.model.Label
import com.mobilesystems.feedme.domain.model.Product
import kotlinx.android.synthetic.main.inventory_add_product_fragment.*
import kotlinx.android.synthetic.main.inventory_add_product_fragment.view.*
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.icu.text.SimpleDateFormat
import android.widget.*
import androidx.core.text.isDigitsOnly
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.ui.common.utils.addDaysToCurrentDate
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddProductToInventoryFragment : Fragment(), AdapterView.OnItemSelectedListener{

    private val sharedViewModel: SharedInventoryViewModel by activityViewModels()
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var alertDialog: AlertDialog

    // view binding
    private var _binding: InventoryAddProductFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: Product
    private lateinit var productLabel: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for this fragment
        _binding = InventoryAddProductFragmentBinding.inflate(inflater, container, false)
        val rootView = binding.root

        // Add dropdown menu
        val spinner: Spinner = rootView.drop_down_add_new_product_labels_inventory
        val addExpirationDate: EditText = binding.editTextAddNewProductExpirationDateInventory
        // Create an ArrayAdapter using the string array and a default spinner layout
        val context = activity?.applicationContext
        val values: List<String> = sharedViewModel.loadAllProductLabels().value!!

        if (context != null){
            ArrayAdapter(context, android.R.layout.simple_spinner_item, values).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }else{
            Log.d(TAG, "App context is empty.")
        }
        spinner.onItemSelectedListener = this

        // Datepicker
        val date = OnDateSetListener { _, year, month, day ->
            val myFormat = "dd.MM.yyyy"
            val dateFormat = SimpleDateFormat(myFormat, Locale.GERMANY)

            // Setup calendar
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            addExpirationDate.setText(dateFormat.format(calendar.time))
        }
        // open on click to editText
        addExpirationDate.setOnClickListener(View.OnClickListener {
            val context = activity
            if(context != null) {
                DatePickerDialog(context, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_scan_product.setOnClickListener {
            // Setup for Zxing Barcode Reader
            // Tutorial: https://github.com/zxing/zxing/wiki/Scanning-Via-Intent
            //           https://medium.com/@dev.jeevanyohan/zxing-qr-code-scanner-android-implementing-in-activities-fragment-custom-colors-faa68bfc761d
            // TODO: Fix deprecated IntentIntegrator
            val intentIntegrator = IntentIntegrator.forSupportFragment(this@AddProductToInventoryFragment)
            intentIntegrator.setOrientationLocked(false)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.setPrompt("Bitte scanne einen Barcode.")
            intentIntegrator.setBarcodeImageEnabled(true)
            intentIntegrator.initiateScan()
        }

        button_enteraddproducttoinventorylist.setOnClickListener {
            var newLabel: Label?
            val productname = binding.editTextAddnewproducttitleInventory.text.toString().trim() // TODO: Productname cannot be empty!!
            var productquantity = binding.editTextAddnewproductquantityInventory.text.toString().trim()
            var productExpirationDate = binding.editTextAddNewProductExpirationDateInventory.text.toString().trim()
            var productManufacturer = binding.editTextAddnewproductManufacturerInventory.text.toString().trim()
            var productNutrition = binding.editTextAddnewproductNutritionvalueInventory.text.toString().trim()

            // TODO: Update list or single value for tag - one tag per product?
            val productLabelList : MutableList<Label> = arrayListOf()
            if(productLabel.isNotEmpty()) {
                newLabel = Label.from(productLabel)

                if (newLabel != null) {
                    productLabelList.add(newLabel)
                    if(productExpirationDate.isEmpty()){
                        // calculate expiration
                        productExpirationDate = calculateExpDate(newLabel)
                    } else {
                        Log.d(TAG, "The expiration date is empty.")
                    }
                }else{
                    Log.d(TAG, "The label is empty.")
                }

            }else{
                val alertBuilder = createAlert()
                // Show dialog
                alertDialog = alertBuilder.create()
                alertDialog.show()
            }
            // TODO: Similar to Login check error in seperate class
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
            product = Product(0, productname, productExpirationDate, productLabelList, productquantity, productManufacturer,
                productNutrition, "https://cdn.pixabay.com/photo/2020/06/23/09/06/donut-5331966_960_720.jpg")
            sharedViewModel.addProductToInventoryList(product)
            val action = AddProductToInventoryFragmentDirections.actionAddProductToInventoryFragmentToNavigationInventorylist()
            findNavController().navigate(action)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // receive result
        // ZXing Scan via Intent: https://github.com/zxing/zxing/wiki/Scanning-Via-Intent
        // TODO: Update deprecated code
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (scanResult != null) {
            if (scanResult.contents == null) {
                Toast.makeText(context, "Barcode scan cancelled! ", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "$scanResult received from barcode scanner!")
            } else {
                Toast.makeText(context, "Scanned -> " + scanResult.contents, Toast.LENGTH_SHORT).show()

                val results = scanResult.contents
                val newProduct = sharedViewModel.getProductFromBarcodeScanResult(results)

                if(newProduct != null){
                    Log.d(TAG, "$newProduct was created.")
                    findNavController()
                    // TODO: Navigate to results page and ask user for permission to store new product
                }
            }
        } else {
            Toast.makeText(context, "Barcode scan failed! ", Toast.LENGTH_SHORT).show()
            Log.d("Fragment", "$scanResult")
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var selectedLabel = ""
        if (parent != null) {
            selectedLabel = parent.getItemAtPosition(position).toString() // TODO: ProductLabel cannot be empty!!
            Log.d(TAG, "Item $selectedLabel product label is selected.")
        }
        productLabel = selectedLabel
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        productLabel = ""
        // Alert Dialog
        val alertBuilder = createAlert()
        // Show dialog
        alertDialog = alertBuilder.create()
        alertDialog.show()
        Log.d(TAG, "No value in dropdown menu selected.")
    }

    private fun createAlert(): AlertDialog.Builder {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        // Use custom view
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_layout, null)
        builder.setView(dialogView)
        // views
        val editText = dialogView.findViewById<View>(R.id.dialog_alert_text) as TextView
        val okButton = dialogView.findViewById<View>(R.id.dialog_button_export) as Button
        val cancelButton = dialogView.findViewById<View>(R.id.dialog_button_cancel) as Button

        editText.text = "Bitte wähle einen Produkttyp aus der Dropdownlist aus."
        // confirm and cancel button
        builder.setCancelable(true)

        okButton.setOnClickListener {
            alertDialog.cancel()
            Log.d(TAG, "The alert dialog is cancelled.")
        }

        cancelButton.setOnClickListener {
            // Cancel export
            alertDialog.cancel()
            Log.d(TAG, "The alert dialog is cancelled.")
        }

        return builder
    }

    private fun calculateExpDate(productLabel: Label) : String {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
        val expDays = productLabel.calculateExpirationDays()
        val result = addDaysToCurrentDate(expDays)
        return sdf.format(result.time)
    }

    companion object {
        const val TAG = "AddProductToInventoryFragment"
        fun newInstance() = AddProductToInventoryFragment()
    }
}