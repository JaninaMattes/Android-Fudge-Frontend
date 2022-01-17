package com.mobilesystems.feedme.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.FoodType
import com.mobilesystems.feedme.domain.model.Settings
import com.mobilesystems.feedme.domain.model.User
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent

import android.os.Build




@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val sharedViewModel: SharedUserProfileViewModel by activityViewModels()

    private lateinit var user: User
    private lateinit var userName: String
    private var userId: Int = 0
    private var userImage: String? = null
    private var userSettings: Settings? = null
    private var userTags: List<FoodType>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflate root
        val rootView =  inflater.inflate(R.layout.user_profile_fragment, container, false)

        // view elements by id
        val profileImageView: ImageView = rootView.findViewById(R.id.user_profile_image)
        val profileFirstName: TextView = rootView.findViewById(R.id.user_profile_firstname)
        val profileLastName: TextView = rootView.findViewById(R.id.user_profile_lastname)
        val profileEmail: TextView = rootView.findViewById(R.id.user_profile_email)
        val profilePassword: TextView = rootView.findViewById(R.id.user_profile_password)
        
        //set elements non-editable
        profileFirstName.isEnabled = false
        profileLastName.isEnabled = false
        profileEmail.isEnabled = false
        profilePassword.isEnabled = false

        // Buttons
        val btnAddRecipeLabel: ImageButton = rootView.findViewById(R.id.button_add_food_label)
        val btnEditUserProfile: Button = rootView.findViewById(R.id.btn_edit_user_profile)
        val btnSaveUser: Button = rootView.findViewById(R.id.btn_save_user)
        val btnCancelUser: Button = rootView.findViewById(R.id.btn_cancel_save_user)

        val btnExpirationReminder: SwitchCompat = rootView.findViewById(R.id.button_toggle_one)
        val btnPushNotification: SwitchCompat = rootView.findViewById(R.id.button_toggle_two)
        val btnRecommendShoppingList: SwitchCompat = rootView.findViewById(R.id.button_toggle_three)

        // Create the observer which updates the UI.
        val userObserver = Observer<User?> { user : User? ->
            if (user != null) {
                // use picasso to locally store image
                userId = user.userId
                userImage = user.userImage
                if(userImage.isNullOrEmpty()){
                    profileImageView.setImageDrawable(ResourcesCompat.getDrawable(resources,
                        R.drawable.default_user_profile, null))
                }else{
                    Picasso.get().load(userImage).into(profileImageView)
                }
                userName = user.firstName
                profileFirstName.text = user.firstName
                profileLastName.text = user.lastName
                profileEmail.text = user.email
                profilePassword.text = user.password // TODO fix password

                userSettings = user.userSettings
                userTags = user.dietaryPreferences
                // Setup buttons toggled
                btnExpirationReminder.isChecked = user.userSettings?.reminderProductExp == true
                btnPushNotification.isChecked = user.userSettings?.allowPushNotifications == true
                btnRecommendShoppingList.isChecked = user.userSettings?.suggestRecipes == true
            } else {
                Log.d("UserProfileFragment", "User is null.")
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        sharedViewModel.loggedInUser.observe(viewLifecycleOwner, userObserver)

        profileImageView.setOnClickListener {
            // TODO: Implement image picking from Gallery/Photo 
            val context = activity?.applicationContext
            Toast.makeText(context,"Not yet implemented!!", Toast.LENGTH_SHORT).show()
        }

        btnAddRecipeLabel.setOnClickListener{
            // TODO: Implement shared view model for products behind button
            val context = activity?.applicationContext
            Toast.makeText(context,"Not yet implemented!!", Toast.LENGTH_SHORT).show()
        }

        btnExpirationReminder.setOnClickListener{
            // TODO: Implement action behind button
            val context = activity?.applicationContext
            Toast.makeText(context,"Not yet implemented!!", Toast.LENGTH_SHORT).show()
            val remindMe = btnExpirationReminder.isChecked
            sharedViewModel.updateExpirationReminderSetting(remindMe)
        }

        btnPushNotification.setOnClickListener{
            // TODO: Implement action behind button
            val context = activity?.applicationContext
            Toast.makeText(context,"Not yet implemented!!", Toast.LENGTH_SHORT).show()
            val remindMe = btnPushNotification.isChecked
            sharedViewModel.updatePushNotficicationsSetting(remindMe)
        }

        btnRecommendShoppingList.setOnClickListener{
            // TODO: Implement action behind button
            val context = activity?.applicationContext
            Toast.makeText(context,"Not yet implemented!!", Toast.LENGTH_SHORT).show()
            val remindMe = btnRecommendShoppingList.isChecked
            sharedViewModel.updateRecommendShopplingListSetting(remindMe)
        }

        btnEditUserProfile.setOnClickListener {
            //set editbutton visibility gone
            btnEditUserProfile.visibility = View.GONE

            //set visibility of save/cancel btn visible
            btnSaveUser.visibility = View.VISIBLE
            btnCancelUser.visibility = View.VISIBLE

            //set editText editable
            profileFirstName.isEnabled = true
            profileLastName.isEnabled = true
            profileEmail.isEnabled = true
            profilePassword.isEnabled = true
        }

        btnCancelUser.setOnClickListener {
            //set editbutton visibility visible
            btnEditUserProfile.visibility = View.VISIBLE

            //set visibility of save/cancel btn gone
            btnSaveUser.visibility = View.GONE
            btnCancelUser.visibility = View.GONE

            //set editText non-editable
            profileFirstName.isEnabled = false
            profileLastName.isEnabled = false
            profileEmail.isEnabled = false
            profilePassword.isEnabled = false
        }

        btnSaveUser.setOnClickListener {
            var firstName = profileFirstName.text.toString()
            val lastName = profileLastName.text.toString()
            val email = profileEmail.text.toString()
            val password = profilePassword.text.toString()

            // TODO check if editText is empty

            user = User(userId, firstName, lastName, email, password, userSettings, userTags, userImage)

            sharedViewModel.updateLoggedInUser(user)

            //set editbutton visibility visible
            btnEditUserProfile.visibility = View.VISIBLE

            //set visibility of save/cancel btn gone
            btnSaveUser.visibility = View.GONE
            btnCancelUser.visibility = View.GONE

            //set editText non-editable
            profileFirstName.isEnabled = false
            profileLastName.isEnabled = false
            profileEmail.isEnabled = false
            profilePassword.isEnabled = false

        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Add ingredient list as child fragment
        addChildFragment()
    }

    private fun addChildFragment(){
        // nest child fragment into parent fragment
        // https://developer.android.com/about/versions/android-4.2#NestedFragments
        val child = childFragmentManager.findFragmentById(R.id.user_tag_list_fragment)
        val tagListFragment = UserFoodPrefListFragment()

        if(child == null) {
            childFragmentManager.beginTransaction().apply {
                add(R.id.user_tag_list_fragment, tagListFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //    _binding = null
        // Persist current state
        sharedViewModel.updateUserProfile()
    }

    companion object {
        const val TAG = "UserProfileFragment"
        fun newInstance() = UserProfileFragment()
    }
}