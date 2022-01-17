package com.mobilesystems.feedme.data.repository

import com.mobilesystems.feedme.common.networkresult.Resource

interface AuthRepository {

    suspend fun login(email: String, password: String) : Resource<Map<String, String>>

    suspend fun register(firstname: String, lastname: String, email: String, password: String) : Resource<Map<String, String>>

    suspend fun logout(username: String, password: String)
}