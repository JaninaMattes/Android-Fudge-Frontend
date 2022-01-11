package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.domain.model.Settings
import com.mobilesystems.feedme.domain.model.User

interface UserRepository {

    suspend fun getLoggedInUser(userId: Int): MutableLiveData<User?>

    suspend fun updateLoggedInUser(user: User): Unit

    suspend fun persistUserProfile(user: User): Unit

    suspend fun isUserLoggedIn(userId: Int): MutableLiveData<Boolean?>

    suspend fun deleteUser(userId: Int)

}