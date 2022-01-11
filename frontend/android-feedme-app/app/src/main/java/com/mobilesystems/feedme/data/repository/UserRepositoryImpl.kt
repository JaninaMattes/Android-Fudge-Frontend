package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.UserDataSourceImpl
import com.mobilesystems.feedme.domain.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val dataSourceImpl: UserDataSourceImpl) : UserRepository{

    // in-memory cache of the fetched objects
    var currentLoggedInUser: MutableLiveData<User?> = MutableLiveData<User?>()
        private set
    var isUserLoggedIn: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
        private set

    override suspend fun getLoggedInUser(userId: Int): MutableLiveData<User?> {
        // get currently logged in user
        val result = dataSourceImpl.loadUser(userId)

        if (result is Response.Success) {
            currentLoggedInUser.postValue(result.data)
        }
        return currentLoggedInUser
    }

    override suspend fun updateLoggedInUser(user: User) {
        // get all products for current user
        dataSourceImpl.updateUser(user)
        getLoggedInUser(user.userId.toInt())
    }

    override suspend fun persistUserProfile(user: User): Unit {
        dataSourceImpl.updateUser(user)
    }

    override suspend fun isUserLoggedIn(userId: Int): MutableLiveData<Boolean?> {
        val result = dataSourceImpl.isUserLoggedIn(userId)

        if (result is Response.Success) {
            isUserLoggedIn.postValue(result.data)
        }
        return isUserLoggedIn
    }

    override suspend fun deleteUser(userId: Int) {
        dataSourceImpl.deleteLoggedInUser(userId)
    }
}