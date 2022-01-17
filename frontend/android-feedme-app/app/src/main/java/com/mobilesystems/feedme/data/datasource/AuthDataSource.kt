package com.mobilesystems.feedme.data.datasource

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.domain.model.User

interface AuthDataSource {


    suspend fun logout(username: String, password: String)

}