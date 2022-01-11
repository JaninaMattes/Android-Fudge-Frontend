package com.mobilesystems.feedme.data.repository

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.domain.model.User

interface AuthRepository {

    suspend fun login(username: String, password: String): Response<User>

    suspend fun register(username: String, email: String, password: String, passwordConfirm: String): Response<User>

    suspend fun logout(username: String, password: String)
}