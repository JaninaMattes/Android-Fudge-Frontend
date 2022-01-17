package com.mobilesystems.feedme.ui.register

interface BaseRegisterViewModel{

    fun register(username: String, email: String, password: String, passwordConfirm: String)

    fun registerDataChanged(username: String, email: String, password: String, passwordConfirm: String)

}