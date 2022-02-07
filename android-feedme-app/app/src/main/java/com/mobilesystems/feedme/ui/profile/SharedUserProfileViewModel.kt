package com.mobilesystems.feedme.ui.profile

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.data.repository.UserRepositoryImpl
import com.mobilesystems.feedme.domain.model.FoodType
import com.mobilesystems.feedme.domain.model.Image
import com.mobilesystems.feedme.domain.model.Settings
import com.mobilesystems.feedme.domain.model.User
import com.mobilesystems.feedme.ui.common.utils.*
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SharedViewModel to propagate shared Data between Fragments.
 */
@HiltViewModel
class SharedUserProfileViewModel @Inject constructor(
    androidApplication: Application,
    private val userRepository: UserRepositoryImpl) :
    BaseViewModel(androidApplication), BaseUserProfileViewModel {

    private var _loggedInUser = MutableLiveData<User?>()
    private var _loggedInUserFoodTypeList = MutableLiveData<List<FoodType>?>()
    private var _currentUserId = MutableLiveData<Int?>()

    val loggedInUser : LiveData<User?>
        get() = _loggedInUser

    val loggedInUserFoodTypeList : LiveData<List<FoodType>?>
        get() = _loggedInUserFoodTypeList

    val currentUserId : LiveData<Int?>
        get() = _currentUserId

    init {
        if(currentUserId.value == null || currentUserId.value == 0){
            val context = getApplication<Application>().applicationContext
            // get pre-fetched user data from sharedPreferences
            getCurrentUserId(context)
            getPreFetchedUserData(context)
        }
        // load full user data from backend
        loadLoggedInUser()

        if(foodTypesHasNoValues()){
            loadLoggedInUserFoodTypeList()
        }
    }

    override fun loadLoggedInUser() {
        viewModelScope.launch {
            // This is a coroutine scope with the lifecycle of the ViewModel
            val userId = currentUserId.value
            if(userId != null && userId != 0) {
                try{
                    val result = userRepository.getLoggedInUser(userId)
                    _loggedInUser.value = result
                }catch (e: Exception){
                    Log.d("UserProfile", "Error occured $e")
                    e.stackTrace
                }
            }else{
                Log.e("UserViewModel", "No user found.")
            }
        }
    }

    override fun updateLoggedInUser(user: User) {
        viewModelScope.launch {
            // Update user profile information
            try{
                userRepository.updateLoggedInUser(user)
                _loggedInUser.value = user
            }catch (e: Exception){
                Log.d("UserProfile", "Error occured $e")
                e.stackTrace
            }
        }
    }

    override fun updateUserImage(image: Image) {
        viewModelScope.launch {
            // Update user profile information
            val userId = currentUserId.value
            if(userId != null && userId != 0){
                try{
                    userRepository.updateUserImage(userId, image)
                }catch (e: Exception){
                    Log.d("UserProfile", "Error occured $e")
                    e.stackTrace
                }
            }else{
                Log.d("UserViewModel", "No user found.")
            }
        }
    }

    override fun deleteLoggedInUser() {
        viewModelScope.launch {
            val userId = currentUserId.value
            if(userId != null && userId != 0) {
                try{
                    userRepository.deleteUser(userId)
                    _loggedInUser.value = null
                    Log.d("UserViewModel", "Delete user.")
                }catch (e: Exception){
                    Log.d("UserProfile", "Error occured $e")
                    e.stackTrace
                }
            }else{
                Log.d("UserViewModel", "No user found.")
            }
        }
    }

    override fun loadLoggedInUserFoodTypeList(): LiveData<List<FoodType>?> {
        // Helper function
        val tempUser = loggedInUser.value
        try{
            if(tempUser != null) {
                _loggedInUserFoodTypeList.value = tempUser.dietaryPreferences
            }else{
                Log.d("UserViewModel", "User is null.")
            }
        }catch (e: Exception){
            Log.d("UserProfile", "Error occured $e")
            e.stackTrace
        }
        return loggedInUserFoodTypeList
    }

    override fun updateExpirationReminderSetting(remindMe: Boolean) {
        viewModelScope.launch {
            val tempUser = loggedInUser.value
            try{
                if (tempUser != null) {
                    val settings = Settings(
                        remindMe,
                        tempUser.userSettings?.allowPushNotifications,
                        tempUser.userSettings?.suggestRecipes
                    )
                    tempUser.userSettings = settings
                    userRepository.allowReminder(tempUser.userId, remindMe)
                }else{
                    Log.d("UserViewModel", "User is null.")
                }
            }catch (e: Exception){
                Log.d("UserProfile", "Error occured $e")
                e.stackTrace
            }
        }
    }

    override fun updatePushNotficicationsSetting(remindMe: Boolean) {
        viewModelScope.launch {
            val tempUser = loggedInUser.value
            try{
                if (tempUser != null) {
                    val settings = Settings(
                        tempUser.userSettings?.reminderProductExp,
                        remindMe,
                        tempUser.userSettings?.suggestRecipes
                    )
                    tempUser.userSettings = settings
                    userRepository.allowPushNotification(tempUser.userId, remindMe)
                }else{
                    Log.d("UserViewModel", "User is null.")
                }
            }catch (e: Exception){
                Log.d("UserProfile", "Error occured $e")
                e.stackTrace
            }
        }
    }

    // Future feature to get recommendations for shoppinglist
    override fun updateRecommendShopplingListSetting(remindMe: Boolean) {
        viewModelScope.launch {
            val tempUser = loggedInUser.value
            try{
                if (tempUser != null) {
                    val settings = Settings(
                        tempUser.userSettings?.reminderProductExp,
                        tempUser.userSettings?.allowPushNotifications,
                        remindMe
                    )
                    tempUser.userSettings = settings
                    userRepository.allowSuggestion(tempUser.userId, remindMe)
                }else{
                    Log.d("UserViewModel", "User is null.")
                }
            }catch (e: Exception){
                Log.d("UserProfile", "Error occured $e")
                e.stackTrace
            }
        }
    }

    fun refresh(){
        // refresh after certain time for better user experience
        viewModelScope.launch {
            try{
                delay(1500)
                val userId = loggedInUser.value?.userId
                if(userId != null && userId != 0) {
                    val result = userRepository.getLoggedInUser(userId)
                    _loggedInUser.value = result
                }
            }catch (e: Exception){
                Log.d("UserProfile", "Error occured $e")
                e.stackTrace
            }
        }
    }

    private fun getCurrentUserId(context: Context): LiveData<Int?>{
        val result = getLoggedInUser(context)
        _currentUserId.value = result?.userId
        return currentUserId
    }

    private fun getPreFetchedUserData(context: Context){
        if(doesPreferenceExist(context, "mPreference", "userData")) {
            try{
                val result = getUserDataFromSharedPreference(context)
                if (result != null) {
                    removeDataFromSharedPreferences(context, result)
                    val user = convertUserDataToUser(result)
                    _loggedInUser.value = user
                }
            }catch (e: Exception){
                Log.d("UserProfile", "Error occured $e")
                e.stackTrace
            }
        }
    }

    private fun foodTypesHasNoValues(): Boolean{
        return loggedInUserFoodTypeList.value.isNullOrEmpty()
    }
}