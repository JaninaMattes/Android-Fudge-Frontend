package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.placeholder.UserPlaceholderContent
import com.mobilesystems.feedme.data.remote.FoodTrackerApi
import com.mobilesystems.feedme.di.AppModule
import com.mobilesystems.feedme.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val foodTrackerApi: FoodTrackerApi,
    @AppModule.DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : UserDataSource {

    // TODO: Pass token for authentification
    override suspend fun loadUser(userId: Int): Response<User?> {
        return try {
            // TODO: Remove placeholder data with network call to backend
            val fakeUser = UserPlaceholderContent.USER_ITEM
            Response.Success(fakeUser)

        } catch (e: Throwable) {
            Response.Error("IOException")
        }
    }

    override suspend fun updateUser(user: User): Response<User?> {
        return try {
            // TODO: Remove placeholder data with network call to backend
            val fakeUser = user
            Response.Success(fakeUser)

        } catch (e: Throwable) {
            Response.Error("IOException")
        }

    }

    override suspend fun deleteLoggedInUser(userId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun isUserLoggedIn(userId: Int): Response<Boolean?> {
        TODO("Not yet implemented")
    }

}