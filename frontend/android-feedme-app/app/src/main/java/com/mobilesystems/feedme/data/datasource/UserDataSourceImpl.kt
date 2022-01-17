package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Resource
import com.mobilesystems.feedme.data.remote.FoodTrackerApi
import com.mobilesystems.feedme.data.response.UserResponse
import com.mobilesystems.feedme.di.AppModule
import com.mobilesystems.feedme.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val foodTrackerApi: FoodTrackerApi,
    @AppModule.DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    BaseDataSource(), UserDataSource {

    // TODO: Pass token for authentification
    override suspend fun getUserById(userId: Int): Resource<UserResponse> = withContext(ioDispatcher) {
        getResult { foodTrackerApi.getUserById(userId) }
    }

    override suspend fun updateUserById(user: User) = withContext(ioDispatcher) {
        getResult { foodTrackerApi.updateUser(user) }
    }

    override suspend fun deleteUserById(userId: Int) = withContext(ioDispatcher) {
        getResult { foodTrackerApi.deleteUser(userId) }
    }

    override suspend fun isUserLoggedIn(userId: Int)= withContext(ioDispatcher) {
        getResult { foodTrackerApi.getIsUserLoggedIn(userId) }
    }

}