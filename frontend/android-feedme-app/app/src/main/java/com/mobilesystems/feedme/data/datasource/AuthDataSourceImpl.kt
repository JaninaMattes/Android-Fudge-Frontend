package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.placeholder.UserPlaceholderContent
import com.mobilesystems.feedme.data.remote.FoodTrackerApi
import com.mobilesystems.feedme.di.AppModule
import com.mobilesystems.feedme.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 *
 * Tutorial Android Coroutines: https://developer.android.com/kotlin/coroutines/coroutines-best-practices
 */
class AuthDataSourceImpl @Inject constructor(
    private val foodTrackerApi: FoodTrackerApi,
    @AppModule.DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : AuthDataSource {

    override suspend fun login(username: String, password: String): Response<User> = withContext(
        ioDispatcher) {
        pLogin(username, password)
        // TODO: Remove placeholder
        // foodTrackerApi.loginUser(username, password)
    }

    override suspend fun register(username: String, email: String, password: String, passwordConfirm: String) = withContext(
        defaultDispatcher) {
        pRegister(username, email, password, passwordConfirm)
        //foodTrackerApi.registerUser(username, email, password, passwordConfirm)
    }

    override suspend fun logout(username: String, password: String) = withContext(
        defaultDispatcher) { foodTrackerApi.logout(username, password) }


    suspend fun pLogin(username: String, password: String): Response<User> {
        return try {
            val fakeUser = UserPlaceholderContent.USER_ITEM
            Response.Success(fakeUser)
        } catch (e: Throwable) {
            Response.Error("Error logging user in.")
        }
    }

    suspend fun pRegister(username: String, email: String, password: String, passwordConfirm: String): Response<User> {
        return try {
            val fakeUser = UserPlaceholderContent.USER_ITEM
            Response.Success(fakeUser)
        } catch (e: Throwable) {
            Response.Error("Error registering user.")
        }
    }
}
