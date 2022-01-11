package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.domain.model.User

interface UserDataSource {

    suspend fun loadUser(userId: Int): Response<User?>

    suspend fun updateUser(user: User): Response<User?>

    suspend fun deleteLoggedInUser(userId: Int)

    suspend fun isUserLoggedIn(userId: Int): Response<Boolean?>

}