package com.mobilesystems.feedme.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.ui.common.utils.getLoggedInUser
import com.mobilesystems.feedme.data.repository.UserRepositoryImpl
import com.mobilesystems.feedme.domain.model.FoodType
import com.mobilesystems.feedme.domain.model.Settings
import com.mobilesystems.feedme.domain.model.User
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
    private var _currentUser = MutableLiveData<Int?>()

    val loggedInUser : LiveData<User?>
        get() = _loggedInUser

    val loggedInUserFoodTypeList : LiveData<List<FoodType>?>
        get() = _loggedInUserFoodTypeList

    val currentUser : LiveData<Int?>
        get() = _currentUser

    init {

        val context = getApplication<Application>().applicationContext
        getCurrentUser(context)
        val userId = currentUser.value

        if(userId != null) {
            loadLoggedIndUser()
            loadLoggedInUserFoodTypeList()
        }
    }

    override fun loadLoggedIndUser() {
        viewModelScope.launch {
            // This is a coroutine scope with the lifecycle of the ViewModel
            val userId = currentUser.value

            if(userId != null) {
                userRepository.getLoggedInUser(userId)
                _loggedInUser = userRepository.currentLoggedInUser
            }
        }
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
            val userId = currentUser.value
            if(userId != null) {
                userRepository.deleteUser(userId)
            }
        }
    }

    override fun loadLoggedInUserFoodTypeList(): LiveData<List<FoodType>?> {
        // Helper function
        var tempUser = loggedInUser.value
        if(tempUser != null) {
            _loggedInUserFoodTypeList.value = tempUser.userTags
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

    private fun getCurrentUser(context: Context): LiveData<Int?>{
        val result = getLoggedInUser(context)
        _currentUser.value = result
        return  currentUser
    }
}