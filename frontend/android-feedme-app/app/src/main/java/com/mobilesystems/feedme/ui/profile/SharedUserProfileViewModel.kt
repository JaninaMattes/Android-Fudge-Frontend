package com.mobilesystems.feedme.ui.profile

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.data.repository.UserRepositoryImpl
import com.mobilesystems.feedme.domain.model.FoodType
import com.mobilesystems.feedme.domain.model.Settings
import com.mobilesystems.feedme.domain.model.User
import com.mobilesystems.feedme.ui.common.utils.getLoggedInUser
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        Log.d("UserViewModel", "Loading all data.")
        val context = getApplication<Application>().applicationContext
        // pre-fetch user from sharedPreferences
        getCurrentUserId(context)
        // load user from backend
        loadLoggedIndUser()
    }

    override fun loadLoggedIndUser() {
        viewModelScope.launch {
            // This is a coroutine scope with the lifecycle of the ViewModel
            val userId = currentUserId.value
            if(userId != null) {
                val result = userRepository.getLoggedInUser(userId)
                _loggedInUser.postValue(result)
                Log.d("UserViewModel", "Loaded User Id ${loggedInUser.value?.userId}, " +
                        "Name ${loggedInUser.value?.firstName}")
            }else{
                Log.e("UserViewModel", "No user id found.")
                // TODO: If not user, then log user out
            }
        }
        loadLoggedInUserFoodTypeList()
    }

    override fun updateLoggedInUser(user: User) {
        viewModelScope.launch {
            // Update user profile information
            userRepository.updateLoggedInUser(user)
            _loggedInUser = userRepository.currentLoggedInUser
        }
    }

    override fun deleteLoggedInUser() {
        viewModelScope.launch {
            val userId = currentUserId.value
            if(userId != null) {
                userRepository.deleteUser(userId)
            }
        }
    }

    override fun loadLoggedInUserFoodTypeList(): LiveData<List<FoodType>?> {
        // Helper function
        var tempUser = loggedInUser.value
        if(tempUser != null) {
            _loggedInUserFoodTypeList.value = tempUser.dietaryPreferences
        }else{
            Log.d("UserViewModel", "User is null.")
        }
        return loggedInUserFoodTypeList
    }

    override fun updateExpirationReminderSetting(remindMe: Boolean) {
        viewModelScope.launch {
            var tempUser = loggedInUser.value
            if (tempUser != null) {
                var settings = Settings(
                    remindMe,
                    tempUser.userSettings?.allowPushNotifications,
                    tempUser.userSettings?.suggestRecipes
                )
                tempUser.userSettings = settings
                userRepository.updateLoggedInUser(tempUser)
                _loggedInUser = userRepository.currentLoggedInUser
            }
        }
    }

    override fun updatePushNotficicationsSetting(remindMe: Boolean) {
        viewModelScope.launch {
            var tempUser = loggedInUser.value
            if (tempUser != null) {
                var settings = Settings(
                    tempUser.userSettings?.reminderProductExp,
                    remindMe, tempUser.userSettings?.suggestRecipes
                )
                tempUser.userSettings = settings
                userRepository.updateLoggedInUser(tempUser)
                _loggedInUser = userRepository.currentLoggedInUser
            }
        }
    }

    override fun updateRecommendShopplingListSetting(remindMe: Boolean) {
        viewModelScope.launch {
            var tempUser = loggedInUser.value
            if (tempUser != null) {
                var settings = Settings(
                    tempUser.userSettings?.reminderProductExp,
                    tempUser.userSettings?.allowPushNotifications,
                    remindMe
                )
                // TODO fix setter and getter for user
                tempUser.userSettings = settings
                userRepository.updateLoggedInUser(tempUser)
                _loggedInUser = userRepository.currentLoggedInUser
            }
        }
    }

    override fun updateUserProfile() {
        viewModelScope.launch {
            val user = loggedInUser.value
            if (user != null) {
                userRepository.persistUserProfile(user)
            }
        }
    }

    private fun getCurrentUserId(context: Context): LiveData<Int?>{
        val result = getLoggedInUser(context)
        _currentUserId.value = result?.userId
        return  currentUserId
    }
}