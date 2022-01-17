package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.data.remote.FoodTrackerApi
import com.mobilesystems.feedme.data.request.LoginRequest
import com.mobilesystems.feedme.data.request.RegisterRequest
import com.mobilesystems.feedme.di.AppModule
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
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : BaseDataSource(), AuthDataSource {

    /**
     * As this operation is manually retrieving the news from the server
     * using a blocking HttpURLConnection, it needs to move the execution
     * to an IO dispatcher to make it main-safe.
     */
    suspend fun login(request: LoginRequest) = withContext(ioDispatcher) {
        getResult { foodTrackerApi.loginUser(request) }
    }

    suspend fun register(request: RegisterRequest) = withContext(ioDispatcher) {
        getResult { foodTrackerApi.registerUser(request) }
    }

    override suspend fun logout(username: String, password: String): Unit = withContext(ioDispatcher) {
        //foodTrackerApi.logout(username, password)
    }
}