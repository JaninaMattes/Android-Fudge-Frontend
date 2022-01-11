package com.mobilesystems.feedme.ui.profile

import androidx.lifecycle.LiveData
import com.mobilesystems.feedme.domain.model.FoodType
import com.mobilesystems.feedme.domain.model.User

interface BaseUserProfileViewModel {

    fun loadLoggedIndUser()

    fun updateLoggedInUser(user: User)

    fun deleteLoggedInUser()

    fun loadLoggedInUserFoodTypeList(): LiveData<List<FoodType>?> // Helper function

    fun updateExpirationReminderSetting(remindMe: Boolean)

    fun updatePushNotficicationsSetting(remindMe: Boolean)

    fun updateRecommendShopplingListSetting(remindMe: Boolean)

    fun updateUserProfile()
}