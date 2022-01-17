package com.mobilesystems.feedme.ui.dashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.databinding.DashboardFragmentBinding
import com.mobilesystems.feedme.domain.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val sharedViewModel: SharedDashboardViewModel by activityViewModels()

    // view binding
    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = _binding!!

    private val CHANNEL_ID = "Expiring_notification_channel"
    private val NOTIFICATION_ID = 1

    // This property is only valid between onCreateView and onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //inflate layout for the fragment
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)

        val userProfileName: TextView = binding.dashboardUserProfileName
        val moreButtonOne: TextView = binding.dashboardMoreButtonOne
        val moreButtonTwo: TextView = binding.dashboardMoreButtonTwo
        val pushButton: ExtendedFloatingActionButton = binding.btnPush


        // TODO: Check if user is not logged in
        // Create the observer which updates the UI.
        val userObserver = Observer<User?> { user : User? ->
            if (user != null) {
                // use picasso to locally store image
                userProfileName.text = user.firstName
                Log.d(TAG, "User ${user.firstName} is logged in.")
            } else {
                // TODO: Log User out
                Log.d(TAG, "No user is logged in.")
            }
        }

        moreButtonOne.setOnClickListener {
            val context = activity?.applicationContext
            Toast.makeText(context,"Not yet implemented!!", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Show more is clicked.")
        }

        moreButtonTwo.setOnClickListener {
            val context = activity?.applicationContext
            Toast.makeText(context,"Not yet implemented!!", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Show more is clicked.")
        }

        //TODO set expiringdate to showNotification
        pushButton.setOnClickListener{
            showNotification()
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        sharedViewModel.loggedInUser.observe(viewLifecycleOwner, userObserver)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Add ingredient list as child fragment

        Log.d(TAG, "Called onViewCreated.")

        val recipeListFragment = DashboardRecipeListFragment()
        val productListFragment = DashboardExpiringProductListFragment()

        addChildFragment(R.id.number_one_recipe_list_fragment, recipeListFragment)
        addChildFragment(R.id.expiring_products_list_fragment, productListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "Called onDestroyView.")
    }

    private fun addChildFragment(viewId: Int, childFragment: Fragment){
        // nest child fragment into parent fragment
        // https://developer.android.com/about/versions/android-4.2#NestedFragments
        val child = childFragmentManager.findFragmentById(viewId)

        if(child == null){
            childFragmentManager.beginTransaction().apply {
                add(viewId, childFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    //push-notifikation für ablaufende Produkte
    fun showNotification(){
        // TODO: Daten abrufen aus der Expiring Liste und 3-5 aktuell ablaufende Produkte anzeigen
        var context = activity?.applicationContext
        var builder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID).apply{
                setSmallIcon(R.drawable.ic_fridgeicon)
                setContentTitle("Ablaufende Produkte im Inventar")
                setContentText("Du hast ablaufende Produkte in Deinem Inventar!")
                setPriority(NotificationCompat.PRIORITY_DEFAULT)
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName = "Expiring_Channel"
            val channelDescription = "ExpiringProduct_Channel for expiring products"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = channelDescription
            }
            val notifyManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifyManager.createNotificationChannel(channel)
        }

        with(context?.let { NotificationManagerCompat.from(it) })
        {
            builder?.let { this?.notify(NOTIFICATION_ID, it.build() ) }
        }

    }

    companion object {
        const val TAG = "DashboardFragment"
        fun newInstance() = DashboardFragment()
    }

}