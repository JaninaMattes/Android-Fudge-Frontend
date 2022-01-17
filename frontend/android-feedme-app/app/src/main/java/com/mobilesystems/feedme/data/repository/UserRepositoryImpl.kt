package com.mobilesystems.feedme.data.repository

import androidx.lifecycle.MutableLiveData
import com.mobilesystems.feedme.data.datasource.UserDataSourceImpl
import com.mobilesystems.feedme.domain.model.User
import com.mobilesystems.feedme.ui.common.utils.convertUserResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSourceImpl: UserDataSourceImpl) : UserRepository {

    // in-memory cache of the fetched objects
    var currentLoggedInUser: MutableLiveData<User?> = MutableLiveData<User?>()
        private set
    var isUserLoggedIn: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
        private set

    override suspend fun getLoggedInUser(userId: Int): User? {
        // get currently logged in user
        val result = dataSourceImpl.getUserById(userId)
        // convert userResponse object to user object
        val user = convertUserResponse(result.data)
        currentLoggedInUser.postValue(user)
        return user
    }

    override suspend fun updateLoggedInUser(user: User) {
        // get all products for current user
        dataSourceImpl.updateUserById(user)
        getLoggedInUser(user.userId.toInt())
    }

    override suspend fun persistUserProfile(user: User): Unit {
        dataSourceImpl.updateUserById(user)
    }

    override suspend fun isUserLoggedIn(userId: Int): MutableLiveData<Boolean?> {
        val result = dataSourceImpl.isUserLoggedIn(userId)

        if (result.data != null) {
            isUserLoggedIn.postValue(result.data)
        }
        return isUserLoggedIn
    }

    override suspend fun deleteUser(userId: Int) {
        dataSourceImpl.deleteUserById(userId)
    }
}