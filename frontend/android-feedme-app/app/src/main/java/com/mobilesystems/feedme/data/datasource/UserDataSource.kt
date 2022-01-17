package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Resource
import com.mobilesystems.feedme.data.response.UserResponse
import com.mobilesystems.feedme.domain.model.User

interface UserDataSource {

    suspend fun getUserById(userId: Int) : Resource<UserResponse?>

    suspend fun updateUserById(user: User) : Resource<Int?>

    suspend fun deleteUserById(userId: Int): Resource<Int?>

    suspend fun isUserLoggedIn(userId: Int): Resource<Boolean?>

}