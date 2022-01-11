package com.mobilesystems.feedme.data.repository

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.datasource.AuthDataSourceImpl
import com.mobilesystems.feedme.domain.model.User
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class AuthRepositoryImpl @Inject constructor(private val dataSourceImpl: AuthDataSourceImpl) : AuthRepository{

    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    override suspend fun login(username: String, password: String): Response<User> {
        // handle login
        val result = dataSourceImpl.login(username, password)

        if (result is Response.Success) {
            setLoggedInUser(result.data)
        }
        return result
    }

    override suspend fun register(username: String, email: String, password: String, passwordConfirm: String): Response<User> {

        val result = dataSourceImpl.register(username, email, password, passwordConfirm)

        if (result is Response.Success) {
            setLoggedInUser(result.data)
        }
        return result
    }

    override suspend fun logout(username: String, password: String) {
        user = null
        dataSourceImpl.logout(username, password)
    }

    private fun setLoggedInUser(user: User?) {
        this.user = user
        // TODO: Use Keystore
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}